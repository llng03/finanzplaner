package de.ftracker.model.costDTOs;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static de.ftracker.model.costDTOs.Interval.MONTHLY;

public class FixedCost extends Cost{
    private Interval frequency = MONTHLY;

    private YearMonth start;

    private Optional<YearMonth> end; //empty: no current ending

    public FixedCost() {
        super("", BigDecimal.ZERO);
        this.frequency = Interval.MONTHLY; // oder null
        this.start = YearMonth.now();
        this.end = Optional.empty(); // oder null
    }

    public FixedCost(String name, BigDecimal betrag, Interval frequency, YearMonth start, YearMonth end) {
        super(name, betrag);
        this.frequency = frequency;
        this.start = start;
        this.end = Optional.ofNullable(end);
    }

    public void setFrequency(Interval frequency) {
        this.frequency = frequency;
    }

    public void setStart(YearMonth start) {
        this.start = start;
    }

    public void setEndValue(YearMonth end) {
        this.end = Optional.ofNullable(end);
    }


    public Interval getFrequency() {
        return frequency;
    }

    public YearMonth getStart() {
        return start;
    }

    public Optional<YearMonth> getEnd() {
        return end;
    }

    public YearMonth getEndValue() {
        return end.orElse(null);
    }




}
