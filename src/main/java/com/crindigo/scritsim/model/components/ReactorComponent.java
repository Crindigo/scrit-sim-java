package com.crindigo.scritsim.model.components;

import com.crindigo.scritsim.model.FissionReactor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReactorComponent {

    private final double moderationFactor;
    protected double maxTemperature;
    private final double thermalConductivity;
    private final double mass;

    private int x;
    private int y;

    private final boolean isValid;

    // The index of the reactor component, which is -1 if unset
    @Setter
    private int index = -1;

    public ReactorComponent(double moderationFactor, double maxTemperature, double thermalConductivity, double mass,
                            boolean isValid) {
        this.moderationFactor = moderationFactor;
        this.maxTemperature = maxTemperature;
        this.thermalConductivity = thermalConductivity;
        this.mass = mass;
        this.isValid = isValid;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getAbsorptionFactor(boolean controlsInserted, boolean isThermal) {
        return 0;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean samePositionAs(ReactorComponent component) {
        return this.getX() == component.getX() && this.getY() == component.getY();
    }

    public double getDistance(ReactorComponent component) {
        return Math.sqrt(Math.pow(this.getX() - component.getX(), 2) +
                Math.pow(this.getY() - component.getY(), 2));
    }

    public List<String> info(FissionReactor reactor) {
        List<String> info = new ArrayList<>();
        info.add("Moderation Factor: " + moderationFactor);
        info.add("Max Temp: " + maxTemperature);
        info.add("Thermal Conductivity: " + thermalConductivity);
        info.add("Mass: " + mass);
        return info;
    }
}
