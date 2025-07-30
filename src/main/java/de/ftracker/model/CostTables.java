package de.ftracker.model;

import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.FixedCost;
import de.ftracker.model.pots.PotManager;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CostTables {

    private List<Cost> einnahmen = new ArrayList<>();
    private List<Cost> ausgaben = new ArrayList<>();
    private List<FixedCost> fixedEinnahmen = new ArrayList<>();
    private List<FixedCost> fixedAusgaben = new ArrayList<>();
    private YearMonth yearMonth;

    public CostTables() {
        yearMonth = YearMonth.now();
    }

    public CostTables(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public List<Cost> getEinnahmen() {
        return einnahmen;
    }

    public List<Cost> getAusgaben() {
        return ausgaben;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

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
        addCostToAusgaben(new Cost(name, betrag));
    }

    public void addToPots(PotManager potManger, BigDecimal amount) {
        addCostToAusgaben("auf Pots zu Verteilen", amount);
        potManger.addToUndistributed(amount);
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
