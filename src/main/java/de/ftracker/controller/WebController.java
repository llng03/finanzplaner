package de.ftracker.controller;

import de.ftracker.model.CostManager;
import de.ftracker.model.CostTables;
import de.ftracker.model.costDTOs.*;
import de.ftracker.model.pots.PotManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Controller
public class WebController {
    private final CostManager costManager;
    private final PotManager potManager;

    @Autowired
    public WebController(CostManager costManager, PotManager potManager) {
        this.costManager = costManager;
        this.potManager = potManager;
    }

    @GetMapping("/")
    public String index(Model model) {
        return indexMonth(model, YearMonth.now().getYear(), YearMonth.now().getMonthValue());
    }

    @GetMapping("/{currYear}/{currMonth}")
    public String indexMonth(Model model, @PathVariable int currYear, @PathVariable int currMonth) {
        YearMonth thisYearMonth = YearMonth.of(currYear, currMonth);
        prepareModel(model, thisYearMonth);
        return "index";
    }

    @PostMapping("/{currYear}/{currMonth}/einnahme")
    public String einnahmeAbschicken(Model model, @ModelAttribute @Valid Cost einnahme, BindingResult bindingResult, @PathVariable int currYear, @PathVariable int currMonth) {
        if(bindingResult.hasErrors()){
            model.addAttribute("einnahme", einnahme);
            prepareModel(model, YearMonth.of(currYear, currMonth));
            return "index";
        }

        einnahme.setIncome(true);

        CostTables costTables = costManager.getTablesOf(YearMonth.of(currYear, currMonth));
        costTables.addCostToEinnahmen(einnahme);
        model.addAttribute("einnahmen", costTables.getEinnahmen());
        return "redirect:/" + currYear + "/" + currMonth;
    }

    @PostMapping("/{currYear}/{currMonth}/ausgabe")
    public String ausgabeAbschicken(Model model, @ModelAttribute @Valid Cost ausgabe, BindingResult bindingResult,  @PathVariable int currYear, @PathVariable int currMonth) {
        if(bindingResult.hasErrors()){
            model.addAttribute("ausgabe", ausgabe);
            prepareModel(model, YearMonth.of(currYear, currMonth));
            return "indexMonth";
        }

        ausgabe.setIncome(false);

        CostTables costTables = costManager.getTablesOf(YearMonth.of(currYear, currMonth));
        costTables.addCostToAusgaben(ausgabe);
        model.addAttribute("ausgaben", costTables.getAusgaben());
        return "redirect:/" + currYear + "/" + currMonth;
    }

    @PostMapping("/{currYear}/{currMonth}/festeEinnahme")
    public String festeEinnahmeAbschicken(Model model, @ModelAttribute @Valid FixedCost festeEinname, BindingResult bindingResult, @PathVariable int currYear, @PathVariable int currMonth) {
        if(bindingResult.hasErrors()){
            model.addAttribute("festeEinnahme", festeEinname);
            prepareModel(model, YearMonth.of(currYear, currMonth));
            return "index";
        }
        costManager.addToFesteEinnahmen(festeEinname);
        return "redirect:/" + currYear + "/" + currMonth;
    }

    @PostMapping("/{currYear}/{currMonth}/festeAusgabe")
    public String festeAusgabeAbschicken(Model model, @ModelAttribute @Valid FixedCost ausgabe, BindingResult bindingResult, @PathVariable int currYear, @PathVariable int currMonth) {
        if(bindingResult.hasErrors()){
            model.addAttribute("festeAusgabe", ausgabe);
            prepareModel(model, YearMonth.of(currYear, currMonth));
            return "index";
        }
        costManager.addToFesteAusgaben(ausgabe);
        return "redirect:/" + currYear + "/" + currMonth;

    }

    @PostMapping("/{currYear}/{currMonth}/toPots")
    public String addToPots(Model model, @RequestParam double amount, @PathVariable int currYear, @PathVariable int currMonth) {
        System.out.println("WebController potManager hash: " + System.identityHashCode(potManager));
        CostTables thisTables = costManager.getTablesOf(YearMonth.of(currYear, currMonth));
        thisTables.addToPots(potManager, new BigDecimal(amount));
        return "redirect:/" + currYear + "/" + currMonth;
    }

    private void prepareModel(Model model, YearMonth month) {
        int currMonth = month.getMonth().getValue();
        int currYear = month.getYear();

        model.addAttribute("currMonthString", month.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN));

        model.addAttribute("currMonth", currMonth);

        model.addAttribute("currYear", currYear);

        if(currMonth != 1) {
            model.addAttribute("prevMonth", currMonth - 1);
            model.addAttribute("prevMonthsYear", currYear);
        } else {
            model.addAttribute("prevMonth", 12);
            model.addAttribute("prevMonthsYear", currYear - 1);
        }

        if(currMonth != 12) {
            model.addAttribute("nextMonth", currMonth + 1);
            model.addAttribute("nextMonthsYear", currYear);
        } else {
            model.addAttribute("nextMonth", 1);
            model.addAttribute("nextMonthsYear", currYear + 1);
        }


        // Fallbacks für leere Felder bei neuem Aufruf oder Fehler
        if (!model.containsAttribute("einnahme"))
            model.addAttribute("einnahme", new Cost());
        if (!model.containsAttribute("ausgabe"))
            model.addAttribute("ausgabe", new Cost());
        if (!model.containsAttribute("festeEinnahme"))
            model.addAttribute("festeEinnahme", new FixedCost());
        if(!model.containsAttribute("festeAusgabe"))
            model.addAttribute("festeAusgabe", new FixedCost());

        CostTables costTables = costManager.getTablesOf(month);

        List<Cost> einnahmen = costManager.getMonthsEinnahmen(month);
        List<Cost> ausgaben = costManager.getMonthsAusgaben(month);

        einnahmen.addAll(costTables.getEinnahmen());
        ausgaben.addAll(costTables.getAusgaben());

        model.addAttribute("einnahmen", einnahmen);
        model.addAttribute("ausgaben", ausgaben);

        BigDecimal sumIn = costTables.sumEinnahmen();
        BigDecimal sumOut = costTables.sumAusgaben();

        model.addAttribute("summeIn", sumIn);
        model.addAttribute("summeOut", sumOut);
        model.addAttribute("differenz", sumIn.subtract(sumOut));
    }
}