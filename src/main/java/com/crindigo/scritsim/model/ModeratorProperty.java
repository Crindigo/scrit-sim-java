package com.crindigo.scritsim.model;

import lombok.Builder;
import lombok.Getter;

@Builder
public class ModeratorProperty implements IModeratorStats {

    @Getter
    private int maxTemperature;
    @Getter
    private double moderationFactor;
    @Getter
    private double absorptionFactor;
}
