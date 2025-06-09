package de.ftracker.controller;

import de.ftracker.model.pots.PotManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BudgetController {
    private PotManager manager = new PotManager();

    @GetMapping("/pots")
    public String pots(Model model) {
        if(!model.containsAttribute("pots")) {
            model.addAttribute("pots", manager.getPots());
        }
        if(!model.containsAttribute("undistributed")) {
            model.addAttribute("undistributed", manager.getUndistributedAmount());
        }
        return "pots";
    }
}
