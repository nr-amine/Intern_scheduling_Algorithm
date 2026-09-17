# InternCal: Hospital Shift Scheduler

A simple Java tool to schedule hospital shifts (*gardes*) fairly among interns over a semester. I wrote this because scheduling shifts by hand is tedious, and it's easy to accidentally assign someone back-to-back shifts or ignore someone's vacation days.

---

## How the Assignment Logic Works

For every shift day, the scheduler follows three straightforward priority rules:

1. **Check vacation leaves:** Skips anyone who is on approved leave that day (checked in $O(1)$ using a `HashSet`).
2. **Balance total shifts:** Always prioritizes whoever has worked the *least* number of shifts so far.
3. **Respect rest time:** If multiple interns have the same shift count, it picks whoever has rested the longest (earliest `lastDayWorked`).
4. **Weekly pool with backup:** Interns are picked from a weekly pool to spread duties across the month. If too many people are on vacation that week and the weekly pool runs dry, it grabs someone from the main cohort who has the lowest shift count.

---

## Compiling and Running

### Prerequisites
* JDK 11 or higher

### Build and Run

```bash
# Compile
javac *.java

# Run the 12-week benchmark
java InternCal
```

### Sample Output

Running the 12-week simulation with 10 interns and staggered leaves:

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

## Known Limitations

* **Greedy approach without backtracking:** The algorithm assigns days one by one from start to finish. If constraints are heavily over-constrained (e.g. half the cohort goes on leave at the same time), it prints an alert rather than undoing earlier choices.
* **Equal shift points:** Right now every shift is worth 1 point. Weekend or night shifts aren't weighted differently yet.
