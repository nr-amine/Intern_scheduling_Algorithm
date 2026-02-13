import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class InternCal {
   private ArrayList<Intern> tab = new ArrayList<>();
    private int globalDayCounter = 0;

    public int getDay() {
        return globalDayCounter;
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


    public static void main(String[] args) {
       InternCal app = new InternCal();

// Define the Date Ranges based on your text list
List<LocalDate> feb18_25 = getDateRange(LocalDate.of(2026, 2, 17), LocalDate.of(2026, 2, 25));
List<LocalDate> mar02_08 = getDateRange(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 8));
List<LocalDate> mar08_18 = getDateRange(LocalDate.of(2026, 3, 8), LocalDate.of(2026, 3, 18));
List<LocalDate> mar18_28 = getDateRange(LocalDate.of(2026, 3, 18), LocalDate.of(2026, 3, 28));

// Group 1: 18/02 - 25/02
app.tab.add(new Intern("Nouar", feb18_25, LocalDate.of(2026, 2, 5)));
app.tab.add(new Intern("Benkhlifa", feb18_25, LocalDate.of(2026, 2, 5)));
app.tab.add(new Intern("Bekhti", feb18_25, LocalDate.of(2026, 2, 5)));

// Group 2: 02/03 – 08/03
app.tab.add(new Intern("Amina", mar02_08, LocalDate.of(2026, 2, 5)));

// Group 3: 08/03 - 18/03
app.tab.add(new Intern("Mohamed", mar08_18, LocalDate.of(2026, 2, 3)));
app.tab.add(new Intern("Asmaa", mar08_18, LocalDate.of(2026, 2, 11)));
app.tab.add(new Intern("Wiam", mar08_18, LocalDate.of(2026, 2, 3)));

// Group 4: 18/03 - 28/03
app.tab.add(new Intern("Fares", mar18_28, LocalDate.of(2026, 2, 11)));
app.tab.add(new Intern("Kouadri", mar18_28, LocalDate.of(2026, 2, 11)));
app.tab.add(new Intern("Ghizlen", mar18_28, LocalDate.of(2026, 2, 11)));



        System.out.println("Start");
        for (int week = 1; week <= 7; week++) {
            System.out.println("\nSemaine " + week);

            List<Intern> semPisc = new ArrayList<>(app.tab);
            
            for (int day = 1; day <= 7; day++) {
                app.globalDayCounter++;
                LocalDate startDate = LocalDate.of(2026, 2, 17);
                LocalDate today = startDate.plusDays(app.globalDayCounter - 1);

                if (app.globalDayCounter % 6 != 1) {
                    continue;
                }

                if (today.getDayOfWeek() == DayOfWeek.FRIDAY || today.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    continue;
                }
                if (today.compareTo(LocalDate.of(2026, 4, 1)) >= 0) {break;}

                semPisc.sort(Comparator.comparingDouble(Intern::getScore).thenComparing(Intern::getLastDayWorked));

                Intern i1 = null;
                Intern i2 = null;
                Intern i3 = null;

                Iterator<Intern> it = semPisc.iterator();
                while (it.hasNext()) {
                    Intern candidate = it.next();
                    if (!candidate.enConge(today)) {
                        i1 = candidate;
                        it.remove();
                        break;
                    }
                }

                for (int i = 0; i < semPisc.size(); i++) {
                    Intern candidate = semPisc.get(i);
                    if (i1 != null && !candidate.getNom().equals(i1.getNom()) && !candidate.enConge(today)) {
                        i2 = candidate;
                        semPisc.remove(i);
                        break;
                    }
                }

                for (int i = 0; i < semPisc.size(); i++) {
                    Intern candidate = semPisc.get(i);
                    if (i1 != null && i2 != null && !candidate.getNom().equals(i1.getNom()) && !candidate.getNom().equals(i2.getNom()) && !candidate.enConge(today)) {
                        i3 = candidate;
                        semPisc.remove(i);
                        break;
                    }
                }

                if (i1 != null && i2 != null && i3 != null) {
                    i1.addScore(1);
                    i1.setLastDayWorked(today);

                    i2.addScore(1);
                    i2.setLastDayWorked(today);

                    i3.addScore(1);
                    i3.setLastDayWorked(today);

                    System.out.println("   Jour " + today + ": " + i1.getNom() + " & " + i2.getNom() + " & " + i3.getNom());
                }
            }
        }

        System.out.println("\nFinal score");
        app.tab.sort((a, b) -> Double.compare(a.getScore(), b.getScore()));
        
        double sum = 0;
        double sumSq = 0;

        for (Intern i : app.tab) {
            System.out.println("Intern " + i.getNom() + ", Gardes: " + (int)i.getScore());
            sum += i.getScore();
        }
        
        double moy = sum / app.tab.size();
        for (Intern i : app.tab) {
            sumSq += Math.pow(i.getScore() - moy, 2);
        }
        double ecartType = Math.sqrt(sumSq / (app.tab.size() - 1));

        System.out.printf("Moyenne: %.2f\n", moy);
        System.out.printf("Ecart type: %.4f\n", ecartType);
    }
}