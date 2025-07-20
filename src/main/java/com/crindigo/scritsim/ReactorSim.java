package com.crindigo.scritsim;

import com.crindigo.scritsim.model.*;
import com.crindigo.scritsim.model.components.ControlRod;
import com.crindigo.scritsim.model.components.CoolantChannel;
import com.crindigo.scritsim.model.components.FuelRod;

public class ReactorSim
{
    private final Fluid distilledWater = new Fluid("distilled_water", 293);
    private final Fluid highPressureSteam = new Fluid("high_pressure_steam", 500);

    private final CoolantProperty distilledWaterCoolant =
            new CoolantProperty(18, highPressureSteam, 1., 1000,
            373, 2260000, 4168.)
            .setAccumulatesHydrogen(true);

    private final FissionFuelProperty leu235 =
            FissionFuelProperty.builder("leu235", 1500, 75000, 3.5)
                    .slowNeutronCaptureCrossSection(1.5)
                    .slowNeutronFissionCrossSection(1.5)
                    .requiredNeutrons(1)
                    .releasedNeutrons(2.5)
                    .releasedHeatEnergy(0.01)
                    .decayRate(0.025)
                    .build();

    private final FissionFuelProperty heu235 =
            FissionFuelProperty.builder("heu235", 1800, 60000, 2.5)
                    .slowNeutronCaptureCrossSection(2)
                    .slowNeutronFissionCrossSection(2)
                    .requiredNeutrons(1)
                    .releasedNeutrons(2.5)
                    .releasedHeatEnergy(0.01)
                    .decayRate(0.05)
                    .build();

    private FuelRod makeFuelRod() {
        return new FuelRod(leu235.getMaxTemperature(), 1, leu235, 650);
    }

    private CoolantChannel makeChannel() {
        CoolantHandler in = new CoolantHandler(distilledWater, 16000);
        in.getFluidTank().setInfinite(true);
        CoolantHandler out = new CoolantHandler(highPressureSteam, 16000);
        out.getFluidTank().setInfinite(true);
        return new CoolantChannel(100050, 0, distilledWaterCoolant, 1000, in, out);
    }

    private ControlRod makeControl() {
        return new ControlRod(100000, false, 1, 800);
    }

    private void run() {
        CoolantRegistry.registerCoolant(distilledWater, distilledWaterCoolant);

        // F C F C F C F
        // X C X C X C X
        // F C F C F C F
        FissionReactor fr = new FissionReactor(7, 1, 0.5);

        for ( int row = 0; row < 7; row++ ) {
            if ( row % 2 == 0 ) {
                fr.addComponent(makeFuelRod(), 0, row);
                fr.addComponent(makeFuelRod(), 2, row);
                fr.addComponent(makeFuelRod(), 4, row);
                fr.addComponent(makeFuelRod(), 6, row);
            } else {
                fr.addComponent(makeControl(), 0, row);
                fr.addComponent(makeControl(), 2, row);
                fr.addComponent(makeControl(), 4, row);
                fr.addComponent(makeControl(), 6, row);
            }
            fr.addComponent(makeChannel(), 1, row);
            //fr.addComponent(makeChannel(), 3, row);
            fr.addComponent(makeChannel(), 5, row);
        }

        /*fr.addComponent(makeFuelRod(), 0, 0);
        fr.addComponent(makeFuelRod(), 2, 0);
        fr.addComponent(makeFuelRod(), 0, 2);
        fr.addComponent(makeFuelRod(), 2, 2);

        fr.addComponent(makeFuelRod(), 0, 1);
        //fr.addComponent(makeFuelRod(), 2,1);
        //fr.addComponent(makeControl(), 0, 1);
        fr.addComponent(makeControl(), 2, 1);

        fr.addComponent(makeChannel(), 1, 0);
        fr.addComponent(makeChannel(), 1, 1);
        fr.addComponent(makeChannel(), 1, 2);*/

        fr.prepareThermalProperties();
        fr.computeGeometry();

        for ( int i = 0; i < 3200; i++ ) {
            fr.debug = i % 10 == 0;
            fr.updatePower();
            fr.updateTemperature();
            fr.updatePressure();
            fr.updateNeutronPoisoning();
            fr.regulateControlRods();
            if ( fr.checkForMeltdown() ) {
                System.out.println("Meltdown");
                break;
            }
            if ( fr.checkForExplosion() ) {
                System.out.println("Explosion");
                break;
            }

            if (i % 10 == 0) {
                System.out.printf("%d | Keff = %.5f, Ctrl = %.4f, Pwr: %.2f/%.2f, Temp: %.2f, Pr: %.0f/%.0f, Dep: %.2f\n",
                        i, fr.kEff, fr.controlRodInsertion, fr.power, fr.maxPower, fr.temperature,
                        fr.pressure, fr.maxPressure, fr.fuelDepletion);
            }
        }
    }

    public static void main(String[] args) {
        ReactorSim sim = new ReactorSim();
        sim.run();
    }
}