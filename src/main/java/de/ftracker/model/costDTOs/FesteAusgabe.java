package de.ftracker.model.costDTOs;

import static de.ftracker.model.costDTOs.Interval.MONTHLY;

public class FesteAusgabe extends Cost{
    private Interval frequency = MONTHLY;

    public void setFrequency(Interval frequency) {
        this.frequency = frequency;
    }

    public Interval getFrequency() {
        return frequency;
    }
}
