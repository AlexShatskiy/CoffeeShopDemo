package com.sh.coffeeshop.service.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * checks the entered parameters
 */
public class ServiceValidator {

    private static String NUMBER = "[1-9]\\d*";


    public static boolean isNumberValid(String quantity) {
        if (quantity == null) {
            return false;
        } else if (quantity.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(NUMBER);
        Matcher matcher = pattern.matcher(quantity);

        return matcher.matches();
    }

    public static boolean isTextValid(String name) {
        if (name == null) {
            return false;
        } else if (name.isEmpty() || name.length() > 200) {
            return false;
        }
        return true;
    }
}
