import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InternCal {
    private List<Intern> interns = new ArrayList<>();
    
    public void addIntern(Intern intern) {
        this.interns.add(intern);
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
        System.out.println("Start");
        
        int weekNumber = 1;
        int globalDayCounter = 1;
        LocalDate currentWeekStart = startDate;

        while (!currentWeekStart.isAfter(endDate)) {
            System.out.println("\nSemaine " + weekNumber);
            
            // Pool d'internes pour la semaine
            List<Intern> semPisc = new ArrayList<>(this.interns);

            for (int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++) {
                LocalDate today = currentWeekStart.plusDays(dayOfWeek);
                
                if (today.isAfter(endDate)) break;

                boolean isCycleDay = (globalDayCounter % cycleLength == 1);
                boolean isWeekend = (today.getDayOfWeek() == DayOfWeek.FRIDAY || today.getDayOfWeek() == DayOfWeek.SATURDAY);

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
        // Selection des internes dispo, tries par score puis par date de derniere garde
        List<Intern> selectedShift = semPisc.stream()
                .filter(candidate -> !candidate.enConge(today))
                .sorted(Comparator.comparingDouble(Intern::getScore).thenComparing(Intern::getLastDayWorked))
                .distinct() 
                .limit(shiftSize)
                .collect(Collectors.toList());

        if (selectedShift.size() == shiftSize) {
            for (Intern i : selectedShift) {
                i.effectuerGarde(today, 1);
                semPisc.remove(i); // Retrait pour eviter 2 gardes la meme semaine
            }
            
            String teamNames = selectedShift.stream()
                    .map(Intern::getNom)
                    .collect(Collectors.joining(" & "));
                    
            System.out.println("   Jour " + today + ": " + teamNames);
        } else {
            System.out.println("   Jour " + today + ": [ALERTE] Pas assez d'internes disponibles ! (" + selectedShift.size() + "/" + shiftSize + ")");
        }
    }

    public void printStatistics() {
        System.out.println("\nFinal score");
        
        interns.sort(Comparator.comparingDouble(Intern::getScore));
        
        double sum = 0;
        for (Intern i : interns) {
            System.out.println("Intern " + i.getNom() + ", Gardes: " + (int) i.getScore());
            sum += i.getScore();
        }
        
        if (interns.isEmpty()) return;

        double moy = sum / interns.size();
        double sumSq = interns.stream()
                .mapToDouble(i -> Math.pow(i.getScore() - moy, 2))
                .sum();
                
        double ecartType = interns.size() > 1 ? Math.sqrt(sumSq / (interns.size() - 1)) : 0.0;

        System.out.printf("Moyenne: %.2f\n", moy);
        System.out.printf("Ecart type: %.4f\n", ecartType);
    }

    public static void main(String[] args) {
        InternCal app = new InternCal();

        List<LocalDate> feb18_25 = getDateRange(LocalDate.of(2026, 2, 17), LocalDate.of(2026, 2, 25));
        List<LocalDate> mar02_08 = getDateRange(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 8));
        List<LocalDate> mar08_18 = getDateRange(LocalDate.of(2026, 3, 8), LocalDate.of(2026, 3, 18));
        List<LocalDate> mar18_28 = getDateRange(LocalDate.of(2026, 3, 18), LocalDate.of(2026, 3, 28));

        app.addIntern(new Intern("Prototype", feb18_25, LocalDate.of(2026, 2, 5)));
        // Add the rest of your interns here...

        LocalDate startDate = LocalDate.of(2026, 2, 17);
        LocalDate endDate = LocalDate.of(2026, 4, 1).minusDays(1); 
        
        app.generateSchedule(startDate, endDate, 3, 6);
        app.printStatistics();
    }
}