# Log Book — COMP2000 Worksheet 2 (Weeks 7–13)

## 7/10/2026 — Week 9: Understanding the concepts and planning the design

- **What I did:** Reviewed my last project feedback (54/100: missing class diagram, missing JFrame/JPanel/Exception inheritance, commits bunched on two days, only one log entry). Studied the Week 13 rubric and the course topics, then planned a 3-week design: Strategy (week 1), Observer + Iterator + streams with a live population graph (week 2), State + collectors + parallel stats (week 3).
- **Course topic:** Strategy, Observer, Decorator, Iterator and State patterns; lambdas, stream pipelines and collectors. I compared Strategy against Template Method for removing the Fox/Rabbit duplication and chose Strategy because diet is a swappable behaviour (composition over inheritance). I decided not to force in Decorator, as nothing in my simulation naturally needs it.
- **What confused me:** Whether to start a new simulation topic for better marks. I decided to extend my existing ecosystem so my log book and code connect across the semester. Working out where parallel streams could be used safely was also tricky: the main act() loop changes the shared grid, so only read-only work like statistics can run in parallel.
- **Connection to project:** This plan directly addresses my Week 7 feedback: commit and log on several days each week, and produce a proper UML pattern diagram. Claude (AI) helped me plan and compare the options.

**Signed:** Niya Patel


## 15/10/2026 — Week 10: Strategy pattern refactor
git push

- **What I did:** Replaced Fox's `huntRabbits()` and Rabbit's `eatPlant()` with one shared `eatNearbyFood()` in `Animal`, driven by a new `FeedingStrategy` interface. Fox and Rabbit now pass their diet in as a lambda (`food -> food instanceof Rabbit`), cutting each class to about 20 lines. Claude (AI) helped plan and write the refactor; I reviewed, ran and committed it on a feature branch and merged it with PR #4.
- **Course topic:** Strategy pattern (composition over inheritance — an Animal *has* a diet rather than overriding a method for it), functional interfaces and lambdas.
- **What went wrong / confused me:** Animals that died of age or hunger were never removed from the grid, so dead rabbits blocked cells forever. Fixing that made the ecosystem collapse within ~40 steps instead. I also copied the new files in while still on `main` before creating my branch, and when I edited LOGBOOK.md on `main` before pulling the merged PR, git refused to pull until I saved my changes elsewhere — this overwrote an entry and I had to fix the log book afterwards.
- **Connection to project:** This fixes the Fox/Rabbit duplication I flagged in my Week 7 worksheet, and the population crash is the problem I'll make visible in Week 11 with the Observer pattern and a population graph.

**Signed:** Niya Patel


## 20/10/2026 — Week 11: Observer pattern, Iterator and stream pipelines

