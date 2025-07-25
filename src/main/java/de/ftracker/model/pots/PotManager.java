package de.ftracker.model.pots;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*@Service
public class PotManager {
    private List<BudgetPot> pots = new ArrayList<>();
    private BigDecimal undistributedAmount = new BigDecimal(0);


    public List<BudgetPot> getPots() {
        return pots;
    }

    public BigDecimal getUndistributedAmount() {
        return undistributedAmount;
    }

    public void addPot(BudgetPot budgetPot) {
        pots.add(budgetPot);
    }

    public BudgetPot getPot(String name) {
        return pots.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Kein Pot mit Namen: " + name));

    }

    public void distribute(BigDecimal amount, String potName) {
        distribute(amount, getPot(potName));
    }

    public void distribute(BigDecimal amount, BudgetPot pot) {
        if(undistributedAmount.compareTo(amount) < 0) {
            throw new IllegalArgumentException("not enough undistributed amount");
        }
        undistributedAmount.subtract(amount);
        pot.addEntry(LocalDate.now(), amount);
    }

    public void addToUndistributed(BigDecimal amount) {
        undistributedAmount += amount;
    }
}*/
