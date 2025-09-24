package com.crindigo.scritsim;

import com.crindigo.scritsim.model.*;

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
                        .releasedHeatEnergy(0.025)
                        .decayRate(0.025)
                        .build();

        public static final FissionFuelProperty heu235 =
                FissionFuelProperty.builder("heu235", 1800, 60000, 2.5)
                        .fastNeutronCaptureCrossSection(0.3)
                        .slowNeutronCaptureCrossSection(2)
                        .slowNeutronFissionCrossSection(2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.5)
                        .releasedHeatEnergy(0.025)
                        .decayRate(0.05)
                        .build();

        public static final FissionFuelProperty lowGradeMox =
                FissionFuelProperty.builder("lowGradeMox", 1600, 50000, 1.5)
                        .fastNeutronCaptureCrossSection(0.5)
                        .slowNeutronCaptureCrossSection(2.2)
                        .slowNeutronFissionCrossSection(2.2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.60)
                        .releasedHeatEnergy(0.052)
                        .decayRate(0.1)
                        .build();

        public static final FissionFuelProperty highGradeMox =
                FissionFuelProperty.builder("highGradeMox", 2000, 80000, 1)
                        .fastNeutronCaptureCrossSection(0.5)
                        .slowNeutronCaptureCrossSection(2.4)
                        .slowNeutronFissionCrossSection(2.4)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.80)
                        .releasedHeatEnergy(0.056)
                        .decayRate(0.2)
                        .build();

        // Supersymmetry Fuels

        public static final FissionFuelProperty leu235Dioxide =
                FissionFuelProperty.builder("leu235Dioxide", 1500, 100000, 3.5)
                        .fastNeutronCaptureCrossSection(0.4)
                        .fastNeutronFissionCrossSection(0.2)
                        .slowNeutronCaptureCrossSection(1.8)
                        .slowNeutronFissionCrossSection(1.8)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.5)
                        .releasedHeatEnergy(0.025)
                        .decayRate(0.025)
                        .build();

        public static final FissionFuelProperty haleu235Dioxide =
                FissionFuelProperty.builder("haleu235Dioxide", 1600, 200000, 3)
                        .fastNeutronCaptureCrossSection(0.35)
                        .fastNeutronFissionCrossSection(0.175)
                        .slowNeutronCaptureCrossSection(1.9)
                        .slowNeutronFissionCrossSection(1.9)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.5)
                        .releasedHeatEnergy(0.025)
                        .decayRate(0.025)
                        .build();

        public static final FissionFuelProperty heu235Dioxide =
                FissionFuelProperty.builder("heu235Dioxide", 1800, 400000, 2.5)
                        .fastNeutronCaptureCrossSection(0.3)
                        .fastNeutronFissionCrossSection(0.15)
                        .slowNeutronCaptureCrossSection(2)
                        .slowNeutronFissionCrossSection(2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.5)
                        .releasedHeatEnergy(0.025)
                        .decayRate(0.05)
                        .build();

        public static final FissionFuelProperty mixedOxide =
                FissionFuelProperty.builder("mixedOxide", 1600, 60000, 1.5)
                        .fastNeutronCaptureCrossSection(0.5)
                        .fastNeutronFissionCrossSection(0.25)
                        .slowNeutronCaptureCrossSection(2.2)
                        .slowNeutronFissionCrossSection(2.2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(2.60)
                        .releasedHeatEnergy(0.052)
                        .decayRate(0.1)
                        .build();

        public static final FissionFuelProperty bismuth =
                FissionFuelProperty.builder("bismuth", 560, 5000, 5)
                        .slowNeutronCaptureCrossSection(0.2)
                        .requiredNeutrons(1)
                        .releasedNeutrons(0)
                        .releasedHeatEnergy(0.005)
                        .build();
    }

    public static class Fluids
    {
        public static final Fluid distilledWater = new Fluid("distilled_water", 293);
        public static final Fluid highPressureSteam = new Fluid("hp_steam", 500);
        public static final Fluid boilingWater = new Fluid("boiling_water", 548);
        public static final Fluid highPressureWetSteam = new Fluid("hp_wet_steam", 558);
        public static final Fluid pressurizedWater = new Fluid("pressurized_water", 548);
        public static final Fluid hotPressurizedWater = new Fluid("hp_water", 588);
        public static final Fluid pressurizedHeavyWater = new Fluid("pressurized_heavy_water", 548);
        public static final Fluid hotPressurizedHeavyWater = new Fluid("hp_heavy_water", 588);
    }

    public static class Coolants
    {
        public static final CoolantProperty distilledWaterCoolant =
                new CoolantProperty(18, Fluids.distilledWater, Fluids.highPressureSteam,
                        2., 1000,
                        373, 2260000, 4168.)
                        .setAccumulatesHydrogen(true)
                        .setSlowAbsorptionFactor(0.1875)
                        .setFastAbsorptionFactor(0.0625);

        public static final CoolantProperty boilingWaterCoolant =
                new CoolantProperty(18, Fluids.boilingWater, Fluids.highPressureWetSteam,
                        1, 1000,
                        558, 2260000, 4184)
                        .setAccumulatesHydrogen(true)
                        .setSlowAbsorptionFactor(0.1875)
                        .setFastAbsorptionFactor(0.0625);

        public static final CoolantProperty pressurizedWaterCoolant =
                new CoolantProperty(18, Fluids.pressurizedWater, Fluids.hotPressurizedWater,
                        1, 1000,
                        588, 2260000, 4184)
                        .setAccumulatesHydrogen(true)
                        .setSlowAbsorptionFactor(0.1875)
                        .setFastAbsorptionFactor(0.0625);

        public static final CoolantProperty pressurizedHeavyWaterCoolant =
                new CoolantProperty(18, Fluids.pressurizedHeavyWater, Fluids.hotPressurizedHeavyWater,
                        4, 1000,
                        588, 2064000, 4228)
                        .setAccumulatesHydrogen(true)
                        .setSlowAbsorptionFactor(0.1875)
                        .setFastAbsorptionFactor(0.0625);

        static {
            CoolantRegistry.registerCoolant(Fluids.distilledWater, distilledWaterCoolant);
            CoolantRegistry.registerCoolant(Fluids.boilingWater, boilingWaterCoolant);
            CoolantRegistry.registerCoolant(Fluids.pressurizedWater, pressurizedWaterCoolant);
            CoolantRegistry.registerCoolant(Fluids.pressurizedHeavyWater, pressurizedHeavyWaterCoolant);
        }
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
