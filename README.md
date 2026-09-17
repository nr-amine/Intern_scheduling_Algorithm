# InternCal: Hospital Shift Rotation Scheduler

A priority-based heuristic scheduling tool written in Java to automate the assignment of hospital intern duties (*gardes* and on-call rotations) over a multi-week semester. Built to eliminate manual scheduling errors, respect planned vacation leaves, and equalize cumulative workload.

---

## How the Algorithm Works

Rather than random assignment, the scheduler applies a deterministic multi-criteria greedy heuristic for every duty date:

1. **Hard Availability Constraint (O(1)):** Filters out any intern on approved leave (`enConge`) using `HashSet<LocalDate>` lookups.
2. **Primary Objective (Workload Balancing):** Prioritizes interns with the lowest cumulative duty count (`score`) to minimize overall workload variance.
3. **Secondary Objective (Rest Period Maximization):** In case of equal scores, tie-breaks in favor of the intern with the earliest `lastDayWorked`, preventing consecutive-day fatigue.
4. **Weekly Pool & Gap-Filling Fallback:** 
   * Each week initializes a fresh pool of interns to encourage balanced weekly rotation.
   * If a week's pool is depleted due to multiple overlapping leaves, the scheduler automatically draws from the global cohort (giving a shift to the intern with the lowest overall score).

---

## Compiling and Running

### Prerequisites
* Java Development Kit (JDK 11 or higher)

### Build and Run

```bash
# Compile all source files
javac *.java

# Execute the simulation
java InternCal
```

### Sample Output

Running the included 12-week benchmark with 10 interns and staggered leaves:

```text
=== Starting Schedule Generation ===
Period: 2026-02-02 to 2026-04-26 | Shift size: 2 | Cycle: every 1 day(s)

--- Semaine 1 ---
   Jour 2026-02-02 (MONDAY): Dr. Robert & Dr. Richard
   Jour 2026-02-03 (TUESDAY): Dr. Durand & Dr. Dupont
   ...
--- Semaine 12 ---
   Jour 2026-04-24 (FRIDAY): Dr. Dupont & Dr. Moreau

=== Final Workload Distribution ===
   Intern Dr. Laurent     | Shifts: 11 | Last worked: 2026-04-16
   Intern Dr. Martin      | Shifts: 12 | Last worked: 2026-04-21
   Intern Dr. Bernard     | Shifts: 12 | Last worked: 2026-04-21
   Intern Dr. Thomas      | Shifts: 12 | Last worked: 2026-04-22
   Intern Dr. Petit       | Shifts: 12 | Last worked: 2026-04-22
   Intern Dr. Robert      | Shifts: 12 | Last worked: 2026-04-20
   Intern Dr. Richard     | Shifts: 12 | Last worked: 2026-04-23
   Intern Dr. Durand      | Shifts: 12 | Last worked: 2026-04-23
   Intern Dr. Moreau      | Shifts: 12 | Last worked: 2026-04-24
   Intern Dr. Dupont      | Shifts: 13 | Last worked: 2026-04-24
----------------------------------------
Moyenne:     12.00 gardes/interne
Ecart type:  0.4714
```

---

## Known Limitations & Design Trade-offs

* **Greedy Heuristic vs. Global CSP Solver:** The algorithm assigns shifts chronologically without backtracking. In heavily over-constrained edge cases (e.g., more than half the cohort taking simultaneous leave), it flags an alert rather than searching a combinatorial tree to backtrack earlier assignments.
* **Uniform Shift Weighting:** Currently scores all shifts equally ($1.0$ point). Extending to differential weights (e.g., weekend shifts worth $1.5$ points, holidays worth $2.0$) would require parameterizing the shift scoring model.
