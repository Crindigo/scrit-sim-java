package com.crindigo.scritsim.model;

public class Config {

    public static NuclearOptions nuclear = new NuclearOptions();

    public static class NuclearOptions {

        public double nuclearPowerMultiplier = 0.05;

        public double fissionCoolantDivisor = 4.49578125;

        public double fissionReactorResolution = 100;

        public int fissionReactorPowerIterations = 10;
    }

}

