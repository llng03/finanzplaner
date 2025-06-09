package de.ftracker.controller;

import de.ftracker.model.costDTOs.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
public class WebController {
    private final CostTable ausgaben;
    private final CostTable einnahmen;
    private final CostTable monEinnahmen;

    public WebController() {
        this.ausgaben = new CostTable();
        this.einnahmen = new CostTable();
        this.monEinnahmen = new CostTable();
    }


    @GetMapping("/")
    public String index(Model model) {
        prepareModel(model);
        return "index";
    }

    @PostMapping("/einnahme")
    public String einnahmeAbschicken(Model model, @ModelAttribute @Valid Einnahme einnahme, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            model.addAttribute("einnahme", einnahme);
            prepareModel(model);
            return "index";
        }
        einnahmen.addCost(einnahme);
        model.addAttribute("einnahmen", einnahmen.getContent());
        return "redirect:/";
    }

    @PostMapping("/ausgabe")
    public String ausgabeAbschicken(Model model, @ModelAttribute @Valid Ausgabe ausgabe, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            model.addAttribute("ausgabe", ausgabe);
            prepareModel(model);
            return "index";
        }
        ausgaben.addCost(ausgabe);
        model.addAttribute("ausgaben", ausgaben.getContent());
        return "redirect:/";
    }

    @PostMapping("/festeEinnahme")
    public String festeEinnahmeAbschicken(Model model, @ModelAttribute @Valid FesteEinnahme festeEinname, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            model.addAttribute("festeEinnahme", festeEinname);
            prepareModel(model);
            return "index";
        }
        monEinnahmen.addCost(festeEinname);
        model.addAttribute("monEinnahmen", monEinnahmen.getContent());
        return "redirect:/";
    }

    private void prepareModel(Model model) {
        if(!model.containsAttribute("currMonth")) {
            model.addAttribute("currMonth", LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN));
        }
        if(!model.containsAttribute("currYear")) {
            model.addAttribute("currYear", LocalDate.now().getYear());
        }
        // Fallbacks für leere Felder bei neuem Aufruf oder Fehler
        if (!model.containsAttribute("einnahme"))
            model.addAttribute("einnahme", new Einnahme());
        if (!model.containsAttribute("ausgabe"))
            model.addAttribute("ausgabe", new Ausgabe());
        if (!model.containsAttribute("festeEinnahme"))
            model.addAttribute("festeEinnahme", new FesteEinnahme());

        CostTable gesamteEinnahmen = new CostTable();
        for (Cost m : monEinnahmen.getContent()) {
            gesamteEinnahmen.addCost(m);
        }
        for (Cost e : einnahmen.getContent()) {
            gesamteEinnahmen.addCost(e);
        }

        model.addAttribute("einnahmen", gesamteEinnahmen.getContent());
        model.addAttribute("ausgaben", ausgaben.getContent());

        double sumIn = gesamteEinnahmen.sum();
        double sumOut = ausgaben.sum();

        model.addAttribute("summeIn", sumIn);
        model.addAttribute("summeOut", sumOut);
        model.addAttribute("differenz", sumIn - sumOut);
    }
}
