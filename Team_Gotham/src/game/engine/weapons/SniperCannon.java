package game.engine.weapons;

import game.engine.titans.Titan;
import java.util.*;

public class SniperCannon extends Weapon {
    public static final int WEAPON_CODE = 2;
    public SniperCannon(int baseDamage) {

        super(baseDamage);
    }


    @Override
    public int turnAttack(PriorityQueue<Titan> laneTitans) {
        int resourcesGained = 0;

        if (!laneTitans.isEmpty()) {
            Titan target = laneTitans.peek();
            int damageDealt = getDamage();
            resourcesGained += target.takeDamage(damageDealt);
            if (target.isDefeated()) {
                laneTitans.remove(target);
            }
        }

        return resourcesGained;
    }
}
