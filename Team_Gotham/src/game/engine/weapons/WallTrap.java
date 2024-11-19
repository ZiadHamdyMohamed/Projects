package game.engine.weapons;

import game.engine.titans.Titan;

import java.util.PriorityQueue;

public class WallTrap extends Weapon{
   public static final int WEAPON_CODE = 4;

    public WallTrap(int baseDamage) {
        super(baseDamage);
    }

    public int turnAttack(PriorityQueue<Titan> laneTitans) {
        int resourcesGained = 0;


        if (!laneTitans.isEmpty()) {
            Titan target = laneTitans.peek();
            int distance = target.getDistance();
            if (distance <= 0) {
                int damageDealt = getDamage();
                resourcesGained += target.takeDamage(damageDealt);
                if (target.isDefeated()) {
                    laneTitans.poll();
                }
            }
        }

        return resourcesGained;
    }

}
