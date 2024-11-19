package game.engine.titans;

import game.engine.interfaces.*;

public abstract class Titan implements Comparable<Titan>, Attackee,Attacker, Mobil {
    private final int baseHealth;
    public boolean FirstTime;
    private int currentHealth;
    private final int baseDamage;
    private final int heightInMeters;
    private int distanceFromBase;
    private int speed;
    private final int resourcesValue;
    private final int dangerLevel ;


    public Titan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase, int speed, int resourcesValue, int dangerLevel) {
        this.baseHealth= baseHealth;
        this.currentHealth = Math.max(0,baseHealth);
        this.baseDamage = baseDamage;
        this.heightInMeters = heightInMeters;
        this.distanceFromBase = distanceFromBase;
        this.speed = speed;
        this.resourcesValue = resourcesValue;
        this.dangerLevel= dangerLevel;
    }

    public int compareTo(Titan o) {
        return Integer.compare(this.distanceFromBase, o.distanceFromBase);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }


    public void setCurrentHealth(int currentHealth) {
            this.currentHealth=Math.max(currentHealth,0);
    }


    public int getDistance() {
        return distanceFromBase;
    }


    public void setDistance(int distanceFromBase) {
        this.distanceFromBase=Math.max(distanceFromBase,0);

    }


    public int getSpeed() {
        return speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public int getBaseHealth() {
        return baseHealth;
    }


    public int getDamage() {
        return baseDamage;
    }


    public int getHeightInMeters() {
        return heightInMeters;
    }


    public int getResourcesValue() {

        return resourcesValue;
    }


    public int getDangerLevel() {
        return dangerLevel;
    }






}
