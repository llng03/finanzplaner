package de.ftracker.utils;

import java.math.BigDecimal;

public class MonthlySums {
    public final BigDecimal sumIn;
    public final BigDecimal sumOut;
    public final BigDecimal difference;

    public MonthlySums(BigDecimal sumIn, BigDecimal sumOut) {
        this.sumIn = sumIn;
        this.sumOut = sumOut;
        this.difference = sumIn.subtract(sumOut);
    }
}
