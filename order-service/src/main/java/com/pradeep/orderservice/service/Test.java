package com.pradeep.orderservice.service;

public class Test {

    private  static String checkValidCode(String number,int codeLength) {

        if(number==null){
            number="";
        }

        number = number.replaceAll("\\D", "9");

        // Check if authCode is a number
        boolean isNumber = number.matches("\\d+");
        if (isNumber) {
            // Check if authCode has length 6
            if (number.length() == codeLength) {
                return number;
            } else if (number.length() > codeLength) {
                // Truncate the authCode to the first 6 characters
                number = number.substring(0, codeLength);
            } else {
                // Generate a random number to append to the input
                int randomNumber =
                        (int) (Math.random() * (Math.pow(10, codeLength - number.length())));
                number += randomNumber;
            }
        } else {
            number = String.valueOf((int) (Math.random() * (Math.pow(10, codeLength))));
        }
        return number;
    }

    public static void main(String[] args) {
        System.out.println(Test.checkValidCode("ABCD2E",6));
    }
}
