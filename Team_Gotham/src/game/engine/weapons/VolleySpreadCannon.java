package game.engine.weapons;

import game.engine.titans.Titan;

import java.util.PriorityQueue;

public class VolleySpreadCannon extends Weapon{
   public static final int WEAPON_CODE = 3;

    private final int minRange;
    private final int maxRange;

    public VolleySpreadCannon(int baseDamage, int minRange, int maxRange) {
        super(baseDamage);
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int turnAttack(PriorityQueue<Titan> laneTitans) {
        int resourcesGained = 0;


        PriorityQueue<Titan> titansInRange = new PriorityQueue<>(laneTitans);
        while (!titansInRange.isEmpty()) {
            Titan target = titansInRange.poll();
            int distance = target.getDistance();
            if (distance >= minRange && distance <= maxRange) {
                int damageDealt = getDamage();
                resourcesGained += target.takeDamage(damageDealt);
                if (target.isDefeated()) {
                    laneTitans.remove(target);
                }
            }
        }

        return resourcesGained;
    }
}
