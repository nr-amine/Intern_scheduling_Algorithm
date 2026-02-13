
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Intern implements Comparable<Intern> {
    private String nom;
    private double score;
    private LocalDate lastDayWorked;
    private List<LocalDate> jours_conges; 

    public Intern(String nom, List<LocalDate> c) {
        this.nom = nom;
        this.score = 0;
        this.lastDayWorked = LocalDate.MIN;
        jours_conges = c;
    }
    public Intern(String nom, List<LocalDate> c, LocalDate ldw) {
        this.nom = nom;
        this.score = 0;
        this.lastDayWorked = ldw;
        jours_conges = c;
    }

    public Intern(String nom, LocalDate ldw) {
        this.nom = nom;
        this.score = 0;
        this.lastDayWorked = ldw;
        jours_conges = new ArrayList<LocalDate>();
    }

    public boolean enConge(LocalDate date) {
        return jours_conges != null && jours_conges.contains(date);
    }

    public String getNom() {
        return nom;
    }

    public double getScore() {
        return score;
    }

    public void addScore(double value) {
        this.score += value;
    }

    public LocalDate getLastDayWorked() {
        return lastDayWorked;
    }

    public void setLastDayWorked(LocalDate lastDayWorked) {
        this.lastDayWorked = lastDayWorked;
    }

    @Override
    public int compareTo(Intern other) {
        return Double.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return this.nom;
    }

}