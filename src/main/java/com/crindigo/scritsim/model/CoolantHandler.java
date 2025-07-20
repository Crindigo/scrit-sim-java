package com.crindigo.scritsim.model;

import lombok.Getter;

@Getter
public class CoolantHandler
{
    private FluidTank fluidTank;

    public CoolantHandler(Fluid fluid, int capacity) {
        this.fluidTank = new FluidTank(fluid, capacity);
    }
}
