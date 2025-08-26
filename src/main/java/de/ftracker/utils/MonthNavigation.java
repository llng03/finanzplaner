package de.ftracker.utils;

public class MonthNavigation {
    public final int currYear;
    public final int currMonth;
    public final int nextMonthsYear;
    public final int nextMonth;
    public final int prevMonthsYear;
    public final int prevMonth;

    public MonthNavigation(int currYear, int currMonth) {
        this.currYear = currYear;
        this.currMonth = currMonth;
        this.nextMonthsYear = computeNextMonthsYear(currYear, currMonth);
        this.nextMonth = computeNextMonth(currMonth);
        this.prevMonthsYear = computePrevMonthsYear(currYear, currMonth);
        this.prevMonth = computePrevMonth(currMonth);

    }

    private int computeNextMonthsYear(int currYear, int currMonth) {
        return (currMonth == 12 ? currYear + 1 : currYear);
    }

    private int computeNextMonth(int currMonth) {
        return (currMonth == 12 ? 1 : currMonth+1);
    }

    private int computePrevMonthsYear(int currYear, int currMonth) {
        return (currMonth == 1 ? currYear - 1 : currYear);
    }

    private int computePrevMonth(int currMonth) {
        return (currMonth == 1 ? 12 : currMonth-1);
    }
}
