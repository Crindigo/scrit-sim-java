package com.crindigo.scritsim;

import com.crindigo.scritsim.model.*;
import com.crindigo.scritsim.model.components.ControlRod;
import com.crindigo.scritsim.model.components.CoolantChannel;
import com.crindigo.scritsim.model.components.FuelRod;
import com.crindigo.scritsim.model.components.ReactorComponent;

import static com.crindigo.scritsim.Data.Coolants.*;
import static com.crindigo.scritsim.Data.Fuels.*;

public class ReactorSim
{
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

    private ControlRod makeControl() {
        return new ControlRod(100000, false, 1, 800);
    }

    private void run() {

        // F C F C F C F
        // X C X C X C X
        // F C F C F C F
        FissionReactor fr = new FissionReactor(3, 2, 0.5);

        /*for ( int row = 0; row < 7; row++ ) {
            if ( row % 2 == 0 ) {
                fr.addComponent(makeFuelRod(leu235Dioxide), 0, row);
                fr.addComponent(makeFuelRod(leu235Dioxide), 2, row);
                fr.addComponent(makeFuelRod(leu235Dioxide), 4, row);
                fr.addComponent(makeFuelRod(leu235Dioxide), 6, row);
            } else {
                fr.addComponent(makeControl(), 0, row);
                fr.addComponent(makeControl(), 2, row);
                fr.addComponent(makeControl(), 4, row);
                fr.addComponent(makeControl(), 6, row);
            }
            fr.addComponent(makeChannel(pressurizedWaterCoolant), 1, row);
            //fr.addComponent(makeChannel(boilingWaterCoolant), 3, row);
            //fr.addComponent(makeChannel(distilledWaterCoolant), 5, row);
        }*/

        // For positioning, X goes left to right, and Y goes from near to far when standing above
        // the fission reactor MTE facing towards the center.
        // 0,2  1,2  2,2
        // 0,1  1,1  2,1
        // 0,0, 1,0  2,0
        fr.addComponent(makeFuelRod(heu235), 0, 0);
        fr.addComponent(makeFuelRod(leu235), 2, 0);
        fr.addComponent(makeFuelRod(leu235), 0, 2);
        fr.addComponent(makeFuelRod(leu235), 2, 2);

        //fr.addComponent(makeFuelRod(), 0, 1);
        //fr.addComponent(makeFuelRod(), 2,1);
        //fr.addComponent(makeControl(), 0, 1);
        //fr.addComponent(makeControl(), 2, 1);

        fr.addComponent(makeChannel(distilledWaterCoolant), 1, 2);
        //fr.addComponent(makeChannel(pressurizedWaterCoolant), 1, 1);
        fr.addComponent(makeControl(), 1, 0);
        fr.addComponent(makeControl(), 1, 1);
        //fr.addComponent(makeChannel(pressurizedWaterCoolant), 1, 2);
        fr.addComponent(makeChannel(distilledWaterCoolant), 0, 1);
        fr.addComponent(makeChannel(distilledWaterCoolant), 2, 1);

        fr.prepareThermalProperties();
        fr.computeGeometry();

        ReactorComponent[][] reactorLayout = fr.getReactorLayout();
        for ( int y = 0; y < 3; y++ ) {
            for ( int x = 0; x < 3; x++ ) {
                if ( reactorLayout[x][y] != null ) {
                    System.out.println("(" + x + ", " + y + ") " + reactorLayout[x][y]);
                }
            }
        }

        for ( int i = 0; i < 32000; i++ ) {
            fr.debug = i % 100 == 0;
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

            if (i % 100 == 0) {
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