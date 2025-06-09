package de.ftracker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class WebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("index loads with initial attributes")
    void test1() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("einnahme", "ausgabe", "einnahmen", "ausgaben", "summeIn", "summeOut", "differenz"))
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("einnahmeAbschicken updatet Model")
    void test2() throws Exception {
        mockMvc.perform(post("/einnahme")
                        .param("desc", "TestEinnahme")
                        .param("betrag", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("einnahmen"))
                .andExpect(model().attribute("einnahmen", hasItem(
                        allOf(
                                hasProperty("desc", is("TestEinnahme")),
                                hasProperty("betrag", is(100.00))
                        )
                )));

    }

    @Test
    @DisplayName("festeAusgabeAbschicken updatet Model")
    void test3() throws Exception {
        mockMvc.perform(post("/festeAusgabe")
                .param("desc", "Miete")
                .param("betrag", "330"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ausgaben"))
                .andExpect(model().attribute("ausgaben", hasItem(
                        allOf(
                                hasProperty("desc", is("Miete")),
                                hasProperty("betrag", is(330.0))
                        )
                )));

    }
}
