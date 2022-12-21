package hu.acsaifz.vaccinationapp.services;

import hu.acsaifz.vaccinationapp.models.Vaccine;
import hu.acsaifz.vaccinationapp.repositories.VaccineRepository;
import hu.acsaifz.vaccinationapp.repositories.VaccineRepositoryImpl;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class VaccineService {
    private final VaccineRepository vaccineRepository;

    public VaccineService(DataSource dataSource){
        vaccineRepository = new VaccineRepositoryImpl(dataSource);
    }

    public List<Vaccine> findAll(){
        return vaccineRepository.findAll();
    }

    public Optional<Vaccine> findById(long id){
        return vaccineRepository.findById(id);
    }
}
