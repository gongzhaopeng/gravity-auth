package cn.benbenedu.utility;

import java.util.regex.Pattern;

public class IdentityUtility {

    public static final String ID_NUMBER_REGEX =
            "^\\d{17}[\\dX]$";

    private static final Pattern ID_NUMBER_PATTERN =
            Pattern.compile(ID_NUMBER_REGEX);

    public static boolean isWellFormedIdNumber(String mobileNumber) {

        return ID_NUMBER_PATTERN.matcher(mobileNumber).matches();
    }
}
