package com.novinitygames.dragonSMP.dragons;

import com.novinitygames.dragonSMP.dragons.classes.*;

import java.util.Random;

public abstract class Dragon {
    public DragonUser dragonUser;

    public boolean fallDamageEnabled = true;
    public int ability1Cooldown = 60;

    public abstract void Init(DragonUser dragonUser);
    public abstract void Uninit();
    public abstract void UseAbility1();
    public abstract String getId();

    public int getAbility1Cooldown(DragonUser dragonUser) {
        if (dragonUser.cooldowns.containsKey(1) && (System.currentTimeMillis() - dragonUser.cooldowns.get(1)) / 1000 < ability1Cooldown) {
            double diff = ability1Cooldown - Math.floor((double) (System.currentTimeMillis() - dragonUser.cooldowns.get(1)) / 1000);
            return Math.toIntExact((long)diff);
        }
        return 0;
    }

    public static Dragon getRandomDragon() {
        Random rand = new Random();
        int random = rand.nextInt(6) + 1;

        Dragon dragon = null;
        switch (random) {
            case 1:
                dragon = new FireDragon();
                break;
            case 2:
                dragon = new WaterDragon();
                break;
            case 3:
                dragon = new IceDragon();
                break;
            case 4:
                dragon = new WindDragon();
                break;
            case 5:
                dragon = new EarthDragon();
                break;
            case 6:
                dragon = new LifeDragon();
                break;
        }

        return dragon;
    }

    public static Dragon getDragonById(String id) {
        Dragon dragon = null;

        switch (id) {
            case "fire":
                dragon = new FireDragon();
                break;
            case "water":
                dragon = new WaterDragon();
                break;
            case "ice":
                dragon = new IceDragon();
                break;
            case "wind":
                dragon = new WindDragon();
                break;
            case "earth":
                dragon = new EarthDragon();
                break;
            case "life":
                dragon = new LifeDragon();
                break;
        }

        return dragon;
    }
}
