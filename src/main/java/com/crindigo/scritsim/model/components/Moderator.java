package com.crindigo.scritsim.model.components;

import com.crindigo.scritsim.model.IModeratorStats;
import lombok.Getter;

public class Moderator extends ReactorComponent {

    @Getter
    private IModeratorStats moderatorStats;

    public Moderator(IModeratorStats moderatorStats, double thermalConductivity, double mass) {
        super(moderatorStats.getModerationFactor(), moderatorStats.getMaxTemperature(), thermalConductivity, mass,
                true);
        this.moderatorStats = moderatorStats;
    }

    public double getAbsorptionFactor(boolean controlsInserted, boolean isThermal) {
        return moderatorStats.getAbsorptionFactor();
    }
}
