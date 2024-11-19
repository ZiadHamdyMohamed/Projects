package game.engine.lanes;
import java.util.*;

import game.engine.base.Wall;
import game.engine.titans.*;
import game.engine.weapons.*;

public class Lane implements Comparable<Lane> {

    private final Wall laneWall;
    private int dangerLevel;
    private final PriorityQueue<Titan> titans;
    private final ArrayList <Weapon> weapons;

    public Lane(Wall laneWall) {
        this.laneWall = laneWall;
        this.dangerLevel = 0;
        this.titans = new PriorityQueue<>();
        this.weapons = new ArrayList<>();
    }



    public int compareTo(Lane other) {
        return Integer.compare(this.dangerLevel, other.dangerLevel);
    }

    public Wall getLaneWall() {
        return laneWall;
    }

    public int getDangerLevel() {
        return dangerLevel;
    }

    public PriorityQueue<Titan> getTitans() {
        return titans;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void setDangerLevel(int dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public void addTitan(Titan titan) {
        titans.add(titan);
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public void moveLaneTitans() {
        PriorityQueue<Titan> movedTitans = new PriorityQueue<>();
        while (!titans.isEmpty()) {
            Titan titan = titans.poll();
            if (!titan.hasReachedTarget()) {
                titan.move();
            }
            movedTitans.add(titan);
        }
        titans.addAll(movedTitans);
    }

    public int performLaneTitansAttacks() {

        int sum=0;
         for (Titan t: titans){
             if (t.hasReachedTarget()){
                 sum+= t.attack(laneWall);
             }
         }

         return sum;
    }

    public int performLaneWeaponsAttacks() {
        int x = 0;
        for (Weapon y : weapons) {
            x += y.turnAttack(titans);
        }
        return x;
    }
    public boolean isLaneLost() {
        return this.laneWall.getCurrentHealth() <= 0;
    }


    public void updateLaneDangerLevel() {
        int totalDangerLevel = 0;
        for (Titan titan : titans) {
                totalDangerLevel += titan.getDangerLevel();
        }
        this.dangerLevel = totalDangerLevel;
    }

}


