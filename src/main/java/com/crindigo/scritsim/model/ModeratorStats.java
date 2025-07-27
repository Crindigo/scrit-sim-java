package com.crindigo.scritsim.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ModeratorStats implements IModeratorStats {
    private int maxTemperature;
    private double moderationFactor;
    private double absorptionFactor;
}
