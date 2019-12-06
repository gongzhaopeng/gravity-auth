package cn.benbenedu.utility;

import java.util.regex.Pattern;

public class MobileUtility {

    public static final String MOBILE_NUMBER_REGEX =
            "^\\d{11}$";

    private static final Pattern MOBILE_NUMBER_PATTERN =
            Pattern.compile(MOBILE_NUMBER_REGEX);

    public static boolean isWellFormedMobileNumber(String mobileNumber) {

        return MOBILE_NUMBER_PATTERN.matcher(mobileNumber).matches();
    }
}
