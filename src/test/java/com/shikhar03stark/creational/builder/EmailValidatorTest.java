package com.shikhar03stark.creational.builder;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.shikhar03stark.creational.singleton.Logger;
import com.shikhar03stark.creational.singleton.LoggerFactory;

public class EmailValidatorTest {

    private final Logger log = LoggerFactory.getInstance();

    @Test
    public void instantiateValidatorWithBuilder(){
        final EmailValidator validator = EmailValidator
            .builder()
            .build();

        Assertions.assertTrue(validator instanceof EmailValidator);

        // Default values
        Assertions.assertTrue(validator.isAllowNumbers());
        Assertions.assertTrue(validator.isAllowSpecialCharacters());
        Assertions.assertEquals("$_.*", validator.getSpecialCharacters());
        Assertions.assertIterableEquals(Arrays.asList("GMAIL.COM", "EXAMPLE.COM"), validator.getAllowedDomains());
        Assertions.assertIterableEquals(Arrays.asList("FUCK", "SHIT"), validator.getOffensiveWords());
    }

    @Test
    public void dontAllowNumbersTest(){
        final EmailValidator validator = EmailValidator
            .builder()
            .allowNumber(false)
            .build();

        Assertions.assertFalse(validator.isAllowNumbers());
    }

    @Test
    public void updateAllowedDomainsTest(){
        final EmailValidator validator = EmailValidator
            .builder()
            .allowedDomains(Arrays.asList("yahoo.com", "outlook.com"))
            .build();

        Assertions.assertIterableEquals(Arrays.asList("YAHOO.COM", "OUTLOOK.COM"), validator.getAllowedDomains());
    }

    @Test
    public void checkBadEmailTest(){
        final List<String> emails = List.of("", "abc", "@example.com", "abc@@example.com", "abc%xyz@gmail.com", "shit30@example.com", "abcd123@yahoo.com");
        final EmailValidator validator = EmailValidator
            .builder()
            .build();


        for(String email: emails){
            log.info(String.format("Email %s", email));
            Assertions.assertFalse(validator.validate(email));
            log.info(String.format("Email bad reason: %s", validator.reason(email).get()));
        }
    }
}
