package de.ftracker.model;

import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.FixedCost;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CostManager {
    //In Tables wird nur hinzugefügt und abhängig vom Monat/Jahr gesucht, dehalb Map
    // Für Statistiken werden chronologisch sortierte Monate benötigt, deshalb TreeMap statt HashMap
    // TreeMap ermöglicht Zugriff in O(log n)
    private Map<YearMonth, CostTables> tables = new TreeMap<>();
    private List<FixedCost> festeAusgaben = new ArrayList<>();
    private List<FixedCost> festeEinnahmen = new ArrayList<>();

    public CostTables getTablesOf(YearMonth yearMonth) {
        return tables.computeIfAbsent(yearMonth, CostTables::new);
    }

    public List<Cost> getMonthsAusgaben(YearMonth month) {
        List<FixedCost> fixedCosts = festeAusgaben.stream()
                .filter(fc -> !fc.getStart().isAfter(month))
                .filter(fc -> fc.getEnd().map(end -> !end.isBefore(month)).orElse(true))
                .collect(Collectors.toList());
        return new ArrayList<>(fixedCosts);
    }

    public List<Cost> getMonthsEinnahmen(YearMonth month) {
        List<FixedCost> fixedCosts = festeEinnahmen.stream()
                .filter(fc -> !fc.getStart().isAfter(month))
                .filter(fc -> fc.getEnd().map(end -> !end.isBefore(month)).orElse(true))
                .collect(Collectors.toList());
        return new ArrayList<>(fixedCosts);
    }

    public List<Cost> getApplicableFixedCosts(YearMonth month) {
        List<Cost> einnahmenUndAusgabenM = getMonthsEinnahmen(month);
        einnahmenUndAusgabenM.addAll(getMonthsAusgaben(month));
        return einnahmenUndAusgabenM;
    }

    /*private List<Cost> getMonthlyCost(List<FixedCost> fixedCosts, YearMonth month) {
        List<Cost> monthlyAusgaben = new ArrayList<>();
        List<Cost> = getApplicableFixedCosts()
        for(FixedCost fixedCost : fixedCosts) {
            double monthlyBetrag;
            switch(fixedCost.getFrequency()) {
                case ANNUAL: monthlyBetrag = fixedCost.getBetrag()/12;
                case SEMI_ANNUAL: monthlyBetrag = fixedCost.getBetrag()/6;
                case QUARTERLY: monthlyBetrag = fixedCost.getBetrag()/4;
                default: monthlyBetrag = fixedCost.getBetrag();
            }
            monthlyAusgaben.add(new Cost(fixedCost.getDesc(), monthlyBetrag));
        }
        return monthlyAusgaben;
    }*/

    public void addToFesteEinnahmen(FixedCost einnahme) {
        festeEinnahmen.add(einnahme);
    }

    public void addToFesteAusgaben(FixedCost ausgaben) {
        festeAusgaben.add(ausgaben);
    }
}
