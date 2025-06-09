package de.ftracker;

import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.CostTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CostTableTest {
    @Test
    @DisplayName("table increases size")
    void test1() {
        CostTable table = new CostTable();
        table.addCost(new Cost("Test", 10.00));
        assertEquals(1, table.getContent().size());
    }

    @Test
    @DisplayName("returns correct sum")
    void test2() {
        CostTable table = new CostTable();
        table.addCost(new Cost("Test", 10.32));
        table.addCost(new Cost("Test", 20.27));
        assertEquals(30.59, table.sum(), 0.001);
    }
}
