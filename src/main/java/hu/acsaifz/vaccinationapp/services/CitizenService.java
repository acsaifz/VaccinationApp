package hu.acsaifz.vaccinationapp.services;

import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.repositories.CitizenRepository;
import hu.acsaifz.vaccinationapp.repositories.CitizenRepositoryImpl;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CitizenService {
    private static final String CSV_SEPARATOR = ";";
    private final CitizenRepository citizenRepository;

    public CitizenService(DataSource dataSource){
        citizenRepository = new CitizenRepositoryImpl(dataSource);
    }

    public void save(Citizen citizen){
        citizenRepository.save(citizen);
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
}
