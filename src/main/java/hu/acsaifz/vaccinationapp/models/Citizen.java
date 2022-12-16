package hu.acsaifz.vaccinationapp.models;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Citizen {
    private long id;
    private String name;
    private String zipCode;
    private int age;
    private String email;
    private String ssn;
    private final SortedSet<Vaccination> vaccinations = new TreeSet<>(Comparator.comparing(Vaccination::getVaccinationDate));


    public Citizen(String name, String zipCode, int age, String email, String ssn) {
        this.name = name;
        this.zipCode = zipCode;
        this.age = age;
        this.email = email;
        this.ssn = ssn;
    }

    public Citizen(long id, String name, String zipCode, int age, String email, String ssn) {
        this(name, zipCode, age, email, ssn);
        this.id = id;
    }

    public void addVaccinations(Collection<Vaccination> vaccinations){
        this.vaccinations.addAll(vaccinations);
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getSsn() {
        return ssn;
    }

    public SortedSet<Vaccination> getVaccinations() {
        return new TreeSet<>(vaccinations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Citizen citizen = (Citizen) o;

        return ssn.equals(citizen.ssn);
    }

    @Override
    public int hashCode() {
        return ssn.hashCode();
    }
}
