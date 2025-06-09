package de.ftracker;

import de.ftracker.model.pots.BudgetPot;
import de.ftracker.model.pots.PotManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        manager.addToUndistributed(500);

        manager.distribute(150, "ausflug");

        assertEquals(350, manager.getUndistributedAmount(), 0.01);
        assertEquals(150, pot.sum(), 0.01);


    }

    @Test
    @DisplayName("distribute more than available throws Exception")
    void test3() {
        PotManager manager = new PotManager();
        manager.addToUndistributed(50);
        manager.addPot(new BudgetPot("new"));
        assertThrows(IllegalArgumentException.class, () -> manager.distribute(100, "new"));
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
        manager.addToUndistributed(500);
        assertEquals(500, manager.getUndistributedAmount());
    }
    @Test
    @DisplayName("distributeToPot decreasesUnverteilt")
    void test6() {
        PotManager manager = new PotManager();
        BudgetPot pot = new BudgetPot("technik");
        manager.addPot(pot);
        manager.addToUndistributed(300);

        manager.distribute(100, "technik");
        assertEquals(200, manager.getUndistributedAmount());
        assertEquals(100, pot.sum());
    }

}