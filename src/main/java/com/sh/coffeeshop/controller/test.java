package com.sh.coffeeshop.controller;

import com.sh.coffeeshop.service.validator.ServiceValidator;

import java.util.UUID;

public class test {
    public static void main(String[] args) {





    }
    private static void id(){
        Long uniqueID1 = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        Long uniqueID2 = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        System.out.println(uniqueID1);
        System.out.println(uniqueID2);
        System.out.println(uniqueID1.equals(uniqueID2));
    }
}
