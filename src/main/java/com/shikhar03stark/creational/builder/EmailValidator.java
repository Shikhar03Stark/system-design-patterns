package com.shikhar03stark.creational.builder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;

@Getter
public class EmailValidator {
    private final boolean allowNumbers;
    private final boolean allowSpecialCharacters;
    private final String specialCharacters;
    private final List<String> allowedDomains;
    private final List<String> offensiveWords;

    private EmailValidator(boolean allowNumbers, boolean allowSpecialCharacters, String specialCharacters, List<String> allowedDomains, List<String> offensiveWords){
        this.allowNumbers = allowNumbers;
        this.allowSpecialCharacters = allowSpecialCharacters;
        this.specialCharacters = specialCharacters;
        this.allowedDomains = allowedDomains.stream().map(String::toUpperCase).toList();
        this.offensiveWords = offensiveWords.stream().map(String::toUpperCase).toList();
    }

    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private boolean isEnglishLexicon(char c){
        return c >= 'A' && c <= 'Z';
    }

    public Optional<String> reason(String email){
        // Validate email logic
        if(Objects.isNull(email)){
            return Optional.of("Invalid email: email is null");
        }
        int sz = email.length();
        if(sz == 0){
            return Optional.of("Invalid email: email has 0 length");
        }
        
        String uppercase = email.toUpperCase();
        int domainIdx = uppercase.indexOf("@");

        if(domainIdx < 0){
            return Optional.of("Invalid email: email does not have a domain");
        } else if (domainIdx <= 0){
            return Optional.of("Invalid email: email has no identifer");
        } else if (domainIdx == uppercase.length()-1){
            return Optional.of("Invalid email: email does not have a domain");
        }

        final String identifier = uppercase.substring(0, domainIdx-1);

        for(String offensiveWord: offensiveWords){
            if(identifier.contains(offensiveWord)){
                return Optional.of("Invalid email: email had an offensive word");
            }
        }

        for(int idx = 0; idx < identifier.length(); idx++){
            char c = identifier.charAt(idx);
            if(isDigit(c) && !allowNumbers){
                return Optional.of(String.format("Invalid email: email contains digit %s at index %s", c, idx));
            }
            if(!isDigit(c) && !isEnglishLexicon(c)){
                if(!specialCharacters.contains(""+c)){
                    return Optional.of(String.format("Invalid email: email contains special character %s which is not allowed", c));
                }
            }
        }


        final String domain = uppercase.substring(domainIdx+1);

        if(Objects.isNull(domain) || domain.length() == 0){
            return Optional.of("Invalid email: email does not have a domain");
        }

        if(!allowedDomains.stream().anyMatch(d -> d.equals(domain))){
            return Optional.of("Invalid email: email does not match allowed domains");
        }

        return Optional.empty();
    }

    public boolean validate(String email){
        final Optional<String> reasonOptional = reason(email);
        if(reasonOptional.isPresent()){
            return false;
        }
        return true;
    }

    public static EmailValidatorBuilder builder(){
        return new EmailValidatorBuilder();
    }

    public static class EmailValidatorBuilder {
        private boolean _allowNumbers = true;
        private boolean _allowSpecialCharacters = true;
        private String _specialCharacters = "$_.*";
        private List<String> _allowedDomains = Arrays.asList("gmail.com", "example.com");
        private List<String> _offensiveWords = Arrays.asList("fuck", "shit");

        public EmailValidatorBuilder allowNumber(boolean allow){
            this._allowNumbers = allow;
            return this;
        }

        public EmailValidatorBuilder allowSpecialCharacters(boolean allow){
            this._allowSpecialCharacters = allow;
            return this;
        }

        public EmailValidatorBuilder specialCharacters(String characters){
            this._specialCharacters = characters;
            return this;
        }

        public EmailValidatorBuilder allowedDomains(List<String> domains){
            this._allowedDomains = domains;
            return this;
        }

        public EmailValidatorBuilder offensiveWords(List<String> words){
            this._offensiveWords = words;
            return this;
        }

        public EmailValidator build(){
            return new EmailValidator(this._allowNumbers, this._allowSpecialCharacters, this._specialCharacters, this._allowedDomains, this._offensiveWords);
        }
        
    }
}
