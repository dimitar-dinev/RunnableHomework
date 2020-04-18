package com.example.runnablehomework;

import java.util.regex.Pattern;

class LoginTaskValidator {

    public  boolean checkEmail(String email) {
        String emailRegex = "^.+@.{2,}\\..{2,}$";

        Pattern pattern = Pattern.compile(emailRegex);

         return pattern.matcher(email).matches();
    }

    public boolean checkPassword(String password) {

        String passwordRegex = "^(.{2}[0-9].{2}[0-9][A-Z][!@#$%&*]){2}$";

        Pattern pattern = Pattern.compile(passwordRegex);

        return pattern.matcher(password).matches();
    }
}
