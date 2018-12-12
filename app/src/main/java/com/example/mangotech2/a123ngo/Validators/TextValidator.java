package com.example.mangotech2.a123ngo.Validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mangotech2 on 10/24/2017.
 */

public class TextValidator {


    public static boolean validatenumber(String number){

        String pattern = "[0-9]{10}";
        Pattern regEx = Pattern.compile(pattern);
        Matcher m = regEx.matcher(number);
        boolean match=m.matches();
        return match;
    }
    public static boolean validateverificationcode(String number){

        String pattern = "[0-9]{4}";
        Pattern regEx = Pattern.compile(pattern);
        Matcher m = regEx.matcher(number);
        boolean match=m.matches();
        return match;
    }


    public static boolean validatepassword(String password){

        String pattern = ".{6,20}";
        Pattern regEx = Pattern.compile(pattern);
        Matcher m = regEx.matcher(password);
        boolean match=m.matches();
        return match;
    }


    public  static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateemail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}
