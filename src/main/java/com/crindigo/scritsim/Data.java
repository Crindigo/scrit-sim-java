package com.crindigo.scritsim;

import com.crindigo.scritsim.model.CoolantProperty;
import com.crindigo.scritsim.model.FissionFuelProperty;
import com.crindigo.scritsim.model.Fluid;
import com.crindigo.scritsim.model.ModeratorProperty;

public class Data
{
    public static class Fuels
    {
        public static final FissionFuelProperty leu235 =
                FissionFuelProperty.builder("leu235", 1500, 75000, 3.5)
                        .fastNeutronCaptureCrossSection(0.4)
                        .slowNeutronCaptureCrossSection(1.8)
                        .slowNeutronFissionCrossSection(1.8)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.5)
                        .releasedHeatEnergy(0.01)
                        .decayRate(0.025)
                        .build();

        public static final FissionFuelProperty heu235 =
                FissionFuelProperty.builder("heu235", 1800, 60000, 2.5)
                        .fastNeutronCaptureCrossSection(0.3)
                        .slowNeutronCaptureCrossSection(2)
                        .slowNeutronFissionCrossSection(2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.5)
                        .releasedHeatEnergy(0.01)
                        .decayRate(0.05)
                        .build();

        public static final FissionFuelProperty lowGradeMox =
                FissionFuelProperty.builder("lowGradeMox", 1600, 50000, 1.5)
                        .fastNeutronCaptureCrossSection(0.5)
                        .slowNeutronCaptureCrossSection(2.2)
                        .slowNeutronFissionCrossSection(2.2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.60)
                        .releasedHeatEnergy(0.02)
                        .decayRate(0.1)
                        .build();

        public static final FissionFuelProperty highGradeMox =
                FissionFuelProperty.builder("highGradeMox", 2000, 80000, 1)
                        .fastNeutronCaptureCrossSection(0.5)
                        .slowNeutronCaptureCrossSection(2.4)
                        .slowNeutronFissionCrossSection(2.4)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.80)
                        .releasedHeatEnergy(0.02)
                        .decayRate(0.2)
                        .build();
    }

    public static class Fluids
    {
        public static final Fluid distilledWater = new Fluid("distilled_water", 293);
        public static final Fluid highPressureSteam = new Fluid("high_pressure_steam", 500);
    }

    public static class Coolants
    {
        public static final CoolantProperty distilledWaterCoolant =
                new CoolantProperty(18, Fluids.highPressureSteam, 2., 1000,
                        373, 2260000, 4168.)
                        .setAccumulatesHydrogen(true)
                        .setSlowAbsorptionFactor(0.1875)
                        .setFastAbsorptionFactor(0.0625);
    }

    public static class Moderators
    {
        public static final ModeratorProperty graphiteModerator = ModeratorProperty.builder()
                .maxTemperature(3650)
                .absorptionFactor(0.0625)
                .moderationFactor(3)
                .build();

        public static final ModeratorProperty berylliumModerator = ModeratorProperty.builder()
                .maxTemperature(1500)
                .absorptionFactor(0.015625)
                .moderationFactor(5)
                .build();
    }
}
