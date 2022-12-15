package hu.acsaifz.vaccinationapp.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vaccination {
    private long id;
    private LocalDateTime vaccinationDate;
    private VaccinationStatus status;
    private String note;
    private Vaccine vaccine;

    public Vaccination(LocalDateTime vaccinationDate, VaccinationStatus status, String note){
        this.vaccinationDate = vaccinationDate;
        this.status = status;
        this.note = note;
    }

    public Vaccination(LocalDateTime vaccinationDate, VaccinationStatus status, String note, Vaccine vaccine) {
        this(vaccinationDate, status, note);
        this.vaccine = vaccine;
    }

    public Vaccination(long id, LocalDateTime vaccinationDate, VaccinationStatus status, String note, Vaccine vaccine) {
        this(vaccinationDate, status, note, vaccine);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getVaccinationDate() {
        return vaccinationDate;
    }

    public VaccinationStatus getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vaccination that = (Vaccination) o;

        if (!vaccinationDate.equals(that.vaccinationDate)) return false;
        if (status != that.status) return false;
        return Objects.equals(vaccine, that.vaccine);
    }

    @Override
    public int hashCode() {
        int result = vaccinationDate.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + (vaccine != null ? vaccine.hashCode() : 0);
        return result;
    }
}
