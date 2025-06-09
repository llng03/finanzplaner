package de.ftracker.controller;

import de.ftracker.model.pots.PotManager;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class PotControllerTestConfig {
    @Bean
    public PotManager potManager() {
        return Mockito.mock(PotManager.class);
    }
}
