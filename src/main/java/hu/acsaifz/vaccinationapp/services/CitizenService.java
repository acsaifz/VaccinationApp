package hu.acsaifz.vaccinationapp.services;

import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.models.Report;
import hu.acsaifz.vaccinationapp.models.Vaccination;
import hu.acsaifz.vaccinationapp.repositories.CitizenRepository;
import hu.acsaifz.vaccinationapp.repositories.CitizenRepositoryImpl;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

public class CitizenService {
    private static final String CSV_SEPARATOR = ";";
    private static final String VACCINATION_PLAN_CSV_HEADER = "Időpont;Név;Irányítószám;Életkor;E-mail cím;TAJ szám\n";
    private final CitizenRepository citizenRepository;
    private final VaccinationService vaccinationService;

    public CitizenService(DataSource dataSource){
        citizenRepository = new CitizenRepositoryImpl(dataSource);
        vaccinationService = new VaccinationService(dataSource);
    }

    public void save(Citizen citizen){
        long citizenId = citizenRepository.save(citizen);
        if (citizen.getNumberOfVaccination() > 0){
            SortedSet<Vaccination> vaccinations = citizen.getVaccinations();
            vaccinations.forEach(vaccination -> vaccinationService.save(vaccination, citizenId));
        }
    }

    public void saveAll(List<Citizen> citizens){
        citizenRepository.saveAll(citizens);
    }

    public List<Citizen> findAll(){
        return citizenRepository.findAll();
    }

    public void massRegistration(Path path){
        List<Citizen> citizens = this.loadCitizens(path);
        this.saveAll(citizens);
    }

    private List<Citizen> loadCitizens(Path path) {
        File file = new File(path.toUri());

        try(Scanner scanner = new Scanner(file)){
            List<String> lines = new ArrayList<>();

            scanner.nextLine(); //skip header

            while(scanner.hasNext()){
                lines.add(scanner.nextLine());
            }

            return this.createCitizensFromLines(lines);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File cannot found!", e);
        }
    }

    private List<Citizen> createCitizensFromLines(List<String> lines) {
        List<Citizen> citizens = new ArrayList<>();

        for (String line: lines){
            String[] params = line.split(CSV_SEPARATOR);

            String name = params[0];
            String zipCode = params[1];
            int age = Integer.parseInt(params[2]);
            String email = params[3];
            String ssn = params[4];

            Citizen citizen = new Citizen(name, zipCode, age, email, ssn);
            citizens.add(citizen);
        }

        return citizens;
    }

    public List<Citizen> findCitizensByZipCodeForDailyVaccinations(String zipCode) {
        return citizenRepository.findCitizensByZipCodeForDailyVaccinations(zipCode);
    }

    public boolean generateVaccinationPlan(String zipCode, String fileName) {
        List<Citizen> citizens = this.findCitizensByZipCodeForDailyVaccinations(zipCode);

        if (citizens.isEmpty()){
            System.out.println("Oltási lista generálása sikertelen! Nincs regisztrált személy ezen az irányítószámon!");
            return false;
        }

        return this.writeCitizensToFile(fileName, citizens);
    }

    private boolean writeCitizensToFile(String fileName, List<Citizen> citizens){
        try(FileWriter writer = new FileWriter(fileName)){
            writer.write(VACCINATION_PLAN_CSV_HEADER);
            LocalTime timeOfVaccination = LocalTime.of(8,0);
            for (Citizen citizen: citizens){
                writer.write(this.createLineFromCitizen(citizen, timeOfVaccination));
                timeOfVaccination = timeOfVaccination.plusMinutes(30);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Oltási lista generálása sikertelen! Fájl írási probléma.");
            return false;
        }
    }

    private String createLineFromCitizen(Citizen citizen, LocalTime time){
        return time.toString() + CSV_SEPARATOR +
                citizen.getName() + CSV_SEPARATOR +
                citizen.getZipCode() + CSV_SEPARATOR +
                citizen.getAge() + CSV_SEPARATOR +
                citizen.getEmail() + CSV_SEPARATOR +
                citizen.getSsn() + "\n";
    }

    public Optional<Citizen> findCitizenBySsn(String ssn){
        Optional<Citizen> result = citizenRepository.findCitizenBySsn(ssn);

        if (result.isPresent()){
            Citizen citizen = result.get();
            List<Vaccination> vaccinations = vaccinationService.findSuccessfulVaccinationsByCitizenId(citizen.getId());
            if (!vaccinations.isEmpty()) {
                citizen.addVaccinations(vaccinations);
            }
        }

        return result;
    }

    public Map<String, Report> getAllVaccinatedCitizensCountCategorizedByVaccinationsCount(){
        return citizenRepository.getAllVaccinatedCitizensCountCategorizedByVaccinationsCount();
    }
}
