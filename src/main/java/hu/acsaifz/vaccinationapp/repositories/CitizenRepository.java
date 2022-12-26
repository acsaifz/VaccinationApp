package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.models.Report;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CitizenRepository {
    long save(Citizen citizen);

    void saveAll(List<Citizen> citizens);

    List<Citizen> findAll();

    List<Citizen> findCitizensByZipCodeForDailyVaccinations(String zipCode);

    Optional<Citizen> findCitizenBySsn(String ssn);

    Map<String, Report> getAllVaccinatedCitizensCountCategorizedByVaccinationsCount();
}
