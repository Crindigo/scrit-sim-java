package com.crindigo.scritsim.model.components;

import com.crindigo.scritsim.model.IFissionFuelStats;
import com.crindigo.scritsim.model.ItemStack;
import lombok.Getter;
import lombok.Setter;

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
}
