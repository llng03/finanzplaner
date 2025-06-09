package de.ftracker.model.pots;

import java.time.LocalDate;

public class PotEntry {
    private LocalDate date;
    private double amount;

    public PotEntry(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
