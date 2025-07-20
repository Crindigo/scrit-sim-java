package com.crindigo.scritsim.model;

import lombok.Getter;

@Getter
public class Fluid
{
    private final String name;
    private final int temperature;

    public Fluid(String name, int temperature) {
        this.name = name;
        this.temperature = temperature;
    }
}
