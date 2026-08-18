package sim;

public abstract class Actor {
    private boolean alive = true;

    public abstract void act();

    public boolean isAlive() {
        return alive;
    }

    protected void setDead() {
        alive = false;
    }
}
