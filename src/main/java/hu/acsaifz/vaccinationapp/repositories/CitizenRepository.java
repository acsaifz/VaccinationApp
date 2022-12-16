package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Citizen;

import java.util.List;

public interface CitizenRepository {
    void save(Citizen citizen);

    void saveAll(List<Citizen> citizens);

    List<Citizen> findAll();
}
