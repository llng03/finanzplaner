package de.ftracker.model.pots;

import de.ftracker.model.costDTOs.Interval;
import de.ftracker.model.costDTOs.IntervalCount;

import java.math.BigDecimal;
import java.time.YearMonth;

public class PotForRegularExp extends BudgetPot{
    private YearMonth lastSaved;

    private YearMonth lastPayed;

    private BigDecimal regularAmount;

    private Interval frequency;



    public PotForRegularExp(String name, YearMonth lastSaved, YearMonth lastPayed, BigDecimal regularAmount, Interval frequency) {
        super(name);
        this.lastSaved = lastSaved;
        this.lastPayed = lastPayed;
        this.regularAmount = regularAmount;
        this.frequency = frequency;
    }

    public void update(YearMonth current) {
        while(!lastSaved.equals(current)) {
            lastSaved = lastSaved.plusMonths(1);
            addEntry(lastSaved.atDay(1), regularAmount);
        }
    }
}
