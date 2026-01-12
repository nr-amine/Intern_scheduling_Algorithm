import java.util.*;
import java.util.stream.Collectors;

public class InternCal {
   private ArrayList<Intern> tab = new ArrayList<>();
    private PriorityQueue<Intern> queue = new PriorityQueue<>();
    private int globalDayCounter = 0;

    public int getDay() {
        return globalDayCounter;
    }

    public static void main(String[] args) {
        InternCal app = new InternCal();
        for (int i = 0; i < 9; i++) {
            app.tab.add(new Intern(String.valueOf((char)(i + 65))));
        }

        System.out.println("Start");

        for (int week = 1; week <= 12; week++) {
            System.out.println("\nSemaine " + week);

            app.queue.clear();
            app.queue.addAll(app.tab);

            List<Intern> semPisc = new ArrayList<>(app.tab);
            List<Intern> internex = new ArrayList<>();

            for (int k = 0; k < 14-app.tab.size(); k++) {
                Intern i = app.queue.poll();
                if(i != null) {
                    semPisc.add(i);
                    internex.add(i);
                }
            }

            System.out.print("Personnes avec garde double: ");
            for (int k = 0; k < internex.size(); k++) {
                if (k > 0) System.out.print(", ");
                System.out.print(internex.get(k).getNom());
            }
            System.out.println();
            
            for (int day = 1; day <= 7; day++) {
                app.globalDayCounter++;

                if (app.globalDayCounter % 6 == 0) {
                    List<Intern> hdjTeam = app.tab.stream()
                        .sorted(Comparator.comparingInt(Intern::getHdjCount)
                        .thenComparingInt(Intern::getLastHdjDay))
                        .limit(4)
                        .collect(Collectors.toList());

                    System.out.print("   hdj Jour " + day + " (Global " + app.getDay() + "): ");
                    for (Intern h : hdjTeam) {
                        h.addHdjCount();
                        System.out.println(h.getNom() + " | Rest period: " + (app.globalDayCounter - h.getLastHdjDay()));
                        h.setLastHdjDay(app.getDay());
                    }
                    System.out.println();
                }

                semPisc.sort(Comparator.comparingInt(Intern::getLastDayWorked));

                Intern i1 = null;
                Intern i2 = null;

                if (!semPisc.isEmpty()) {
                    i1 = semPisc.remove(0);
                }

                for (int i = 0; i < semPisc.size(); i++) {
                    Intern candidate = semPisc.get(i);
                    if (i1 != null && !candidate.getNom().equals(i1.getNom())) {
                        i2 = candidate;
                        semPisc.remove(i);
                        break;
                    }
                }

                if (i1 != null && i2 != null) {
                    i1.addScore(1);
                    i1.setLastDayWorked(app.globalDayCounter);

                    i2.addScore(1);
                    i2.setLastDayWorked(app.globalDayCounter);

                    System.out.println("   Jour " + day + ": " + i1.getNom() + " & " + i2.getNom());
                }
            }
        }

        System.out.println("\nFinal score");
        app.tab.sort((a, b) -> Double.compare(a.getScore(), b.getScore()));
        
        double sum = 0;
        double sumSq = 0;

        for (Intern i : app.tab) {
            System.out.println("Intern " + i.getNom() + ", Gardes: " + (int)i.getScore() + ", hdg: " + i.getHdjCount());
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