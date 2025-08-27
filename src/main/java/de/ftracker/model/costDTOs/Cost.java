package de.ftracker.model.costDTOs;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="cost_type")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Beschreibung darf nicht leer sein")
    private String descr;

    @Min(value = 0, message = "Betrag darf nicht negativ sein")
    private BigDecimal betrag;

    private boolean isIncome;

    public Cost() {
        // Default-Konstruktor für Spring Binding
    }

    public Cost(String descr, BigDecimal betrag, boolean isIncome) {
        this.descr = descr;
        this.betrag = betrag;
    }

    public void setIsIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    @Override
    public String toString() {
        return "Cost[descr=" + descr + ", betrag=" + betrag + "]";
    }

    public boolean getIsIncome(){
        return isIncome;
    }

    // equals() und hashCode() kannst du nur überschreiben, wenn nötig
}