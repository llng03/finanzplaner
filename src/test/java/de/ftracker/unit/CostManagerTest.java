package de.ftracker.unit;

import de.ftracker.services.CostManager;
import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.FixedCost;
import de.ftracker.model.costDTOs.Interval;
import de.ftracker.services.CostTablesRepository;
import de.ftracker.services.FixedExpRepository;
import de.ftracker.services.FixedIncomeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static java.time.Month.AUGUST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CostManagerTest {
    @Mock
    private CostTablesRepository costTablesRepository;

    @Mock
    private FixedExpRepository fixedExpRepository;

    @Mock
    private FixedIncomeRepository fixedIncomeRepository;

    @InjectMocks
    private CostManager costManager;

    // - - getApplicableFixedCost - -  //
    @Test
    @DisplayName("getApplicableFixedCost funktioniert für aktuellen Monat")
    void test1() {
        FixedCost fixedCost = new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), null);
        when(fixedIncomeRepository.findAll()).thenReturn(Arrays.asList(fixedCost));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.JULY));

        assertThat(applFixedCosts).anyMatch(fc -> fc.getDescr().equals("fixedEinnahme"));

    }
    @Test
    @DisplayName("gAFC funktioniert für nächsten Monat")
    void test2() {
        FixedCost fixedCost = new FixedCost("fixedEinnahme",
            new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), null);

        when(fixedIncomeRepository.findAll()).thenReturn(Arrays.asList(fixedCost));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, AUGUST));

        assertThat(applFixedCosts).anyMatch(fc -> fc.getDescr().equals("fixedEinnahme"));

    }

    @Test
    @DisplayName("gAFC funktioniert für letzen Monat nicht")
    void test3() {
        FixedCost fixedCost = new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), null);
        when(fixedIncomeRepository.findAll()).thenReturn(Arrays.asList(fixedCost));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.JUNE));

        assertThat(applFixedCosts).noneMatch(fc -> fc.getDescr().equals("fixedEinnahme"));

    }
    @Test
    @DisplayName("gAFC funktioniert für nächsten Monat nicht falls unerwünscht")
    void test4() {
        FixedCost fixedCost = new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), YearMonth.of(2025, AUGUST));
        when(fixedIncomeRepository.findAll()).thenReturn(Arrays.asList(fixedCost));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, Month.SEPTEMBER));

        assertThat(applFixedCosts).noneMatch(fc -> fc.getDescr().equals("fixedEinnahme"));

    }

    @Test
    @DisplayName("gAFC funktioniert für letzten erwünschten Monat noch")
    void test5() {
        FixedCost fixedCost = new FixedCost("fixedEinnahme",
                new BigDecimal("50"), Interval.MONTHLY, YearMonth.of(2025, Month.JULY), YearMonth.of(2025, AUGUST));
        when(fixedIncomeRepository.findAll()).thenReturn(Arrays.asList(fixedCost));

        List<Cost> applFixedCosts = costManager.getApplicableFixedCosts(YearMonth.of(2025, AUGUST));

        assertThat(applFixedCosts).anyMatch(fc -> fc.getDescr().equals("fixedEinnahme"));

    }

    // - - getMonthlyCost - - //
    @Test
    @DisplayName("getMonthlyCost correct for QUATERLY")
    void test6() {
        FixedCost quaterInc = new FixedCost("quaterInc",new BigDecimal("75"),
                Interval.QUARTERLY, YearMonth.of(2025, AUGUST), null);
        assertThat(costManager.getMonthlyCost(quaterInc)).isEqualByComparingTo(new BigDecimal("25"));
    }

    @Test
    @DisplayName("getMonthlyCost correct for SEMI_ANNUAL")
    void test7() {
        FixedCost quaterInc = new FixedCost("quaterInc",new BigDecimal("60"),
                Interval.SEMI_ANNUAL, YearMonth.of(2025, AUGUST), null);
        assertThat(costManager.getMonthlyCost(quaterInc)).isEqualByComparingTo(new BigDecimal("10"));
    }

    @Test
    @DisplayName("getMonthlyCost correct for ANNUAL")
    void test8() {
        FixedCost quaterInc = new FixedCost("quaterInc",new BigDecimal("120"),
                Interval.ANNUAL, YearMonth.of(2025, AUGUST), null);
        assertThat(costManager.getMonthlyCost(quaterInc)).isEqualByComparingTo(new BigDecimal("10"));
    }
}
