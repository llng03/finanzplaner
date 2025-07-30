package de.ftracker;


import de.ftracker.model.CostTables;
import de.ftracker.model.costDTOs.Cost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CostTablesTest {
    @Test
    @DisplayName("table increases size")
    void test1() {
        CostTables tables = new CostTables();
        tables.addCostToAusgaben(new Cost("Test", new BigDecimal("100")));
        assertEquals(1, tables.getAusgaben().size());
    }

    @Test
    @DisplayName("returns correct sum")
    void test2() {
        CostTables tables = new CostTables();
        tables.addCostToAusgaben(new Cost("Test", new BigDecimal("10.32")));
        tables.addCostToAusgaben(new Cost("Test", new BigDecimal("20.27")));
        assertThat(tables.sumAusgaben()).isEqualByComparingTo(new BigDecimal("30.59"));
    }
}