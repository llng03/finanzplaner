package de.ftracker.model.pots;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PotManager {
    private List<BudgetPot> pots = new ArrayList<>();
    private BigDecimal undistributed = new BigDecimal(0);


    public List<BudgetPot> getPots() {
        return pots;
    }

    public BigDecimal getUndistributed() {
        return undistributed;
    }

    public void addPot(BudgetPot budgetPot) {
        pots.add(budgetPot);
    }

    public BudgetPot getPot(String name) {
        return pots.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Kein Pot mit Namen: " + name));

    }

    public void distribute(BigDecimal amount, String potName) {
        distribute(amount, getPot(potName));
    }

    public void distribute(BigDecimal amount, BudgetPot pot) {
        if(undistributed.compareTo(amount) < 0) {
            throw new IllegalArgumentException("not enough undistributed amount");
        }
        this.undistributed = undistributed.subtract(amount);
        pot.addEntry(LocalDate.now(), amount);
    }

    public void addToUndistributed(BigDecimal amount) {
        this.undistributed = undistributed.add(amount);
    }

    public void deletePotByName(String string) {
        BudgetPot pot = getPot(string);
        pots.remove(pot);
        addToUndistributed(pot.sum());
    }
}