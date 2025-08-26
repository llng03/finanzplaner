package de.ftracker.model.costDTOs;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static de.ftracker.model.costDTOs.Interval.MONTHLY;

@Getter
@Setter
public class FixedCostForm {
    private String descr;

    private BigDecimal betrag;

    private Interval frequency = MONTHLY;

    private YearMonth start;

    private YearMonth end = null;

    public FixedCostForm() {
        this.descr = "";
        this.betrag = BigDecimal.ZERO;
        this.frequency = Interval.MONTHLY; // oder null
        this.start = YearMonth.now();
        this.end = null;
    }

    public FixedCostForm(String name, BigDecimal betrag, Interval frequency, YearMonth start, YearMonth end) {
        this.descr = name;
        this.betrag = betrag;
        this.frequency = frequency;
        this.start = start;
        this.end = end;
    }

    public Optional<YearMonth> getEnd(){
        return Optional.ofNullable(end);
    }
}
