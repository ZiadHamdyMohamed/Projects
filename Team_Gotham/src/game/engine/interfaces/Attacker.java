package game.engine.interfaces;

public interface Attacker {
   public int getDamage();

   default int attack(Attackee target) {
      int damage = this.getDamage();
      int resourcesGained = target.takeDamage(damage);
      return resourcesGained;
   }
}