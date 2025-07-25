package de.ftracker.model.costDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class Cost {

    @NotBlank(message = "Beschreibung darf nicht leer sein")
    private String desc;

    @Min(value = 0, message = "Betrag darf nicht negativ sein")
    private BigDecimal betrag;

    private boolean isIncome;

    public Cost() {
        // Default-Konstruktor für Spring Binding
    }

    public Cost(String desc, BigDecimal betrag) {
        this.desc = desc;
        this.betrag = betrag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public void setIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    @Override
    public String toString() {
        return "Cost[desc=" + desc + ", betrag=" + betrag + "]";
    }

    // equals() und hashCode() kannst du nur überschreiben, wenn nötig
}