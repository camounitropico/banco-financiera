package com.banco_financiera.utils;

import java.util.regex.Pattern;

public class StringValidator {
    private static final Integer MIN_LENGTH = 2;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }

    public static boolean isValidName(String name) {
        return name != null && name.length() >= MIN_LENGTH;
    }
}
