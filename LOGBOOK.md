# Log Book — COMP2000 Semester Project

## 19/08/2026 — Project setup, core hierarchy, exceptions, GUI

**What I did:**
- Set up Java 21 + VS Code with the Java extension pack, confirmed the toolchain
  worked with a "Hello World" test.
- Created a public GitHub repo (comp2000-simulation1) and got it cloned and
  tracked locally with a proper src/sim package structure and .gitignore.
- Built the core class hierarchy: an abstract Actor class (location, alive state),
  extended by an abstract Animal class (age, hunger), extended by concrete Fox
  and Rabbit classes that each override act() completely differently — Fox hunts
  adjacent rabbits and eats them, Rabbit just moves and breeds.
- Built Field as the grid, using generics (List<Actor>, Optional<Location>) to
  manage actors and find free adjacent cells safely.
- Added exception handling: a custom InvalidLocationException thrown when code
  tries to place/move an actor outside the grid, and constructor validation on
  Field (IllegalArgumentException for non-positive dimensions). Demonstrated
  both with try/catch in Main.
- Built a simple Swing GUI (FieldCanvas extends JPanel, SimulatorView extends
  JFrame) using a javax.swing.Timer to animate the simulation a few times a
  second, per the "watchable simulation" requirement.
- Used feature branches and pull requests on GitHub for the exception-handling
  and GUI work, merging each via PR rather than committing straight to main.

**Problems I hit / what I learned:**
- Ran into `NoClassDefFoundError: wrong name` a few times when running a class
  from inside its own package folder instead of from the source root — learned
  that once a class declares `package sim;`, it has to be run as `sim.ClassName`
  from the parent of `sim/`, not from inside `sim/` itself. VS Code's Run
  codelens handles this automatically, which is why it worked there but not
  from a manually-typed terminal command.
- Accidentally committed the GUI work directly onto main instead of a feature
  branch (forgot to run `git checkout -b` first). Fixed it by branching off
  the existing commit, pushing that branch, then resetting main back to match
  origin — good lesson in how git branches are just pointers to commits, not
  separate copies of files.
- Chose Optional<Location> as the return type for freeAdjacentLocation() instead
  of returning null, so callers are forced to explicitly handle the "no free
  space" case rather than risking a silent NullPointerException later.

**Signed:** Niya Patel