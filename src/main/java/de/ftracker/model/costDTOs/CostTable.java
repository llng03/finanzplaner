package de.ftracker.model.costDTOs;

import java.util.ArrayList;
import java.util.List;

public class CostTable {
    private List<Cost> costs;

    public CostTable() {
        this.costs = new ArrayList<Cost>();
    }

    public void addCost(Cost cost) {
        this.costs.add(cost);
    }

    public List<Cost> getContent() {
        return costs;
    }

    public double sum() {
        return costs.stream()
                .mapToDouble(Cost::getBetrag)
                .sum();
    }
}
