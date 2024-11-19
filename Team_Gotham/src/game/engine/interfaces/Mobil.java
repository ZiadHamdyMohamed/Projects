package game.engine.interfaces;

public interface Mobil {
    public int getDistance();
    public void setDistance(int distance);
    public int getSpeed();
    public void setSpeed(int speed);

    default boolean hasReachedTarget() {
        return getDistance() <= 0;
    }

    default boolean move() {
        int newDistance = getDistance() - getSpeed();
        setDistance(newDistance);

        return hasReachedTarget();
    }
}
