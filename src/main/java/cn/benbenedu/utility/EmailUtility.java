package cn.benbenedu.utility;

import java.util.regex.Pattern;

public class EmailUtility {

    public static final String EMAIL_ADDRESS_REGEX =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile(EMAIL_ADDRESS_REGEX);

    public static boolean isWellFormedEmailAddress(String emailAddress) {

        return EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();
    }
}
