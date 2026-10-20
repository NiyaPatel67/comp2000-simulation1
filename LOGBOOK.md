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
- **What went wrong / confused me:** The graph made the crash from Week 10 visible: rabbits boom at first, eat most of the plants, then crash, and the foxes starve shortly after — everything dies within about 50 steps. I also had to work out why `flatMap` was needed to build the neighbour list (two nested ranges of row/column offsets flattened into one stream of `Location`s).
- **Connection to project:** The population graph is my "watchable emergent behaviour" for the uniqueness criterion, and it shows exactly what I need to fix in Week 12: the populations need to cycle instead of collapsing.

**Signed:** Niya Patel