package de.ftracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetPot {
    private String name;
    private List<PotEntry> entries;

    public BudgetPot(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public double getSum() {
        return entries.stream().mapToDouble(e -> e.getAmount()).sum();
    }

    public String getName() {
        return name;
    }

    public List<PotEntry> getEntries() {
        return entries;
    }

    public void addEntry(LocalDate date, double amount) {
        entries.add(new PotEntry(date, amount));
    }
}
