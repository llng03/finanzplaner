package de.ftracker.controller;

import de.ftracker.model.pots.BudgetPot;
import de.ftracker.model.pots.PotManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PotController {
    private PotManager manager = new PotManager();

    @GetMapping("/pots")
    public String pots(Model model) {
        prepareModel(model);
        return "pots";
    }

    @PostMapping("/pots/new")
    public String createNewPot(Model model, @RequestParam("name") String newPotName) {
        manager.addPot(new BudgetPot(newPotName));
        model.addAttribute("pots", manager.getPots());
        return "redirect:/pots";
    }

    @PostMapping("/pots/distribute")
    public String distribute(Model model, @RequestParam("potName") String potName,
                             @RequestParam("amount") double amount) {
        try {
            manager.distribute(amount, potName);
        } catch(IllegalArgumentException e) {
            model.addAttribute("showDistributeModal", true);
            model.addAttribute("error", "Es wurde eine höhere Summe verteilt, als vorhanden ist :c");
            model.addAttribute("pots", manager.getPots());
            return "pots";
        }
        model.addAttribute("pots", manager.getPots());
        return "redirect:/pots";
    }

    private void prepareModel(Model model) {
        if(!model.containsAttribute("pots")) {
            model.addAttribute("pots", manager.getPots());
        }
        if(!model.containsAttribute("undistributed")) {
            //nur zu testing-zwecken!!:
            //manager.addToUndistributed(500);
            //
            model.addAttribute("undistributed", manager.getUndistributedAmount());
        }
    }
}
