package com.crindigo.scritsim.model;

// drain fill getCapacity getFluidAmount

import lombok.Getter;
import lombok.Setter;

@Getter
public class FluidTank
{
    private final Fluid fluid;
    private final int capacity;
    private int fluidAmount;
    @Setter
    private boolean infinite = false;

    public FluidTank(Fluid fluid, int capacity) {
        this.fluid = fluid;
        this.capacity = capacity;
    }

    public FluidStack drain(int amount, boolean doDrain) {
        int movable = Math.min(amount, infinite ? capacity : fluidAmount);
        FluidStack stack = new FluidStack(fluid, movable);
        if ( doDrain && !infinite ) {
            fluidAmount -= movable;
        }
        return stack;
    }

    public int fill(FluidStack fluidStack, boolean doFill) {
        if ( infinite ) {
            return capacity;
        }

        int movable = Math.min(fluidStack.getAmount(), capacity - fluidAmount);
        if ( doFill ) {
            fluidStack.setAmount(fluidStack.getAmount() - movable);
            fluidAmount += movable;
        }
        return movable;
    }
}
