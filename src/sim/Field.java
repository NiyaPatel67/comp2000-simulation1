package sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The grid the simulation runs on.
 * Implements Iterable (Iterator pattern) so other classes can loop over the
 * actors without ever seeing or modifying the internal list.
 */
public class Field implements Iterable<Actor> {
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

    public boolean isFree(Location location) {
        return getActorAt(location) == null;
    }

    /** Stream pipeline: adjacent cells -> keep the empty ones -> pick one at random. */
    public Optional<Location> freeAdjacentLocation(Location location) {
        List<Location> free = adjacentLocations(location).stream()
                .filter(this::isFree)
                .collect(Collectors.toList());
        return free.isEmpty()
                ? Optional.empty()
                : Optional.of(free.get(rand.nextInt(free.size())));
    }

    /** All in-bounds neighbours of a cell, built with nested IntStreams and flatMap. */
    public List<Location> adjacentLocations(Location location) {
        return IntStream.rangeClosed(-1, 1).boxed()
                .flatMap(dr -> IntStream.rangeClosed(-1, 1)
                        .filter(dc -> dr != 0 || dc != 0)
                        .mapToObj(dc -> new Location(location.getRow() + dr, location.getCol() + dc)))
                .filter(this::isValidLocation)
                .collect(Collectors.toList());
    }

    /** A random empty cell anywhere on the grid, if one exists after a few tries. */
    public Optional<Location> randomFreeLocation() {
        return Stream.generate(() -> new Location(rand.nextInt(height), rand.nextInt(width)))
                .limit(50)
                .filter(this::isFree)
                .findFirst();
    }

    public void clear(Location location) {
        checkLocation(location);
        grid[location.getRow()][location.getCol()] = null;
    }

    public void removeActor(Actor actor) {
        clear(actor.getLocation());
        actors.remove(actor);
    }

    /** Iterator pattern: callers can traverse actors but can't add or remove them. */
    @Override
    public Iterator<Actor> iterator() {
        return Collections.unmodifiableList(actors).iterator();
    }

    /** A stream over the current actors, for stats and drawing. */
    public Stream<Actor> stream() {
        return actors.stream();
    }

    /** A snapshot copy, so actors can be born and die while we loop over this one. */
    public List<Actor> snapshot() {
        return new ArrayList<>(actors);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
