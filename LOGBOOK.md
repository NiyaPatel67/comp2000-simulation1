# Log Book — COMP2000 Worksheet 2 (Weeks 7–13)

## 17/09/2026 — Week 8: Understanding the concepts and planning the design

- **What I did:** Reviewed my Week 7 feedback (54/100: missing class diagram, missing JFrame/JPanel/Exception inheritance, commits bunched on two days, only one log entry). Studied the Week 13 rubric and the course topics, then planned a 3-week design: Strategy (week 1), Observer + Iterator + streams with a live population graph (week 2), State + collectors + parallel stats (week 3).
- **Course topic:** Strategy, Observer, Decorator, Iterator and State patterns; lambdas, stream pipelines and collectors. I compared Strategy against Template Method for removing the Fox/Rabbit duplication and chose Strategy because diet is a swappable behaviour (composition over inheritance). I decided not to force in Decorator, as nothing in my simulation naturally needs it.
- **What confused me:** Whether to start a new simulation topic for better marks. I decided to extend my existing ecosystem so my log book and code connect across the semester. Working out where parallel streams could be used safely was also tricky: the main act() loop changes the shared grid, so only read-only work like statistics can run in parallel.
- **Connection to project:** This plan directly addresses my Week 7 feedback: commit and log on several days each week, and produce a proper UML pattern diagram. Claude (AI) helped me plan and compare the options.

**Signed:** Niya Patel

## 15/10/2026 — Week 10: Strategy pattern refactor

- **What I did:** Replaced Fox's `huntRabbits()` and Rabbit's `eatPlant()` with one shared `eatNearbyFood()` in `Animal`, driven by a new `FeedingStrategy` interface. Fox and Rabbit now pass their diet in as a lambda (`food -> food instanceof Rabbit`), cutting each class to about 20 lines. Claude (AI) helped plan and write the refactor; I reviewed, ran and committed it.
- **Course topic:** Strategy pattern (composition over inheritance — an Animal *has* a diet rather than overriding a method for it), functional interfaces and lambdas. I considered Template Method as the alternative but chose Strategy because the diet is a swappable behaviour, not a fixed step.
- **What went wrong / confused me:** Animals that died of age or hunger were never removed from the grid, so dead rabbits blocked cells forever. Fixing that made the ecosystem collapse within ~40 steps instead. I also copied the new files in while still on `main` before creating my branch, and VS Code showed errors in `Main.java` even though `javac` compiled it fine (stale language server).
- **Connection to project:** This fixes the Fox/Rabbit duplication I flagged in my Week 7 worksheet, and the population crash is the problem I'll make visible next week with the Observer pattern and a population graph.

**Signed:** Niya Patel