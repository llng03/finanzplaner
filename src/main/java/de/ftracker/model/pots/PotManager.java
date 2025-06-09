package de.ftracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PotManager {
    private List<BudgetPot> pots = new ArrayList<>();
    private double undistributedAmount;


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

    }

    public void distribute(double amount, BudgetPot pot) {
    }

    public void addToUndistributed(double amount) {

    }

    public double getUndistributedAmount() {
        return undistributedAmount;
    }
}
