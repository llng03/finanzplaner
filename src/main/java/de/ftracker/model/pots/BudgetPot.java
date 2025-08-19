package de.ftracker.model.pots;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class BudgetPot {
    private String name;
    private List<PotEntry> entries;

    public BudgetPot(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public BigDecimal sum() {
        return entries.stream()
                .map(e -> e.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getName() {
        return name;
    }

    public List<PotEntry> getEntries() {
        return entries;
    }

    public void addEntry(LocalDate date, BigDecimal amount) {
        entries.add(new PotEntry(date, amount));
    }

    public void pay(LocalDate date, BigDecimal amount) {
        addEntry(LocalDate.now(), amount.negate());
    }
}
