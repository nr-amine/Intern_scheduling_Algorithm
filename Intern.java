import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Intern implements Comparable<Intern> {
    private String nom;
    private double score;
    private LocalDate lastDayWorked;
    private Set<LocalDate> jours_conges;

    public Intern(String nom, LocalDate ldw) {
        this(nom, new HashSet<>(), ldw);
    }

    public Intern(String nom, Collection<LocalDate> c) {
        this(nom, c, LocalDate.MIN);
    }

    public Intern(String nom, Collection<LocalDate> c, LocalDate ldw) {
        this.nom = nom;
        this.score = 0;
        this.lastDayWorked = (ldw != null) ? ldw : LocalDate.MIN;
        this.jours_conges = (c != null) ? new HashSet<>(c) : new HashSet<>();
    }

    public boolean enConge(LocalDate date) {
        return jours_conges.contains(date);
    }

    public void ajouterConge(LocalDate date) {
        jours_conges.add(date);
    }

    public void ajouterConges(Collection<LocalDate> dates) {
        if (dates != null) {
            jours_conges.addAll(dates);
        }
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
        if (other == null) return 1;
        int cmp = Double.compare(this.score, other.score);
        if (cmp != 0) return cmp;
        return this.nom.compareTo(other.nom);
    }

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