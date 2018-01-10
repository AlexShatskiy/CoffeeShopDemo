package com.sh.coffeeshop.service.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceValidator {

    private static String QUANTITY = "[1-9]\\d*";

    public static boolean isQuantityOrIdValid(String quantity) {
        if (quantity == null) {
            return false;
        } else if (quantity.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(QUANTITY);
        Matcher matcher = pattern.matcher(quantity);

        return matcher.matches();
    }
    public static boolean isNameValid(String name) {
        if (name == null) {
            return false;
        } else if (name.isEmpty()) {
            return false;
        }
        return true;
    }
}
