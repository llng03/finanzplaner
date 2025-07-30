package de.ftracker.controller;

import de.ftracker.model.pots.BudgetPot;
import de.ftracker.model.pots.PotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class PotController {
    private final PotManager potManager;

    @Autowired
    public PotController(PotManager potManager) {
        this.potManager = potManager;
    }

    @GetMapping("/pots")
    public String pots(Model model) {
        prepareModel(model);
        return "pots";
    }

    @PostMapping("/pots/new")
    public String createNewPot(Model model, @RequestParam("name") String newPotName) {
        System.out.println("PotController potManager hash: " + System.identityHashCode(potManager));
        potManager.addPot(new BudgetPot(newPotName));
        model.addAttribute("pots", potManager.getPots());
        return "redirect:/pots";
    }

    @PostMapping("/pots/distribute")
    public String distribute(Model model, @RequestParam("potName") String potName,
                             @RequestParam("amount") double amount) {
        try {
            potManager.distribute(BigDecimal.valueOf(amount), potName);
        } catch(IllegalArgumentException e) {
            model.addAttribute("showDistributeModal", true);
            model.addAttribute("error", "Es wurde eine höhere Summe verteilt, als vorhanden ist :c");
            model.addAttribute("pots", potManager.getPots());
            return "pots";
        }
        model.addAttribute("pots", potManager.getPots());
        return "redirect:/pots";
    }

    private void prepareModel(Model model) {
        model.addAttribute("pots", potManager.getPots());
        model.addAttribute("undistributed", potManager.getUndistributed());

    }


}
