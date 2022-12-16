package hu.acsaifz.vaccinationapp.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorServiceTest {
    private final ValidatorService validatorService = new ValidatorService();

    @Test
    void testIsValidCitizenName(){
        boolean result = validatorService.isValidName("John Doe");
        boolean result2 = validatorService.isValidName("Jack Doe");

        assertTrue(result);
        assertTrue(result2);
    }

    @Test
    void testIsValidCitizenNameWhenNotValid(){
        boolean result = validatorService.isValidName(null);
        boolean result2 = validatorService.isValidZipCode("");
        boolean result3 = validatorService.isValidName("   ");

        assertFalse(result);
        assertFalse(result2);
        assertFalse(result3);
    }

    @Test
    void testIsValidZipCode(){
        boolean result = validatorService.isValidZipCode("1069");
        boolean result2 = validatorService.isValidZipCode("9549");

        assertTrue(result);
        assertTrue(result2);
    }

    @Test
    void testIsValidZipCodeWhenNotValid(){
        boolean result = validatorService.isValidZipCode(null);
        boolean result2 = validatorService.isValidZipCode("");
        boolean result3 = validatorService.isValidZipCode("256");
        boolean result4 = validatorService.isValidZipCode("10000");

        assertFalse(result);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    void testIsValidAge(){
        boolean result = validatorService.isValidAge(15);
        boolean result2 = validatorService.isValidAge(118);

        assertTrue(result);
        assertTrue(result2);
    }

    @Test
    void testIsValidAgeWhenNotValid(){
        boolean result = validatorService.isValidAge(5);
        boolean result2 = validatorService.isValidAge(164);

        assertFalse(result);
        assertFalse(result2);
    }

    @Test
    void testIsValidEmail(){
        boolean result = validatorService.isValidEmail("john.doe@domain.com");
        boolean result2 = validatorService.isValidEmail("johndoe@domain.com");
        boolean result3 = validatorService.isValidEmail("john_doe@domain.com");
        boolean result4 = validatorService.isValidEmail("john-doe@domain.com");
        boolean result5 = validatorService.isValidEmail("johndoe@do.main.com");

        assertTrue(result);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);
    }

    @Test
    void testIsValidEmailWhenNotValid(){
        boolean result = validatorService.isValidEmail("johndoe.@domain.com");
        boolean result2 = validatorService.isValidEmail(".user.name@domain.com");
        boolean result3 = validatorService.isValidEmail("user-name@domain.com.");
        boolean result4 = validatorService.isValidEmail("username@.com");

        assertFalse(result);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    void testIsValidSsn(){
        boolean result = validatorService.isValidSsn("123456788");
        boolean result2 = validatorService.isValidSsn("925862035");

        assertTrue(result);
        assertTrue(result2);
    }

    @Test
    void testIsValidSsnWhenNotValid(){
        boolean result = validatorService.isValidSsn("123456789");
        boolean result2 = validatorService.isValidSsn("925862030");

        assertFalse(result);
        assertFalse(result2);
    }
}