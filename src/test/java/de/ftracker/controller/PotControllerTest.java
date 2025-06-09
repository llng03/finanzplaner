package de.ftracker.controller;

import de.ftracker.model.pots.BudgetPot;
import de.ftracker.model.pots.PotManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(PotControllerTestConfig.class)
public class PotControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PotManager potManager;

    @Test
    @DisplayName("/pots wird erreicht")
    void test1() throws Exception{
        mvc.perform(get("/pots"))
                .andExpect(status().isOk())
                .andExpect(view().name("pots"))
                .andExpect(model().attributeExists("pots"))
                .andExpect(model().attributeExists("undistributed"));

    }

    @Test
    @DisplayName("createNewPotRedirectsToPots")
    void test2() throws Exception{
        mvc.perform(post("pots/new").param("name", "chess"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pots"));
        verify(potManager).addPot(any(BudgetPot.class));
    }

    @Test
    @DisplayName("distributeToPots updates correctly")
    void test3() throws Exception{
        mvc.perform(post("/pots/distribute")
                .param("potName", "technik")
                .param("amount", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pots"));
        verify(potManager).distribute(100, "technik");
    }
}
