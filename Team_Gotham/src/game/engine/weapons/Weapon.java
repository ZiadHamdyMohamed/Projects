package game.engine.weapons;

import game.engine.interfaces.*;
import game.engine.titans.Titan;
import java.util.PriorityQueue;

public abstract class Weapon implements Attacker {
    private final int baseDamage;

    public Weapon (int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getDamage() {
        return baseDamage;
    }

    public abstract int turnAttack(PriorityQueue<Titan> laneTitans);

}
