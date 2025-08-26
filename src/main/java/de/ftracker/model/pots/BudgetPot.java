package de.ftracker.model.pots;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pot_type")
public class BudgetPot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PotEntry> entries = new ArrayList<>();

    public BudgetPot(){}

    public BudgetPot(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public BigDecimal sum() {
        return entries.stream()
                .map(e -> e.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getName() {
        return name;
    }

    public List<PotEntry> getEntries() {
        return entries;
    }

    public void addEntry(LocalDate date, BigDecimal amount) {
        entries.add(new PotEntry(date, amount));
    }

    public void pay(LocalDate date, BigDecimal amount) {
        addEntry(LocalDate.now(), amount.negate());
    }
}
