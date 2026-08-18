package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Field {
    private static final Random rand = new Random();

    private final int width;
    private final int height;
    private final Actor[][] grid;
    private final List<Actor> actors = new ArrayList<>();

    public Field(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                "Field dimensions must be positive: got " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.grid = new Actor[height][width];
    }

    private boolean isValidLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    private void checkLocation(Location location) {
        if (!isValidLocation(location)) {
            throw new InvalidLocationException(
                "Location " + location + " is outside the field bounds ("
                + width + "x" + height + ")");
        }
    }

    public void place(Actor actor, Location location) {
        checkLocation(location);
        grid[location.getRow()][location.getCol()] = actor;
        actor.setLocation(location);
        actors.add(actor);
    }

    public void move(Actor actor, Location newLocation) {
        checkLocation(newLocation);
        clear(actor.getLocation());
        grid[newLocation.getRow()][newLocation.getCol()] = actor;
        actor.setLocation(newLocation);
    }

    public Actor getActorAt(Location location) {
        checkLocation(location);
        return grid[location.getRow()][location.getCol()];
    }

    public Optional<Location> freeAdjacentLocation(Location location) {
        List<Location> free = new ArrayList<>();
        for (Location loc : adjacentLocations(location)) {
            if (getActorAt(loc) == null) {
                free.add(loc);
            }
        }
        if (free.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(free.get(rand.nextInt(free.size())));
    }

    public List<Location> adjacentLocations(Location location) {
        List<Location> locations = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Location candidate = new Location(location.getRow() + dr, location.getCol() + dc);
                if (isValidLocation(candidate)) {
                    locations.add(candidate);
                }
            }
        }
        return locations;
    }

    public void clear(Location location) {
        checkLocation(location);
        grid[location.getRow()][location.getCol()] = null;
    }

    public void removeActor(Actor actor) {
        clear(actor.getLocation());
        actors.remove(actor);
    }

    public List<Actor> getActors() { return actors; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}