- **What I did:** Added a `Simulator` class that runs each step and notifies a list of `SimulationObserver`s. `SimulatorView`, a new `PopulationGraph` panel and a `ConsoleLogger` all register as observers, so the window, live fox/rabbit/plant graph and console report update after every step. Made `Field` implement `Iterable<Actor>` and rewrote `adjacentLocations()` and `freeAdjacentLocation()` as stream pipelines. Claude (AI) helped write the code; I ran it, committed it on `feature/observer-graph` and opened a PR.
- **Course topic:** Observer pattern (one-to-many notification without tight coupling — `Simulator` doesn't know what its observers are), Iterator pattern (`FieldCanvas` loops `for (Actor a : field)` without seeing the internal list), stream pipelines (`filter`, `flatMap`, `collect`), `groupingBy` with `counting()` in `ConsoleLogger`, and method references like `this::isFree` and `PopulationStats::rabbits`.
- **What went wrong / confused me:** The graph made the crash from Week 10 visible: every fox and rabbit was dead before step 50. After that, plants spread unchecked, from 226 at step 50 to over 2,500 by step 300, because nothing was left to eat them. I also had to work out why `flatMap` was needed to build the neighbour list (two nested ranges of row/column offsets flattened into one stream of `Location`s).
- **Connection to project:** The population graph is my "watchable emergent behaviour" for the uniqueness criterion, and it shows exactly what I need to fix in Week 12: the populations need to cycle instead of collapsing.

**Signed:** Niya Patel
## 22/10/2026 — Week 11: Testing loose coupling in the Observer pattern

- **What I did:** Commented out the line that registers `ConsoleLogger` as an observer in `Main`, then re-ran the simulation. The window and population graph kept updating normally and only the console reports stopped. I didn't have to change `Simulator.java` at all. Then I restored the line and merged the Week 11 pull request (#5).
- **Course topic:** Observer pattern — one-to-many notification without tight coupling. `Simulator` only knows about the `SimulationObserver` interface and loops `observers.forEach(o -> o.onStep(...))`, so observers can be added or removed without touching the subject.
- **What confused me:** At first I expected removing an observer to break something in `Simulator`, because the console output used to come from inside the simulation loop. It helped to see that the subject doesn't care how many observers there are, or what they do.
- **Connection to project:** This is my main evidence for worksheet question 1.3 (why Observer over calling the view directly). It also means in Week 12 I can add new statistics without editing `Simulator`'s loop.

**Signed:** Niya Patel

## 27/10/2026 — Week 12: State pattern for hungry and fed animals

- **What I did:** Added an `AnimalState` interface with two states, `HungryState` (look for food, otherwise wander; no breeding) and `FedState` (wander and breed). `Animal.act()` now updates its state from its food level and calls `state.act(this, field)` instead of running one fixed sequence. `FieldCanvas` draws hungry rabbits paler so the state is visible on screen. Claude (AI) helped write the code; I ran it and committed it on `feature/state-pattern`.
- **Course topic:** State pattern — an object changes its behaviour when its internal state changes. Both states are singletons, since they hold no data of their own.
- **What went wrong / confused me:** I wasn't sure how State differs from Strategy, since both delegate to another object. The difference is who chooses: with Strategy, Fox and Rabbit pick their diet once when they're created; with State, the animal switches its own behaviour every turn as it gets hungry or eats. The ecosystem still crashed within about 100 steps.
- **Connection to project:** Hungry animals no longer breed, which should slow the boom-and-crash I saw in Week 11. The State vs Strategy comparison is useful for worksheet question 1.3.

**Signed:** Niya Patel
## 27/10/2026 — Week 12: Collectors, a custom Collector and parallel stats

- **What I did:** Wrote `PopulationStatsCollector`, a custom `Collector` that counts foxes, rabbits, plants and hungry animals and averages animal age in one pass. `Simulator` now builds each step's stats with `field.stream().parallel().collect(new PopulationStatsCollector())`. `ConsoleLogger` uses `partitioningBy(Animal::isHungry, counting())` for hungry vs fed and `groupingBy(..., summarizingInt(Animal::getAge))` for age stats per species. The status bar shows hungry animals and average age. Also added a few plant seedlings each step. Claude (AI) helped write the code; I ran it and committed it.
- **Course topic:** Collectors — `groupingBy`, `partitioningBy`, downstream collectors, `summarizingInt`, and writing a custom `Collector` (supplier, accumulator, combiner, finisher). Parallel streams and thread safety.
- **What went wrong / confused me:** Understanding why the parallel stream is safe. Each thread gets its own `Tally` from `supplier()`, fills it with `accumulator()`, and the partial tallies are merged by `combiner()`, so nothing is shared between threads. The stats step only *reads* the field. The main `act()` loop can't be parallel, because animals move and remove each other on the shared grid, which would be a race condition. The ecosystem still crashed within about 100 steps.
- **Connection to project:** This is my main evidence for worksheet section 2 (custom collector, parallel stream with thread safety). The extra stats will help me tune the populations on Thursday.

**Signed:** Niya Patel