package util;

import model.enums.UserType;

import java.util.regex.Pattern;

public class Helper {
    public static boolean isEmailPatternValid(String emailAddress) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(emailAddress)
                .matches();
    }
    public static boolean isPasswordValid(String password) {
        return Pattern.compile("^(?=.*[0-9])"
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        + "(?=.*[@#$%^&+=])"
                        + "(?=\\S+$).{8,20}$")
                .matcher(password)
                .matches();
    }
}
