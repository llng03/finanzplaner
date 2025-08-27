package de.ftracker.services;

import de.ftracker.model.CostTables;
import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.FixedCost;
import de.ftracker.model.costDTOs.FixedCostForm;
import de.ftracker.model.costDTOs.Interval;
import de.ftracker.utils.IntervalCount;
import de.ftracker.utils.MonthlySums;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CostManager {
    private final CostTablesRepository costTablesRepository;
    private final FixedCostsRepository fixedCostsRepository;


    public CostManager(CostTablesRepository costTablesRepository, FixedCostsRepository fixedCostsRepository) {
        this.costTablesRepository = costTablesRepository;
        this.fixedCostsRepository = fixedCostsRepository;
    }

    public CostTables getTablesOf(YearMonth yearMonth) {
        return costTablesRepository.findByMonthAndYear(yearMonth.getMonthValue(), yearMonth.getYear())
                .orElseGet(() -> {
                    CostTables newTables = new CostTables();
                    newTables.setYearMonth(yearMonth);
                    return costTablesRepository.save(newTables);
                });
    }

    public List<FixedCost> getFixedIncome() {
        return fixedCostsRepository.findAll().stream()
                .filter(c -> c.getIsIncome())
                .collect(Collectors.toList());
    }

    public List<FixedCost> getFixedExp() {
        return fixedCostsRepository.findAll().stream()
                .filter(c -> !c.getIsIncome())
                .collect(Collectors.toList());
    }

    public List<Cost> getIncome(YearMonth yearMonth) {
        return getTablesOf(yearMonth).getEinnahmen();
    }

    public List<Cost> getExp(YearMonth yearMonth) {
        return getTablesOf(yearMonth).getAusgaben();
    }

    public List<Cost> getAllMonthsIncome(YearMonth month) {
        List<Cost> income = getIncome(month);
        income.addAll(getMonthsEinnahmen(month));
        return income;
    }

    public List<Cost> getAllMonthsExp(YearMonth month) {
        List<Cost> exp = getExp(month);
        exp.addAll(getMonthsAusgaben(month));
        return exp;
    }

    public List<Cost> getMonthsAusgaben(YearMonth month) {
        return getFixedExp().stream()
                .filter(fc -> !fc.getStart().isAfter(month))
                .filter(fc -> fc.getEnd().map(end -> !end.isBefore(month)).orElse(true))
                .collect(Collectors.toList());
    }

    public List<Cost> getMonthsEinnahmen(YearMonth month) {
        return getFixedIncome().stream()
                .filter(fc -> !fc.getStart().isAfter(month))
                .filter(fc -> fc.getEnd().map(end -> !end.isBefore(month)).orElse(true))
                .collect(Collectors.toList());
    }

    public List<Cost> getApplicableFixedCosts(YearMonth month) {
        List<Cost> einnahmenUndAusgabenM = getMonthsEinnahmen(month);
        einnahmenUndAusgabenM.addAll(getMonthsAusgaben(month));
        return einnahmenUndAusgabenM;
    }

    public static BigDecimal getMonthlyCost(FixedCostForm costForm) {
        return costForm.getBetrag().divide(BigDecimal.valueOf(IntervalCount.countMonths(costForm.getFrequency())));
    }

    public static BigDecimal getMonthlyCost(FixedCost ausgabe) {
        return ausgabe.getBetrag().divide(BigDecimal.valueOf(IntervalCount.countMonths(ausgabe.getFrequency())), 2, RoundingMode.CEILING);
    }

    @Transactional
    public void addIncome(int year, int month, Cost income) {
        CostTables costTables = costTablesRepository.findByMonthAndYear(month, year)
                .orElseThrow();
        costTables.addCostToEinnahmen(income);
    }

    @Transactional
    public void addExp(int year, int month, Cost exp) {
        CostTables costTables = costTablesRepository.findByMonthAndYear(month, year)
                .orElseThrow();
        costTables.addCostToAusgaben(exp);
    }

    @Transactional
    public void addToFixedIncome(FixedCostForm incomeForm) {
        FixedCost fixedCost = new FixedCost();
        fixedCost.setDescr(incomeForm.getDescr());
        fixedCost.setBetrag(incomeForm.getBetrag());
        fixedCost.setIsIncome(incomeForm.getIsIncome());
        fixedCost.setFrequency(incomeForm.getFrequency());
        fixedCost.setStart(incomeForm.getStart());
        fixedCost.setEnd(incomeForm.getEnd());
        addToFixedIncome(fixedCost);
    }

    @Transactional
    public void addToFixedIncome(FixedCost income) {
        System.out.println("now we save: " + income + "into fixedIncomeRepository");
        fixedCostsRepository.save(income);
    }

    @Transactional
    public void addToFixedExp(FixedCostForm expForm) {
        FixedCost fixedCost = new FixedCost();
        fixedCost.setDescr(expForm.getDescr());
        fixedCost.setBetrag(expForm.getBetrag());
        fixedCost.setFrequency(expForm.getFrequency());
        fixedCost.setStart(expForm.getStart());
        fixedCost.setEnd(expForm.getEnd());
        addToFixedExp(fixedCost);
    }

    public void addToFixedExp(FixedCost exp) {
        System.out.println("now we save: " + exp + "into fixedExpRepository");
        if(exp.getFrequency() == Interval.MONTHLY) {
            fixedCostsRepository.save(exp);
        } else {
            fixedCostsRepository.save(
                    new FixedCost(exp.getDescr(), getMonthlyCost(exp), false, Interval.MONTHLY, exp.getStart(), exp.getEndValue())
            );
        }
    }

    public void deleteFromFixedIncome(FixedCost income) {
        fixedCostsRepository.delete(income);
    }



    public void deleteFromFixedIncome(String income, YearMonth start) {
        fixedCostsRepository.deleteByDescrAndStart(income, start.getYear(), start.getMonthValue());
    }

    public void deleteFromFesteAusgaben(FixedCost ausgabe) {
        fixedCostsRepository.delete(ausgabe);
    }

    public void deleteFromFesteAusgaben(String ausgabe, YearMonth start) {
        fixedCostsRepository.deleteByDescrAndStart(ausgabe, start.getYear(), start.getMonthValue());
    }

    public BigDecimal getThisMonthsEinnahmenSum(YearMonth month) {
        List<Cost> einnahmen = getMonthsEinnahmen(month);
        einnahmen.addAll(getTablesOf(month).getEinnahmen());
        return einnahmen.stream()
                .map(Cost::getBetrag)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getThisMonthsAusgabenSum(YearMonth month) {
        List<Cost> ausgaben = getMonthsAusgaben(month);
        ausgaben.addAll(getTablesOf(month).getAusgaben());
        return ausgaben.stream()
                .map(Cost::getBetrag)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public MonthlySums calculateThisMonthsSums(YearMonth month) {
        BigDecimal sumIn = getThisMonthsEinnahmenSum(month);
        BigDecimal sumOut = getThisMonthsAusgabenSum(month);
        return new MonthlySums(sumIn, sumOut);
    }
}
