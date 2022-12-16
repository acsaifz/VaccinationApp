package hu.acsaifz.vaccinationapp.services;

import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.repositories.CitizenRepository;
import hu.acsaifz.vaccinationapp.repositories.CitizenRepositoryImpl;

import javax.sql.DataSource;

public class CitizenService {
    private final CitizenRepository citizenRepository;

    public CitizenService(DataSource dataSource){
        citizenRepository = new CitizenRepositoryImpl(dataSource);
    }

    public void save(Citizen citizen){
        citizenRepository.save(citizen);
    }

}
