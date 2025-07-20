package com.crindigo.scritsim.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FluidStack
{
    private final Fluid fluid;

    @Setter
    private int amount;

    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }
}
