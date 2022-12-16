package hu.acsaifz.vaccinationapp.controllers;

import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.services.CitizenService;
import hu.acsaifz.vaccinationapp.services.CityService;
import hu.acsaifz.vaccinationapp.services.DataSourceService;
import hu.acsaifz.vaccinationapp.services.ValidatorService;
import org.springframework.dao.DuplicateKeyException;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VaccinationController {
    private static final String LINE = "----------------------------------";
    private final Scanner scanner = new Scanner(System.in);
    private final ValidatorService validatorService = new ValidatorService();
    private final CityService cityService;
    private final CitizenService citizenService;

    public VaccinationController(){
        DataSource dataSource = DataSourceService.getDataSource();
        cityService = new CityService(dataSource);
        citizenService = new CitizenService(dataSource);
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
        String zipCode = this.getCitizenCipCode();
        int age = this.getCitizenAge();
        String email = this.getCitizenEmail();
        String ssn = this.getCitizenSsn();

        Citizen citizen = new Citizen(name, zipCode, age, email, ssn);
        citizenService.save(citizen);

        System.out.println(LINE);
        System.out.println("Állampolgár sikeresen regisztrálva.");
        System.out.println(LINE);
    }

    private String getCitizenSsn() {
        String ssn;

        do {
            System.out.print("Kérem adja meg az állampolgár TAJ számát");
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

    private String getCitizenCipCode() {
        String zipCode;
        List<String> cities = new ArrayList<>();

        do {
            System.out.print("Kérem adja meg az az állampolgár irányítószámát: ");
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
    }

    private void vaccinate() {
    }

    private void vaccinationFailure() {
    }

    private void report() {
    }

    private boolean exit() {
        System.out.println("Viszlát!");
        return false;
    }
}
