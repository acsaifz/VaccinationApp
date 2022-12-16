package hu.acsaifz.vaccinationapp.repositories;

import java.util.List;

public interface CityRepository {
    List<String> findCityByZipCode(String zipCode);
}
