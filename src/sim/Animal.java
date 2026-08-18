package sim;

public abstract class Animal extends Actor {
    private int age = 0;
    private int foodLevel;

    protected void incrementAge(int maxAge) {
        age++;
        if (age > maxAge) setDead();
    }

    protected int getAge() { return age; }
    protected void setFoodLevel(int level) { this.foodLevel = level; }
    protected int getFoodLevel() { return foodLevel; }

    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) setDead();
    }
}