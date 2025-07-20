package com.crindigo.scritsim.model;

public class Config {

    public static NuclearOptions nuclear = new NuclearOptions();

    public static class NuclearOptions {

        public double nuclearPowerMultiplier = 0.1;

        public double fissionCoolantDivisor = 14;

        public double fissionReactorResolution = 100;

        public int fissionReactorPowerIterations = 10;
    }

}

