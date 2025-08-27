package de.ftracker.services.pots;

import de.ftracker.model.pots.BudgetPot;
import de.ftracker.model.pots.PotForRegularExp;
import de.ftracker.model.pots.PotSummary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class PotManager {

    private final PotRepository potRepository;
    private final PotSummaryRepository potSummaryRepository;

    private PotSummary potSummary;

    public PotManager(PotRepository potRepository, PotSummaryRepository potSummaryRepository){
        this.potRepository = potRepository;
        this.potSummaryRepository = potSummaryRepository;
        this.potSummary = potSummaryRepository.findById(1L)
                .orElseGet(() -> potSummaryRepository.save(new PotSummary()));
    }

    public List<BudgetPot> getPots() {
        return potRepository.findAll();
    }

    public BigDecimal getUndistributed() {
        return potSummary.getUndistributed();
    }

    public void addPot(BudgetPot budgetPot) {
        potRepository.save(budgetPot);
    }

    public BudgetPot getPot(String name) {
        return potRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Kein Pot mit Namen: " + name));

    }

    public void distribute(BigDecimal amount, String potName) {
        distribute(amount, getPot(potName));
    }

    public void distribute(BigDecimal amount, BudgetPot pot) {
        BigDecimal undistributed = getUndistributed();
        if(undistributed.compareTo(amount) < 0) {
            throw new IllegalArgumentException("not enough undistributed amount");
        }
        potSummary.setUndistributed(undistributed.subtract(amount));
        potSummaryRepository.save(potSummary);
        addEntry(pot, LocalDate.now(), amount);
    }

    public void addEntry(BudgetPot pot, LocalDate date, BigDecimal amount) {
        pot.addEntry(LocalDate.now(), amount);
        potRepository.save(pot);
    }

    public void addToUndistributed(BigDecimal amount) {
        potSummary.addToUndistributed(amount);
        potSummaryRepository.save(potSummary);
    }

    public void deletePotByName(String string) {
        BudgetPot pot = getPot(string);
        addToUndistributed(pot.sum());
        potRepository.delete(pot);
    }

    public BigDecimal getTotal() {
        BigDecimal currentUndistributed = potSummary.getUndistributed();
        return currentUndistributed.add(sumAllPots());
    }

    public void update(PotForRegularExp pot, YearMonth curr) {
        pot.update(curr);
        potRepository.save(pot);
    }

    private BigDecimal sumAllPots() {
        return getPots().stream().map(BudgetPot::sum).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}