package sim;

public abstract class Actor {
    private Location location;
    private boolean alive = true;

    public abstract void act(Field field);

    public Location getLocation() { return location; }
    protected void setLocation(Location location) { this.location = location; }
    public boolean isAlive() { return alive; }
    protected void setDead() { alive = false; }
}