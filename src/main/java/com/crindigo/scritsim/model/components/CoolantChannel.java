package com.crindigo.scritsim.model.components;

import com.crindigo.scritsim.model.CoolantHandler;
import com.crindigo.scritsim.model.ICoolantStats;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CoolantChannel extends ReactorComponent {

    @Getter
    private final ICoolantStats coolant;
    @Setter
    @Getter
    private double weight;

    @Getter
    private final CoolantHandler inputHandler;
    @Getter
    private final CoolantHandler outputHandler;

    // Allows fission reactors to heat up less than a full liter of coolant.
    public double partialCoolant;

    public CoolantChannel(double maxTemperature, double thermalConductivity, ICoolantStats coolant, double mass,
                          CoolantHandler inputHandler, CoolantHandler outputHandler) {
        super(coolant.getModeratorFactor(), maxTemperature, thermalConductivity, mass,
                true);
        this.coolant = coolant;
        this.weight = 0;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public void addWeight(double weight) {
        this.weight += weight;
    }

    public double getAbsorptionFactor(boolean controlsInserted, boolean isThermal) {
        return isThermal ? coolant.getSlowAbsorptionFactor() : coolant.getFastAbsorptionFactor();
    }

    @Override
    public String toString() {
        return "CoolantChannel{" +
                "weight=" + weight +
                '}';
    }
}
