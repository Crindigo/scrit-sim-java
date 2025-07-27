package com.crindigo.scritsim.model;

import lombok.Getter;
import lombok.Setter;

public class CoolantProperty implements ICoolantStats {

    @Setter
    @Getter
    private Fluid hotHPCoolant;
    @Setter
    @Getter
    private double moderatorFactor;
    /**
     * Roughly the heat transfer coefficient Do not put too much thought into this
     */
    @Setter
    @Getter
    private double coolingFactor;
    // in kelvin at standard conditions
    @Setter
    @Getter
    private double boilingPoint;
    // neutron absorption rate
    // in J/L
    @Setter
    @Getter
    private double heatOfVaporization;
    // in J/(kg*K)
    @Setter
    @Getter
    private double specificHeatCapacity;
    private boolean accumulatesHydrogen = false;

    @Getter
    private double slowAbsorptionFactor;

    @Getter
    private double fastAbsorptionFactor;

    @Getter
    private final double mass;

    public CoolantProperty(int materialMass, Fluid hotHPCoolant, double moderatorFactor,
                           double coolingFactor,
                           double boilingPoint, double heatOfVaporization,
                           double specificHeatCapacity) {
        this.hotHPCoolant = hotHPCoolant;
        this.moderatorFactor = moderatorFactor;
        this.coolingFactor = coolingFactor;
        this.boilingPoint = boilingPoint;
        this.heatOfVaporization = heatOfVaporization;
        this.specificHeatCapacity = specificHeatCapacity;
        this.mass = materialMass;
    }

    public boolean accumulatesHydrogen() {
        return accumulatesHydrogen;
    }

    public CoolantProperty setAccumulatesHydrogen(boolean accumulatesHydrogen) {
        this.accumulatesHydrogen = accumulatesHydrogen;
        return this;
    }

    public Fluid getHotCoolant() {
        return hotHPCoolant;
    }

    public CoolantProperty setFastAbsorptionFactor(double fastAbsorptionFactor) {
        this.fastAbsorptionFactor = fastAbsorptionFactor;
        return this;
    }

    public CoolantProperty setSlowAbsorptionFactor(double slowAbsorptionFactor) {
        this.slowAbsorptionFactor = slowAbsorptionFactor;
        return this;
    }
}
