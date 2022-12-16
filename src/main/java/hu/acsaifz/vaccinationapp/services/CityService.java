package hu.acsaifz.vaccinationapp.services;

import hu.acsaifz.vaccinationapp.repositories.CityRepository;
import hu.acsaifz.vaccinationapp.repositories.CityRepositoryImpl;

import javax.sql.DataSource;
import java.util.List;

public class CityService {
    private final CityRepository cityRepository;

    public CityService(DataSource dataSource){
        cityRepository = new CityRepositoryImpl(dataSource);
    }

    public List<String> findCityByZipCode(String zipCode){
        return cityRepository.findCityByZipCode(zipCode);
    }
}
