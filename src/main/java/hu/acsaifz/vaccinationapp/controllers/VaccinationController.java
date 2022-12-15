package hu.acsaifz.vaccinationapp.controllers;

import java.util.Scanner;

public class VaccinationController {
    private static final String LINE = "----------------------------------";
    private final Scanner scanner = new Scanner(System.in);

    public void run(){
        boolean run = true;
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
        System.out.println(LINE);
        System.out.println("1. Regisztráció");
        System.out.println("2. Tömeges regisztráció");
        System.out.println("3. Generálás");
        System.out.println("4. Oltás");
        System.out.println("5. Oltás meghiúsulás");
        System.out.println("6. Riport");
        System.out.println("X. Kilépés");
        System.out.println(LINE);
        System.out.print("Válassz a menüpontok közül: ");
    }

    private void registration() {
    }

    private void massRegistration() {
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
