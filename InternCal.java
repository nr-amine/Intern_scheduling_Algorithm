import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InternCal {
    private List<Intern> interns = new ArrayList<>();
    private Set<DayOfWeek> weekendDays = new HashSet<>(Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
    
    public void addIntern(Intern intern) {
        this.interns.add(intern);
    }

    public void setWeekendDays(Set<DayOfWeek> weekendDays) {
        this.weekendDays = (weekendDays != null) ? weekendDays : Collections.emptySet();
    }

    public static List<LocalDate> getDateRange(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }

    public void generateSchedule(LocalDate startDate, LocalDate endDate, int shiftSize, int cycleLength) {
        System.out.println("=== Starting Schedule Generation ===");
        System.out.printf("Period: %s to %s | Shift size: %d | Cycle: every %d day(s)\n\n",
                startDate, endDate, shiftSize, cycleLength);
        
        int weekNumber = 1;
        int globalDayCounter = 1;
        LocalDate currentWeekStart = startDate;

        while (!currentWeekStart.isAfter(endDate)) {
            System.out.println("--- Semaine " + weekNumber + " ---");
            
            // Pool of interns available for this week
            List<Intern> semPisc = new ArrayList<>(this.interns);

            for (int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++) {
                LocalDate today = currentWeekStart.plusDays(dayOfWeek);
                if (today.isAfter(endDate)) break;

                boolean isCycleDay = (cycleLength <= 1) || (globalDayCounter % cycleLength == 1);
                boolean isWeekend = weekendDays.contains(today.getDayOfWeek());

                if (isCycleDay && !isWeekend) {
                    assignShift(today, semPisc, shiftSize);
                }
                
                globalDayCounter++;
            }
            
            currentWeekStart = currentWeekStart.plusDays(7);
            weekNumber++;
        }
    }

    private void assignShift(LocalDate today, List<Intern> semPisc, int shiftSize) {
        // 1. Select available interns from the current weekly pool, prioritizing lowest score then longest rest
        List<Intern> selectedShift = semPisc.stream()
                .filter(candidate -> !candidate.enConge(today))
                .sorted(Comparator.comparingDouble(Intern::getScore).thenComparing(Intern::getLastDayWorked))
                .distinct()
                .limit(shiftSize)
                .collect(Collectors.toList());

        // 2. Fallback: if the weekly pool is depleted, fill gaps from the global cohort (giving a double shift to lowest score)
        if (selectedShift.size() < shiftSize) {
            final List<Intern> alreadySelected = new ArrayList<>(selectedShift);
            List<Intern> fallbacks = this.interns.stream()
                    .filter(c -> !c.enConge(today) && !alreadySelected.contains(c))
                    .sorted(Comparator.comparingDouble(Intern::getScore).thenComparing(Intern::getLastDayWorked))
                    .limit(shiftSize - selectedShift.size())
                    .collect(Collectors.toList());
            selectedShift.addAll(fallbacks);
        }

        if (selectedShift.size() == shiftSize) {
            for (Intern i : selectedShift) {
                i.effectuerGarde(today, 1);
                semPisc.remove(i); // Remove from weekly pool to minimize back-to-back shifts in the same week
            }
            
            String teamNames = selectedShift.stream()
                    .map(Intern::getNom)
                    .collect(Collectors.joining(" & "));
                    
            System.out.println("   Jour " + today + " (" + today.getDayOfWeek() + "): " + teamNames);
        } else {
            System.out.println("   Jour " + today + " (" + today.getDayOfWeek() + "): [ALERTE] Pas assez d'internes disponibles ! (" 
                    + selectedShift.size() + "/" + shiftSize + ")");
        }
    }

    public void printStatistics() {
        System.out.println("\n=== Final Workload Distribution ===");
        
        interns.sort(Comparator.comparingDouble(Intern::getScore));
        
        double sum = 0;
        for (Intern i : interns) {
            System.out.printf("   Intern %-15s | Shifts: %2d | Last worked: %s\n",
                    i.getNom(), (int) i.getScore(), i.getLastDayWorked());
            sum += i.getScore();
        }
        
        if (interns.isEmpty()) return;

        double moy = sum / interns.size();
        double sumSq = interns.stream()
                .mapToDouble(i -> Math.pow(i.getScore() - moy, 2))
                .sum();
                
        double ecartType = interns.size() > 1 ? Math.sqrt(sumSq / (interns.size() - 1)) : 0.0;

        System.out.println("----------------------------------------");
        System.out.printf("Moyenne:     %.2f gardes/interne\n", moy);
        System.out.printf("Ecart type:  %.4f\n", ecartType);
    }

    public static void main(String[] args) {
        InternCal app = new InternCal();

        // 12-week semester simulation
        LocalDate semesterStart = LocalDate.of(2026, 2, 2);
        LocalDate semesterEnd = LocalDate.of(2026, 4, 26);

        // Cohort of 10 interns with staggered vacation leaves
        app.addIntern(new Intern("Dr. Dupont", getDateRange(LocalDate.of(2026, 2, 16), LocalDate.of(2026, 2, 22)), LocalDate.of(2026, 1, 28)));
        app.addIntern(new Intern("Dr. Martin", getDateRange(LocalDate.of(2026, 2, 23), LocalDate.of(2026, 3, 1)), LocalDate.of(2026, 1, 29)));
        app.addIntern(new Intern("Dr. Bernard", getDateRange(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 8)), LocalDate.of(2026, 1, 30)));
        app.addIntern(new Intern("Dr. Thomas", getDateRange(LocalDate.of(2026, 3, 9), LocalDate.of(2026, 3, 15)), LocalDate.of(2026, 1, 31)));
        app.addIntern(new Intern("Dr. Petit", getDateRange(LocalDate.of(2026, 3, 16), LocalDate.of(2026, 3, 22)), LocalDate.of(2026, 2, 1)));
        app.addIntern(new Intern("Dr. Robert", getDateRange(LocalDate.of(2026, 3, 23), LocalDate.of(2026, 3, 29)), LocalDate.of(2026, 1, 25)));
        app.addIntern(new Intern("Dr. Richard", getDateRange(LocalDate.of(2026, 3, 30), LocalDate.of(2026, 4, 5)), LocalDate.of(2026, 1, 26)));
        app.addIntern(new Intern("Dr. Durand", getDateRange(LocalDate.of(2026, 4, 6), LocalDate.of(2026, 4, 12)), LocalDate.of(2026, 1, 27)));
        app.addIntern(new Intern("Dr. Moreau", getDateRange(LocalDate.of(2026, 4, 13), LocalDate.of(2026, 4, 19)), LocalDate.of(2026, 1, 28)));
        app.addIntern(new Intern("Dr. Laurent", getDateRange(LocalDate.of(2026, 4, 20), LocalDate.of(2026, 4, 26)), LocalDate.of(2026, 1, 29)));

        // Generate schedule: 2 interns per duty day, operating every weekday
        app.generateSchedule(semesterStart, semesterEnd, 2, 1);
        app.printStatistics();
    }
}