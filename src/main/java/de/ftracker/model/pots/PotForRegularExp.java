package de.ftracker.model.pots;

import java.time.YearMonth;

public class PotForRegularExp extends BudgetPot{
    public YearMonth lastSaved;

    public YearMonth lastPayed;

    public PotForRegularExp(String name, YearMonth lastSaved, YearMonth lastPayed) {
        super(name);
    }
}
