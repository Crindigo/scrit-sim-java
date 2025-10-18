package com.crindigo.scritsim.model.components;

import com.crindigo.scritsim.model.IFissionFuelStats;
import com.crindigo.scritsim.model.ItemStack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FuelRod extends ReactorComponent {

    @Getter
    private IFissionFuelStats fuel;
    @Getter
    @Setter
    private double weight = 1;

    @Getter
    @Setter
    private double thermalProportion;

    private double depletionPoint;

    @Getter
    private int fuelUsed;

    public FuelRod(double maxTemperature, double thermalConductivity, IFissionFuelStats fuel, double mass) {
        super(0, maxTemperature, thermalConductivity, mass, true);
        this.fuel = fuel;
        this.depletionPoint = fuel.getDuration();
    }

    public double getDuration() {
        return fuel.getDuration();
    }

    public double getNeutronGenerationTime() {
        return fuel.getNeutronGenerationTime();
    }

    public void setFuel(IFissionFuelStats property) {
        this.fuel = property;
        this.maxTemperature = property.getMaxTemperature();
    }

    public ItemStack getDepletedFuel() {
        return fuel.getDepletedFuel(thermalProportion);
    }

    public boolean isDepleted(double totalDepletion) {
        return this.depletionPoint <= totalDepletion * weight;
    }

    public double depletionPercent(double totalDepletion) {
        // interp between previous and current depletion thresholds
        var previous = depletionPoint - fuel.getDuration();
        var rodDepletion = totalDepletion * weight;
        var percent = (rodDepletion - previous) / fuel.getDuration();
        return Math.min(1.0, Math.max(0.0, percent));
    }

    public void markUndepleted() {
        this.depletionPoint += fuel.getDuration();
    }

    @Override
    public String toString() {
        return "FuelRod{" +
                "weight=" + weight +
                ", thermalProportion=" + thermalProportion +
                '}';
    }

    @Override
    public List<String> info() {
        List<String> details = super.info();
        details.addFirst("<b>" + fuel.getId() + "</b>");
        details.add(String.format("Weight: %.6f", weight));
        details.add(String.format("Thermal Proportion: %.6f", thermalProportion));
        details.add("Fuel Consumed: " + fuelUsed);
        return details;
    }

    public void consume() {
        fuelUsed++;
    }
}
