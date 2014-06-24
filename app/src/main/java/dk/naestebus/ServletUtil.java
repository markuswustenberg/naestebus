package dk.naestebus;

import java.util.regex.Pattern;

/**
 * Various utility methods.
 */
final class ServletUtil {

    public static final Pattern INTEGER_ONLY_PATTERN = Pattern.compile("^\\d+$");
    public static final Pattern BASIC_STRING_ONLY_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    private ServletUtil() {
        // Don't instantiate
    }
}
