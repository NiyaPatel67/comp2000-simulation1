# Log Book — COMP2000 Semester Project

## 15/10/2026 — Week 10: Strategy pattern refactor

- **What I did:** Replaced Fox's `huntRabbits()` and Rabbit's `eatPlant()` with one shared `eatNearbyFood()` in `Animal`, driven by a new `FeedingStrategy` interface. Fox and Rabbit now pass their diet in as a lambda (`food -> food instanceof Rabbit`), cutting each class to about 20 lines. Claude (AI) helped plan and write the refactor; I reviewed, ran and committed it.
- **Course topic:** Strategy pattern (composition over inheritance — an Animal *has* a diet rather than overriding a method for it), functional interfaces and lambdas. I considered Template Method as the alternative but chose Strategy because the diet is a swappable behaviour, not a fixed step.
- **What went wrong / confused me:** Animals that died of age or hunger were never removed from the grid, so dead rabbits blocked cells forever. Fixing that made the ecosystem collapse within ~40 steps instead. I also copied the new files in while still on `main` before creating my branch, and VS Code showed errors in `Main.java` even though `javac` compiled it fine (stale language server).
- **Connection to project:** This fixes the Fox/Rabbit duplication I flagged in my Week 7 worksheet, and the population crash is the problem I'll make visible next week with the Observer pattern and a population graph.

**Signed:** Niya Patel