package de.ftracker.model;

import de.ftracker.model.costDTOs.Cost;
import de.ftracker.services.pots.PotManager;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CostTables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;
    private int year;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Cost> einnahmen;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Cost> ausgaben;

    public CostTables() {
        this.month = YearMonth.now().getMonthValue();
        this.year = YearMonth.now().getYear();
        this.einnahmen = new ArrayList<>();
        this.ausgaben = new ArrayList<>();
    }

    public CostTables(YearMonth yearMonth) {
        this.month = yearMonth.getMonthValue();
        this.year = yearMonth.getYear();
        this.einnahmen = new ArrayList<>();
        this.ausgaben = new ArrayList<>();
    }

    public List<Cost> getEinnahmen() {
        return einnahmen;
    }

    public List<Cost> getAusgaben() {
        return ausgaben;
    }

    public YearMonth getYearMonth() {
        return YearMonth.of(this.year, this.month);
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.month = yearMonth.getMonthValue();
        this.year = yearMonth.getYear();
    }

    public void setEinnahmen(List<Cost> einnahmen) {this.einnahmen = einnahmen;}

    public void setAusgaben(List<Cost> ausgaben) {
        this.ausgaben = ausgaben;
    }

    public void addCostToEinnahmen(Cost cost){
        this.einnahmen.add(cost);
    }

    public void addCostToAusgaben(Cost cost){
        this.ausgaben.add(cost);
    }

    public void addCostToAusgaben(String name, BigDecimal betrag) {
        addCostToAusgaben(new Cost(name, betrag, false));
    }

    public void addToPots(PotManager potManger, BigDecimal amount) {
        addCostToAusgaben("auf Pots zu Verteilen", amount);
        potManger.addToUndistributed(amount);
    }

    public void addToPot(PotManager potManager, BigDecimal amount, String potName) {
        addCostToAusgaben("auf Pot " + potName + " verteilen", amount);
        potManager.addToUndistributed(amount);
        potManager.distribute(amount, potName);
    }

    public BigDecimal sumEinnahmen() {
        return sum(einnahmen);
    }

    public BigDecimal sumAusgaben() {
        return sum(ausgaben);
    }

    private static BigDecimal sum(List<Cost> costs) {
        return costs.stream()
                .map(e -> e.getBetrag())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
