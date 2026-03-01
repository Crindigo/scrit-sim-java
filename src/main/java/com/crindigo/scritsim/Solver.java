package com.crindigo.scritsim;

import com.crindigo.scritsim.model.*;
import com.crindigo.scritsim.model.components.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solver {
    /*public static void main(String[] args) {
        int diameter = 3;
        int reactorDepth = 3;
        int totalSteps = 21600;
        FissionReactor fr = new FissionReactor(diameter, reactorDepth, 0.5);

        List<FuelRod> fuelRods = new ArrayList<>();
        List<CoolantChannel> coolantChannels = new ArrayList<>();
        for ( int y = 0; y < diameter; y++ ) {
            for ( int x = 0; x < diameter; x++ ) {
                if ( reactorCanvas[x][y] == null || reactorCanvas[x][y].componentSupplier == null ) {
                    continue;
                }

                ReactorComponent component = reactorCanvas[x][y].componentSupplier.get();
                fr.addComponent(component, x, y);
                if ( component instanceof FuelRod rod ) {
                    fuelRods.add(rod);
                }
                if ( component instanceof CoolantChannel channel ) {
                    coolantChannels.add(channel);
                }
            }
        }

        fr.prepareThermalProperties();
        fr.computeGeometry();

        // Track all types of consumed fuel and generated hot coolant
        Map<String, Double> consumedFuel = new HashMap<>();
        Map<String, Integer> generatedCoolant = new HashMap<>();
        Map<ICoolantStats, Integer> generatedCoolantWarmedUp = new HashMap<>();
        int steps;
        for ( steps = 0; steps < totalSteps; steps++ ) {
            // scrit checks fuel depletion before updating state
            for (FuelRod fuelRod : fuelRods) {
                String fuelType = fuelRod.getFuel().getId();
                if (fuelRod.isDepleted(fr.fuelDepletion)) {
                    fuelRod.markUndepleted();
                    fuelRod.consume();
                    consumedFuel.compute(fuelType, (k, v) -> (v == null) ? 1 : (v + 1));
                }
            }

            fr.updatePower();
            fr.updateTemperature();

            // this updates in updateTemperature so calculate it right after
            for (CoolantChannel cc : coolantChannels) {
                final String hotCoolant = cc.getCoolant().getHotCoolant().getName();
                final int liters = cc.getGeneratedHotCoolant();
                generatedCoolant.compute(hotCoolant, (k, v) -> (v == null) ? liters : (v + liters));

                // Track coolant for the latest hour of runtime when we know it's warmed up
                if ( steps >= (totalSteps - 3600) ) {
                    generatedCoolantWarmedUp.compute(cc.getCoolant(),
                            (k, v) -> (v == null) ? liters : (v + liters));

                    cc.setLastHourCoolantGenerated(cc.getLastHourCoolantGenerated() + liters);
                }
            }

            fr.updatePressure();
            fr.updateNeutronPoisoning();
            fr.regulateControlRods();

            if ( fr.checkForMeltdown() ) {
                System.out.println("meltdown " + steps);
                return;
            }
            if ( fr.checkForExplosion() ) {
                System.out.println("explosion " + steps);
                return;
            }
        }

        // Add in how much fractional fuel has been consumed on the current rods
        for (FuelRod fuelRod : fuelRods) {
            String fuelType = fuelRod.getFuel().getId();
            var fraction = fuelRod.depletionPercent(fr.fuelDepletion);
            consumedFuel.compute(fuelType, (k, v) -> (v == null) ? 0 : (v + fraction));
        }

        StringBuilder builder = new StringBuilder();
        consumedFuel.forEach((k, v) -> {
            builder.append("consume ").append(k).append(' ').append(v).append('\n');
        });
        generatedCoolantWarmedUp.forEach((k, v) -> {
            builder.append("generate ").append(k.getHotCoolant().getName()).append(' ').append(v / 3600.).append('\n');
        });

        System.out.println(builder);
    }

    private FuelRod makeFuelRod(FissionFuelProperty fuel) {
        return new FuelRod(fuel.getMaxTemperature(), 1, fuel, 650);
    }

    private CoolantChannel makeChannel(CoolantProperty coolant) {
        CoolantHandler in = new CoolantHandler(coolant.getCoolant(), 16000);
        in.getFluidTank().setInfinite(true);
        CoolantHandler out = new CoolantHandler(coolant.getHotCoolant(), 16000);
        out.getFluidTank().setInfinite(true);
        return new CoolantChannel(100050, 0, coolant, 1000, in, out);
    }

    private ControlRod makeControl(boolean hasModeratorTip) {
        return new ControlRod(100000, hasModeratorTip, 1, 800);
    }

    private Moderator makeModerator(ModeratorProperty moderator) {
        return new Moderator(moderator, 0.5, 800);
    }
*/
}
