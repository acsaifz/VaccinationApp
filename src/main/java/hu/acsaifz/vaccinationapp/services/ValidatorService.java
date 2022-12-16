package hu.acsaifz.vaccinationapp.services;

import java.util.regex.Pattern;

public class ValidatorService {
    private static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String ZIP_CODE_REGEX_PATTERN = "\\d{4}";
    private static final String SSN_REGEX_PATTERN = "\\d{9}";

    public boolean isValidName(String name) {
        return !this.isEmptyString(name);
    }

    public boolean isValidZipCode(String zipCode){
        return patternMatches(zipCode, ZIP_CODE_REGEX_PATTERN);
    }

    public boolean isValidAge(int age) {
        return age >= 10 && age <= 150;
    }

    public boolean isValidEmail(String email){
        return this.patternMatches(email,EMAIL_REGEX_PATTERN);
    }

    public boolean isValidSsn(String ssn) {
        if (!patternMatches(ssn, SSN_REGEX_PATTERN)){
            return false;
        }

        int lastDigit = this.getSsnLastDigit(ssn);
        int checkSum = this.getSsnChecksum(ssn);

        return lastDigit == checkSum;
    }

    private int getSsnLastDigit(String ssn){
        return Integer.parseInt(
                Character.toString(ssn.charAt(ssn.length() - 1))
        );
    }

    private int getSsnChecksum(String ssn){
        int checksum = 0;
        for (int i = 0; i < ssn.length() - 1; i++){
            char digit = ssn.charAt(i);

            int num = Integer.parseInt(
                    Character.toString(digit)
            );

            checksum += i % 2 == 0 ? num * 3 : num * 7;
        }

        return checksum % 10;
    }

    private boolean isEmptyString(String s){
        return s == null || s.isBlank();
    }

    private boolean patternMatches(String s, String regexPattern){
        return s != null && Pattern.compile(regexPattern)
                .matcher(s)
                .matches();
    }
}
