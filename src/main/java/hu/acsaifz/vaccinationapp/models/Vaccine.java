package hu.acsaifz.vaccinationapp.models;

public class Vaccine {
    private long id;
    private String name;

    public Vaccine(String name) {
        this.name = name;
    }

    public Vaccine(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
