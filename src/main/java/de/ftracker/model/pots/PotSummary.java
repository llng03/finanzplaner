package de.ftracker.model.pots;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class PotSummary {
    @Id
    private Long id = 1L; //only one entry

    private BigDecimal undistributed = BigDecimal.ZERO;

    public PotSummary() {}

    public BigDecimal getUndistributed() {
        return undistributed;
    }

    public void setUndistributed(BigDecimal undistributed) {
        this.undistributed = undistributed;
    }

    public void addToUndistributed(BigDecimal amount) {
        undistributed = undistributed.add(amount);
    }
}
