/*package de.ftracker;

import de.ftracker.model.pots.BudgetPot;
import de.ftracker.model.pots.PotManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PotManagerTest {
    @Test
    @DisplayName("addPot funktioniert")
    void test1() {
        PotManager potManager = new PotManager();
        BudgetPot pot = new BudgetPot("urlaub");
        potManager.addPot(pot);
        assertThat(potManager.getPot("urlaub")).isEqualTo(pot);
    }

    @Test
    @DisplayName("distributed correctly")
    void test2() {
        PotManager manager = new PotManager();
        BudgetPot pot = new BudgetPot("ausflug");
        manager.addPot(pot);
        manager.addToUndistributed(new BigDecimal("500"));

        manager.distribute(new BigDecimal("150"), "ausflug");

        assertThat(new BigDecimal("350")).isEqualByComparingTo(manager.getUndistributedAmount());
        assertThat(new BigDecimal("150")).isEqualByComparingTo(pot.sum());


    }

    @Test
    @DisplayName("distribute more than available throws Exception")
    void test3() {
        PotManager manager = new PotManager();
        manager.addToUndistributed(new BigDecimal("50"));
        manager.addPot(new BudgetPot("new"));
        assertThrows(IllegalArgumentException.class, () -> manager.distribute(new BigDecimal("100"), "new"));
    }


    @Test
    @DisplayName("new pot has zero sum")
    void test4() {
        PotManager manager = new PotManager();
        manager.addPot(new BudgetPot("urlaub"));
        assertThat(manager.getPot("urlaub").sum()).isEqualTo(0);
    }

    @Test
    @DisplayName("addToUnverteilt increasesValue")
    void test5() {
        PotManager manager = new PotManager();
        manager.addToUndistributed(new BigDecimal("500"));
        assertEquals(500, manager.getUndistributedAmount());
    }
    @Test
    @DisplayName("distributeToPot decreasesUnverteilt")
    void test6() {
        PotManager manager = new PotManager();
        BudgetPot pot = new BudgetPot("technik");
        manager.addPot(pot);
        manager.addToUndistributed(new BigDecimal("300"));

        manager.distribute(new BigDecimal("100"), "technik");
        assertEquals(200, manager.getUndistributedAmount());
        assertEquals(100, pot.sum());
    }

}*/