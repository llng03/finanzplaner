package de.ftracker.integration;

import de.ftracker.controller.WebController;
import de.ftracker.model.costDTOs.Cost;
import de.ftracker.model.costDTOs.Interval;
import de.ftracker.services.CostManager;
import de.ftracker.model.CostTables;
import de.ftracker.model.costDTOs.FixedCost;
import de.ftracker.services.pots.PotManager;
import de.ftracker.utils.MonthlySums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //hier macht der mock Probleme, weil der Aufruf der Methode getMonthsEinnahmen null zurückgibt weil costManager gemockt...
    @MockitoBean
    private CostManager costManager;

    @MockitoBean
    private PotManager potManager;

    @BeforeEach
    void setupStubs() {
        // Erzeuge ein leeres CostTables-Objekt
        CostTables fakeTables = new CostTables(YearMonth.of(2025, Month.JUNE));

        // Stub für alle Tests
        when(costManager.getTablesOf(any())).thenReturn(fakeTables);
        when(costManager.getMonthsEinnahmen(any())).thenReturn(Collections.emptyList());
        when(costManager.getMonthsAusgaben(any())).thenReturn(Collections.emptyList());
        when(costManager.getFixedIncome()).thenReturn(Collections.emptyList());
        when(costManager.getFixedExp()).thenReturn(Collections.emptyList());

        when(costManager.calculateThisMonthsSums(YearMonth.of(2025, Month.JUNE))).thenReturn(new MonthlySums(BigDecimal.ZERO, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("index lädt mit initalen attributen")
    void test1() throws Exception {
        mockMvc.perform(get("/2025/6"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("einnahme", "ausgabe", "einnahmen", "ausgaben", "summeIn", "summeOut", "differenz"))
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("einnahmeAbschicken funktioniert falls valid")
    void test2() throws Exception {
        mockMvc.perform(post("/2025/6/einnahme")
                        .param("descr", "TestEinnahme")
                        .param("betrag", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/2025/6"));

    }

    @Test
    @DisplayName("einnahmeAbschicken funktioniert nicht falls nicht valid")
    void test3() throws Exception {
        mockMvc.perform(post("/2025/6/einnahme")
                        .param("descr", "")
                        .param("betrag", "-100.00"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("cost", "descr", "betrag"))
                .andExpect(model().errorCount(2));
    }

    @Test
    @DisplayName("neue Eingabe wird im Model gespeichert")
    void test4() throws Exception {
        Cost cost = new Cost("TestEinnahme", new BigDecimal("1000"), true);
        when(costManager.getAllMonthsIncome(YearMonth.of(2025, Month.JUNE))).thenReturn(Collections.singletonList(cost));

        mockMvc.perform(get("/2025/6"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("einnahmen"))
                .andExpect(model().attribute("einnahmen", hasItem(
                        allOf(
                                hasProperty("descr", is("TestEinnahme")),
                                hasProperty("betrag", is(new BigDecimal("1000")))
                        )
                )));
    }

    @Test
    @DisplayName("ausgabeAbschicken updatet das Model")
    void test5() throws Exception {

        mockMvc.perform(post("/2025/6/ausgabe")
                        .param("descr", "TestAusgabe")
                        .param("betrag", "50.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/2025/6"));
    }

    @Test
    @DisplayName("neue Ausgabe wird im Model gespeichert ")
    void test6() throws Exception {
        Cost exp = new Cost("TestAusgabe", new BigDecimal("50.00"), false);
        when(costManager.getAllMonthsExp(YearMonth.of(2025, Month.JUNE))).thenReturn(Collections.singletonList(exp));
        mockMvc.perform(get("/2025/6"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ausgaben"))
                .andExpect(model().attribute("ausgaben", hasItem(
                        allOf(
                                hasProperty("descr", is("TestAusgabe")),
                                hasProperty("betrag", is(new BigDecimal("50.00")))
                        )
                )));
    }


    @Test
    @DisplayName("festeAusgabeAbschicken funktioniert falls valid")
    void test7() throws Exception {

        mockMvc.perform(post("/2025/6/festeAusgabe")
                        .param("descr", "Miete")
                        .param("betrag", "330"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/2025/6"));

    }

    @Test
    @DisplayName("neue festeAusgabe landet im Model")
    void test8() throws Exception {
        FixedCost fixedCost = new FixedCost("Miete", new BigDecimal("330"), false, Interval.MONTHLY, YearMonth.of(2025, 6), null);
        when(costManager.getFixedExp()).thenReturn(Collections.singletonList(fixedCost));
        mockMvc.perform(get("/2025/6"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("festeAusgaben"))
                .andExpect(model().attribute("festeAusgaben", hasItem(
                        allOf(
                                hasProperty("descr", is("Miete")),
                                hasProperty("betrag", is(new BigDecimal("330")))
                        )
                )));

    }

}
