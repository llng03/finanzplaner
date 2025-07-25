package de.ftracker;

import de.ftracker.model.CostManager;
import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.FixedCost;
import de.ftracker.model.costDTOs.Interval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CostManagerTest {

    @Test
    @DisplayName("getApplicableFixedCost funktioniert für aktuellen Monat")
    void test1() {
        CostManager costManager = new CostManager();
        costManager.addToFesteEinnahmen(new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), null));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.JULY));

        assertThat(applFixedCosts).anyMatch(fc -> fc.getDesc().equals("fixedEinnahme"));

    }
    @Test
    @DisplayName("gAFC funktioniert für nächsten Monat")
    void test2() {
        CostManager costManager = new CostManager();
        costManager.addToFesteEinnahmen(new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), null));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.AUGUST));

        assertThat(applFixedCosts).anyMatch(fc -> fc.getDesc().equals("fixedEinnahme"));

    }

    @Test
    @DisplayName("gAFC funktioniert für letzen Monat nicht")
    void test3() {
        CostManager costManager = new CostManager();
        costManager.addToFesteEinnahmen(new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), null));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.JUNE));

        assertThat(applFixedCosts).noneMatch(fc -> fc.getDesc().equals("fixedEinnahme"));

    }
    @Test
    @DisplayName("gAFC funktioniert für nächsten Monat nicht falls unerwünscht")
    void test4() {
        CostManager costManager = new CostManager();
        costManager.addToFesteEinnahmen(new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), YearMonth.of(2025, Month.AUGUST)));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.SEPTEMBER));

        assertThat(applFixedCosts).noneMatch(fc -> fc.getDesc().equals("fixedEinnahme"));

    }

    @Test
    @DisplayName("gAFC funktioniert für letzten erwünschten Monat noch")
    void test5() {
        CostManager costManager = new CostManager();
        costManager.addToFesteEinnahmen(new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), YearMonth.of(2025, Month.AUGUST)));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.AUGUST));

        assertThat(applFixedCosts).anyMatch(fc -> fc.getDesc().equals("fixedEinnahme"));

    }
}
