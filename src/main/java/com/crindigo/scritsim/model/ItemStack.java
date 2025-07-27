package com.crindigo.scritsim.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ItemStack
{
    private final String id;

    @Setter
    private int amount;

    public ItemStack(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }
}
