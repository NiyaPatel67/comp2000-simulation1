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


## 22/10/2026 — Week 11: Testing loose coupling in the Observer pattern

- **What I did:** Commented out the line that registers `ConsoleLogger` as an observer in `Main`, then re-ran the simulation. The window and population graph kept updating normally and only the console reports stopped. I didn't have to change `Simulator.java` at all. Then I restored the line and merged the Week 11 pull request (#5).
- **Course topic:** Observer pattern — one-to-many notification without tight coupling. `Simulator` only knows about the `SimulationObserver` interface and loops `observers.forEach(o -> o.onStep(...))`, so observers can be added or removed without touching the subject.
- **What confused me:** At first I expected removing an observer to break something in `Simulator`, because the console output used to come from inside the simulation loop. It helped to see that the subject doesn't care how many observers there are, or what they do.
- **Connection to project:** This is my main evidence for worksheet question 1.3 (why Observer over calling the view directly). It also means in Week 12 I can add new statistics without editing `Simulator`'s loop.

**Signed:** Niya Patel