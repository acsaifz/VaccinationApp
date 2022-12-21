package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Vaccination;

import java.util.List;

public interface VaccinationRepository {
    List<Vaccination> findSuccessfulVaccinationsByCitizenId(long citizenId);

    void save(Vaccination vaccination, long citizenId);
}
