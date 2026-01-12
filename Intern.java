
public class Intern implements Comparable<Intern> {
    private String nom;
    private double score;
    private int hdjCount;
    private int lastHdjDay;
    private int lastDayWorked;

    public Intern(String nom) {
        this.nom = nom;
        this.score = 0;
        this.hdjCount = 0;
        this.lastHdjDay = 0; 
        this.lastDayWorked = 0;
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

    public int getHdjCount() {
        return hdjCount;
    }

    public void addHdjCount() {
        this.hdjCount++;
    }

    public int getLastHdjDay() {
        return lastHdjDay;
    }

    public void setLastHdjDay(int lastHdjDay) {
        this.lastHdjDay = lastHdjDay;
    }

    public int getLastDayWorked() {
        return lastDayWorked;
    }

    public void setLastDayWorked(int lastDayWorked) {
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