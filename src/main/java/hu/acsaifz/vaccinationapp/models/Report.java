package hu.acsaifz.vaccinationapp.models;

public class Report {
    int numberOfNotVaccinated;
    int numberOfOnceVaccinated;
    int numberOfTwiceVaccinated;


    public Report() {
    }

    public Report(int numberOfNotVaccinated, int numberOfOnceVaccinated, int numberOfTwiceVaccinated) {
        this.numberOfNotVaccinated = numberOfNotVaccinated;
        this.numberOfOnceVaccinated = numberOfOnceVaccinated;
        this.numberOfTwiceVaccinated = numberOfTwiceVaccinated;
    }

    public int getNumberOfNotVaccinated() {
        return numberOfNotVaccinated;
    }

    public int getNumberOfOnceVaccinated() {
        return numberOfOnceVaccinated;
    }

    public int getNumberOfTwiceVaccinated() {
        return numberOfTwiceVaccinated;
    }
}
