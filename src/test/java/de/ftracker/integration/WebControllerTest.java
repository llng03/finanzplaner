package de.ftracker.integration;

import de.ftracker.controller.WebController;
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
    @DisplayName("einnahmeAbschicken updatet Model")
    void test2() throws Exception {

        mockMvc.perform(post("/2025/6/einnahme")
                        .param("descr", "TestEinnahme")
                        .param("betrag", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/2025/6"));

        mockMvc.perform(get("/2025/6"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("einnahmen"))
                .andExpect(model().attribute("einnahmen", hasItem(
                        allOf(
                                hasProperty("descr", is("TestEinnahme")),
                                hasProperty("betrag", is(new BigDecimal("100.00")))
                        )
                )));

    }

    @Test
    @DisplayName("ausgabeAbschicken updatet das Model")
    void test3() throws Exception {

        mockMvc.perform(post("/2025/6/ausgabe")
                        .param("descr", "TestAusgabe")
                        .param("betrag", "50.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/2025/6"));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ausgaben"))
                .andExpect(model().attribute("ausgaben", hasItem(
                        allOf(
                                hasProperty("descr", is("TestAusgabe")),
                                hasProperty("betrag", is(new BigDecimal("50.00")))
                        )
                )));
    }



    /*@Test
    @DisplayName("festeAusgabeAbschicken updatet Model")
    void test4() throws Exception {
        List<FixedCost> gespeicherteFesteAusgaben = new ArrayList<>();

        doAnswer(invocation -> {
            gespeicherteFesteAusgaben.add(invocation.getArgument(0));
            return null;
        }).when(costManager).addToFixedExp(any());

        when(costManager.getTablesOf(any())).thenAnswer(invocation -> {
            CostTables t = new CostTables();
            t.setAusgaben(new ArrayList<>(gespeicherteFesteAusgaben));
            return t;
        });

        mockMvc.perform(post("/2025/6/festeAusgabe")
                .param("descr", "Miete")
                .param("betrag", "330"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/2025/6"));
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ausgaben"))
                .andExpect(model().attribute("ausgaben", hasItem(
                        allOf(
                                hasProperty("descr", is("Miete")),
                                hasProperty("betrag", is(new BigDecimal("330")))
                        )
                )));

                }*/


}
