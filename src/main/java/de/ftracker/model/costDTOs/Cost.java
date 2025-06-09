package de.ftracker.model.costDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Cost {

    @NotBlank(message = "Beschreibung darf nicht leer sein")
    private String desc;

    @Min(value = 0, message = "Betrag darf nicht negativ sein")
    private double betrag;

    public Cost() {
        // Default-Konstruktor für Spring Binding
    }

    public Cost(String desc, double betrag) {
        this.desc = desc;
        this.betrag = betrag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    @Override
    public String toString() {
        return "Cost[desc=" + desc + ", betrag=" + betrag + "]";
    }

    // equals() und hashCode() kannst du nur überschreiben, wenn nötig
}