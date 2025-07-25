package de.ftracker.model.costDTOs;

import java.math.BigDecimal;

public class Ausgabe extends Cost {
    public Ausgabe(String desc, BigDecimal betrag) {
        this.setDesc(desc);
        this.setBetrag(betrag);
    }
}
