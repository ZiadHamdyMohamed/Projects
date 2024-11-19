package game.engine.weapons;

import game.engine.titans.Titan;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class PiercingCannon extends Weapon {
    public static final int WEAPON_CODE = 1;

    public PiercingCannon(int baseDamage) {
        super(baseDamage);
    }

    @Override
    public int turnAttack(PriorityQueue<Titan> laneTitans) {
    int x=0;
    ArrayList<Titan>reamainAfter=new ArrayList<>();
    for (int i=0; i<5 && !laneTitans.isEmpty(); i++){
        x+=attack(laneTitans.peek());
        if (laneTitans.peek().isDefeated()){
            laneTitans.poll();
        }
        else{
            reamainAfter.add(laneTitans.poll());
        }
    }
    laneTitans.addAll(reamainAfter);
    return x;
    }
}
