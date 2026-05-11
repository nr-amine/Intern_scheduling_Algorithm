import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Intern implements Comparable<Intern> {
    private String nom;
    private double score;
    private LocalDate lastDayWorked;
    private List<LocalDate> jours_conges;

    public Intern(String nom, LocalDate ldw) {
        this(nom, new ArrayList<>(), ldw);
    }

    public Intern(String nom, List<LocalDate> c) {
        this(nom, c, LocalDate.MIN);
    }

    public Intern(String nom, List<LocalDate> c, LocalDate ldw) {
        this.nom = nom;
        this.score = 0;
        this.lastDayWorked = ldw;
        this.jours_conges = (c != null) ? c : new ArrayList<>();
    }

    public boolean enConge(LocalDate date) {
        return jours_conges.contains(date);
    }

    public String getNom() {
        return nom;
    }

    public double getScore() {
        return score;
    }

    public LocalDate getLastDayWorked() {
        return lastDayWorked;
    }

    public void effectuerGarde(LocalDate date, double points) {
        this.score += points;
        this.lastDayWorked = date;
    }

    @Override
    public int compareTo(Intern other) {
        return Double.compare(this.score, other.score);
    }

    // Evite de selectionner le meme interne 2 fois
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intern intern = (Intern) o;
        return Objects.equals(nom, intern.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    @Override
    public String toString() {
        return this.nom;
    }
}