package hu.acsaifz.vaccinationapp.controllers;

import hu.acsaifz.vaccinationapp.models.*;
import hu.acsaifz.vaccinationapp.services.*;
import org.springframework.dao.DuplicateKeyException;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class VaccinationController {
    private static final String LINE = "----------------------------------";
    private static final String REPORT_LINE = "+--------------------+--------------------+--------------------+--------------------+";
    private final Scanner scanner = new Scanner(System.in);
    private final ValidatorService validatorService = new ValidatorService();
    private final CityService cityService;
    private final CitizenService citizenService;
    private final VaccinationService vaccinationService;
    private final VaccineService vaccineService;

    public VaccinationController(){
        DataSource dataSource = DataSourceService.getDataSource();
        cityService = new CityService(dataSource);
        citizenService = new CitizenService(dataSource);
        vaccinationService = new VaccinationService(dataSource);
        vaccineService = new VaccineService(dataSource);
    }

    public void run(){
        boolean run = true;

        System.out.println(LINE);
        while(run){
            this.printMainMenu();
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()){
                case "1" -> this.registration();
                case "2" -> this.massRegistration();
                case "3" -> this.generation();
                case "4" -> this.vaccinate();
                case "5" -> this.vaccinationFailure();
                case "6" -> this.report();
                case "x" -> run = this.exit();
                default -> System.out.println("Nincs ilyen menüpont!");
            }
        }
    }

    private void printMainMenu(){
        System.out.println("1. Regisztráció");
        System.out.println("2. Tömeges regisztráció");
        System.out.println("3. Generálás");
        System.out.println("4. Oltás");
        System.out.println("5. Oltás meghiúsulás");
        System.out.println("6. Riport");
        System.out.println("X. Kilépés");
        System.out.println(LINE);
        System.out.print("Kérem válasszon a menüpontok közül: ");
    }

    private void registration() {
        System.out.println(LINE);
        System.out.println("Állampolgár regisztráció");
        System.out.println(LINE);

        String name = this.getCitizenName();
        String zipCode = this.getCitizenCipCode("Kérem adja meg az állampolgár irányítószámát: ");
        int age = this.getCitizenAge();
        String email = this.getCitizenEmail();
        String ssn = this.getCitizenSsn();

        Citizen citizen = new Citizen(name, zipCode, age, email, ssn);
        citizenService.save(citizen);

        System.out.println("Állampolgár sikeresen regisztrálva.");
        System.out.println(LINE);
    }

    private String getCitizenSsn() {
        String ssn;

        do {
            System.out.print("Kérem adja meg az állampolgár TAJ számát: ");
            ssn = scanner.nextLine();

            if (!validatorService.isValidSsn(ssn)){
                System.out.println("Érvénytelen TAJ szám!");
            }

            System.out.println(LINE);

        } while(!validatorService.isValidSsn(ssn));

        return ssn;
    }

    private String getCitizenEmail() {
        String email;

        do {
            System.out.print("Kérem adja meg az állampolgár email címét: ");
            email = scanner.nextLine();

            if (!validatorService.isValidEmail(email)){
                System.out.println("Hibás email cím formátum! (pelda@email.com)");
            }

            System.out.println(LINE);

        }while(!validatorService.isValidEmail(email));

        return email;
    }

    private int getCitizenAge() {
        int age;

        do {
            System.out.print("Kérem adja meg az állampolgár életkorát: ");

            try{
                age = Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                age = -1;
            }

            if (!validatorService.isValidAge(age)){
                System.out.println("Hibás életkor! Csak 10 - 150 év közötti állampolgár regisztrálható.");
            }

            System.out.println(LINE);

        }while(!validatorService.isValidAge(age));

        return 0;
    }

    private String getCitizenCipCode(String message) {
        String zipCode;
        List<String> cities = new ArrayList<>();

        do {
            System.out.print(message);
            zipCode = scanner.nextLine();

            if (!validatorService.isValidZipCode(zipCode)){
                System.out.println("Hibás irányítószám! Nem megfelelő formátum.");
                continue;
            }

            cities = cityService.findCityByZipCode(zipCode);

            if (cities.isEmpty()){
                System.out.println("Hibás irányítószám! Nincs ilyen irányítószámmal település regisztrálva.");
            } else {
                this.printCities(cities);
            }

            System.out.println(LINE);

        }while(cities.isEmpty());

        return zipCode;
    }

    private void printCities(List<String> cities) {
        System.out.print("Város: ");

        if (cities.size() == 1){
            System.out.println(cities.get(0));
            return;
        }

        for (int i = 0; i < cities.size(); i++){
            System.out.print(cities.get(i));
            if (i != cities.size() - 1){
                System.out.print("; ");
            } else {
                System.out.println();
            }
        }
    }

    private String getCitizenName() {
        String name;
        do{
            System.out.print("Kérem adja meg az állampolgár nevét: ");
            name = scanner.nextLine();

            if (!validatorService.isValidName(name)){
                System.out.println("Hibás név! A név nem lehet üres.");
            }

            System.out.println(LINE);

        } while(!validatorService.isValidName(name));

        return name;
    }

    private void massRegistration() {
        System.out.println(LINE);
        System.out.println("Tömeges regisztráció fájlból");
        System.out.println(LINE);
        System.out.print("Adja meg az adatokat tartalmazó csv fájl elérési útvonalát: ");
        try{
            String path = scanner.nextLine();
            citizenService.massRegistration(Paths.get(path));
            System.out.println("Adatok sikeresen regisztrálva.");
        }catch (IllegalStateException ise){
            System.out.println("A rendszer nem találja a megadott fájlt!");
        }catch (DuplicateKeyException dke){
            System.out.println("Adatok regisztrálása sikertelen!");
        }finally {
            System.out.println(LINE);
        }
    }

    private void generation() {
        System.out.println(LINE);
        System.out.println("Oltási lista generálása");
        System.out.println(LINE);

        System.out.print("Adja meg a fájl nevét: ");
        String fileName = scanner.nextLine();

        String zipCode = this.getCitizenCipCode("Kérem adja meg az irányítószámot: ");

        if(citizenService.generateVaccinationPlan(zipCode, fileName)){
            System.out.println("Oltási lista sikeresen legenerálva!");
        }
        System.out.println(LINE);
    }

    private void vaccinate() {
        System.out.println(LINE);
        System.out.println("Oltás beadása");
        System.out.println(LINE);

        Citizen citizen = this.getCitizenBySsn();
        int numberOfVaccination = citizen.getNumberOfVaccination();

        if (!validatorService.isCitizenVaccinateAble(citizen)){
            System.out.println("A páciens nem beoltható: " +
                    (numberOfVaccination == 2 ? "Már rendelkezik két oltással." : "Kevesebb, mint 15 napja kapta az első oltást."));
            System.out.println(LINE);
            citizen.getVaccinations().forEach(this::printVaccinationData);
            return;
        }

        if (numberOfVaccination > 0) {
            this.printVaccinationData(citizen.getLastVaccination());
        }

        this.administrateVaccination(citizen);
    }

    private void administrateVaccination(Citizen citizen) {
        Vaccine vaccine;
        if (citizen.getNumberOfVaccination() == 0){
            vaccine = this.getVaccine();
        }else{
            vaccine = citizen.getLastVaccination().getVaccine();
            System.out.println("A pácienst " + vaccine.getName() + " vakcinával kell beoltani. Folytatáshoz nyomja le az [Enter] billentyűt");
            scanner.nextLine();
        }

        Vaccination vaccination = new Vaccination(LocalDateTime.now(), VaccinationStatus.SUCCESSFUL, vaccine);
        vaccinationService.save(vaccination,citizen.getId());
        System.out.println("Oltás sikeresen elmentve.");
        System.out.println(LINE);
    }

    private Vaccine getVaccine() {
        List<Vaccine> vaccines = vaccineService.findAll();
        Vaccine vaccine;

        do{
            this.printVaccinesMenu(vaccines);
            System.out.print("Válassz a vakcinák közül: ");
            String input = scanner.nextLine();
            try{
                int vaccineId = Integer.parseInt(input);
                vaccine = this.searchVaccine(vaccines, vaccineId);
            } catch (NumberFormatException nfe){
                vaccine = null;
            }

            if (vaccine == null){
                System.out.println("Nincs ilyen számú vakcina!");
            }
            System.out.println(LINE);
        }while(vaccine == null);

        return vaccine;
    }

    private Vaccine searchVaccine(List<Vaccine> vaccines, int vaccineId) {
        for (Vaccine vaccine: vaccines){
            if (vaccine.getId() == vaccineId){
                return vaccine;
            }
        }

        return null;
    }

    private void printVaccinesMenu(List<Vaccine> vaccines) {
        System.out.println("Vakcinák:");
        System.out.println(LINE);
        for (Vaccine vaccine: vaccines){
            System.out.println(vaccine.getId() + ". " + vaccine.getName());
        }
        System.out.println(LINE);
    }

    private void printVaccinationData(Vaccination vaccination){
        System.out.println("Oltás dátuma: " + vaccination.getVaccinationDate());
        System.out.println("Oltás gyártója: " + vaccination.getVaccine().getName());
        System.out.println(LINE);
    }

    private Citizen getCitizenBySsn() {
        Optional<Citizen> result;
        do {
            System.out.print("Adja meg a páciens TAJ számát: ");
            String ssn = scanner.nextLine();
            result = citizenService.findCitizenBySsn(ssn);

            if (result.isEmpty()){
                System.out.println("Nincs ilyen TAJ számmal regisztrált személy!");
            }

            System.out.println(LINE);
        } while(result.isEmpty());


        return result.get();
    }

    private void vaccinationFailure(){
        System.out.println(LINE);
        System.out.println("Oltás meghiúsulás regisztrálása");
        System.out.println(LINE);

        Citizen citizen = this.getCitizenBySsn();
        String note = this.getNoteOfVaccinationFailure();

        Vaccination vaccination = new Vaccination(LocalDateTime.now(), VaccinationStatus.FAILED, note);
        vaccinationService.save(vaccination, citizen.getId());
        System.out.println("Meghiúsulás regisztrálva.");
        System.out.println(LINE);
    }

    private String getNoteOfVaccinationFailure() {
        String input;
        do {
            System.out.print("Adja meg az oltás meghiúsulásának az okát: ");
            input = scanner.nextLine();

            if (input.isBlank()){
                System.out.println("A meghiúsulás oka nem lehet üres!");
            }

            System.out.println(LINE);
        }while(input.isBlank());

        return input;
    }

    private void report() {
        Map<String, Report> reports = citizenService.getAllVaccinatedCitizensCountCategorizedByVaccinationsCount();

        System.out.println(REPORT_LINE);
        System.out.printf("| %-19s| %-19s| %-19s| %-19s|%n", "Irányítószám", "Oltatlanok", "Egyszer oltottak", "Kétszer oltottak");
        System.out.println(REPORT_LINE);
        for (Map.Entry<String, Report> reportEntry: reports.entrySet()){
            String zipCode = reportEntry.getKey();
            Report report = reportEntry.getValue();

            System.out.printf("| %-19s| %-19d| %-19d| %-19d|%n", zipCode, report.getNumberOfNotVaccinated(),
                    report.getNumberOfOnceVaccinated(), report.getNumberOfTwiceVaccinated());
            System.out.println(REPORT_LINE);
        }

    }

    private boolean exit() {
        System.out.println("Viszlát!");
        return false;
    }
}
