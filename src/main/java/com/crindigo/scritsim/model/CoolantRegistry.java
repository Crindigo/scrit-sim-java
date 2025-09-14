package com.crindigo.scritsim.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CoolantRegistry {

    private static final Map<Fluid, ICoolantStats> COOLANTS = new HashMap<>();
    private static final Map<ICoolantStats, Fluid> COOLANTS_INVERSE = new HashMap<>();

    public static void registerCoolant(@NotNull Fluid fluid, @NotNull ICoolantStats coolant) {
        COOLANTS.put(fluid, coolant);
        COOLANTS_INVERSE.put(coolant, fluid);
    }

    @Nullable
    public static ICoolantStats getCoolant(Fluid fluid) {
        return COOLANTS.get(fluid);
    }

    @NotNull
    public static Collection<Fluid> getAllCoolants() {
        return COOLANTS.keySet();
    }

    @Nullable
    public static Fluid originalFluid(ICoolantStats stats) {
        return COOLANTS_INVERSE.get(stats);
    }
}
