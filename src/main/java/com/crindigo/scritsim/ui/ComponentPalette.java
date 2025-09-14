package com.crindigo.scritsim.ui;

import com.crindigo.scritsim.Data;
import com.crindigo.scritsim.model.CoolantHandler;
import com.crindigo.scritsim.model.CoolantProperty;
import com.crindigo.scritsim.model.FissionFuelProperty;
import com.crindigo.scritsim.model.ModeratorProperty;
import com.crindigo.scritsim.model.components.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ComponentPalette
{
    public static List<Paint> fuels = new ArrayList<>();
    public static List<Paint> coolants = new ArrayList<>();
    public static List<Paint> moderators = new ArrayList<>(2);
    public static List<Paint> controls = new ArrayList<>(2);

    public static Paint empty = new Paint(null, "", "Empty");

    public static void init()
    {
        fuels.add(new Paint(makeFuelRod(Data.Fuels.leu235), "fuel_rod_leu235.png", "LEU-235"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.heu235), "fuel_rod_heu235.png", "HEU-235"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.lowGradeMox), "fuel_rod_mox_low.png", "Low-Grade MOX"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.highGradeMox), "fuel_rod_mox_high.png", "High-Grade MOX"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.leu235Dioxide), "fuel_rod_leu235_dioxide.png", "LEU-235 Dioxide"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.haleu235Dioxide), "fuel_rod_haleu235_dioxide.png", "HALEU-235 Dioxide"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.heu235Dioxide), "fuel_rod_heu235_dioxide.png", "HEU-235 Dioxide"));
        fuels.add(new Paint(makeFuelRod(Data.Fuels.mixedOxide), "fuel_rod_mox_susy.png", "Mixed Oxide"));

        coolants.add(new Paint(makeCoolant(Data.Coolants.distilledWaterCoolant), "coolant_distilled.png", "Distilled Water"));
        coolants.add(new Paint(makeCoolant(Data.Coolants.boilingWaterCoolant), "coolant_boiling.png", "Boiling Water"));
        coolants.add(new Paint(makeCoolant(Data.Coolants.pressurizedWaterCoolant), "coolant_pressurized.png", "Pressurized Water"));
        coolants.add(new Paint(makeCoolant(Data.Coolants.pressurizedHeavyWaterCoolant), "coolant_pressurized_heavy.png", "Pressurized Heavy Water"));

        moderators.add(new Paint(makeModerator(Data.Moderators.graphiteModerator), "moderator_graphite.png", "Graphite Moderator"));
        moderators.add(new Paint(makeModerator(Data.Moderators.berylliumModerator), "moderator_beryllium.png", "Beryllium Moderator"));

        controls.add(new Paint(makeControl(false), "control_rod.png", "Normal Control Rod"));
        controls.add(new Paint(makeControl(true), "control_rod_moderated.png", "Graphite-Tipped Control Rod"));
    }

    public static List<Paint> allPaints()
    {
        List<Paint> all = new ArrayList<>(fuels);
        all.addAll(coolants);
        all.addAll(moderators);
        all.addAll(controls);
        all.add(empty);
        return all;
    }

    public static class Paint {
        public Supplier<ReactorComponent> componentSupplier;
        public String image;
        public String name;

        public Paint(Supplier<ReactorComponent> componentSupplier, String image, String name) {
            this.componentSupplier = componentSupplier;
            this.image = image;
            this.name = name;
        }
    }

    private static Supplier<ReactorComponent> makeFuelRod(FissionFuelProperty fuel) {
        return () -> new FuelRod(fuel.getMaxTemperature(), 1, fuel, 650);
    }

    private static Supplier<ReactorComponent> makeCoolant(CoolantProperty coolant) {
        CoolantHandler in = new CoolantHandler(coolant.getCoolant(), 16000);
        in.getFluidTank().setInfinite(true);
        CoolantHandler out = new CoolantHandler(coolant.getHotCoolant(), 16000);
        out.getFluidTank().setInfinite(true);
        return () -> new CoolantChannel(100050, 0, coolant, 1000, in, out);
    }

    private static Supplier<ReactorComponent> makeControl(boolean hasModeratorTip) {
        return () -> new ControlRod(100000, hasModeratorTip, 1, 800);
    }

    private static Supplier<ReactorComponent> makeModerator(ModeratorProperty moderator) {
        return () -> new Moderator(moderator, 0.5, 800);
    }
}
