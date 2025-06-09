package de.ftracker.model.pots;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PotManager {
    private List<BudgetPot> pots = new ArrayList<>();
    private double undistributedAmount = 0;


    public List<BudgetPot> getPots() {
        return pots;
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

    public void distribute(double amount, String potName) {
        distribute(amount, getPot(potName));
    }

    public void distribute(double amount, BudgetPot pot) {
        if(undistributedAmount < amount) {
            throw new IllegalArgumentException("not enough undistributed amount");
        }
        undistributedAmount -= amount;
        pot.addEntry(LocalDate.now(), amount);
    }

    public void addToUndistributed(double amount) {
        undistributedAmount += amount;
    }

    public double getUndistributedAmount() {
        return undistributedAmount;
    }
}
