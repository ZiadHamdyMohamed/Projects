package game.engine.interfaces;

public interface Attackee {
    public int getCurrentHealth();
    public void setCurrentHealth(int health);
    public int getResourcesValue();

    default boolean isDefeated(){
        if (this.getCurrentHealth()<=0){
            return true;
        }
        return false;
    }

    default int takeDamage(int damage) {
        int currentHealth = this.getCurrentHealth();
        currentHealth -= damage;
        this.setCurrentHealth(currentHealth);

        if (isDefeated()) {
            return this.getResourcesValue();
        }
        return 0;
    }
}