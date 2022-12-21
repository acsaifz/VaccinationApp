package hu.acsaifz.vaccinationapp.services;

import hu.acsaifz.vaccinationapp.models.Vaccination;
import hu.acsaifz.vaccinationapp.repositories.VaccinationRepository;
import hu.acsaifz.vaccinationapp.repositories.VaccinationRepositoryImpl;

import javax.sql.DataSource;
import java.util.List;

public class VaccinationService {
    private final VaccinationRepository vaccinationRepository;

    public VaccinationService(DataSource dataSource) {
        vaccinationRepository = new VaccinationRepositoryImpl(dataSource);
    }

    public List<Vaccination> findSuccessfulVaccinationsByCitizenId(long citizenId){
        return vaccinationRepository.findSuccessfulVaccinationsByCitizenId(citizenId);
    }

    public void save(Vaccination vaccination, long citizenId){
        vaccinationRepository.save(vaccination, citizenId);
    }
}
