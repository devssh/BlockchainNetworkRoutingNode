package app.utils;

import java.util.List;

public class StringUtils {

    public static final String SQUARE="square";
    public static final String CURLY = "curly";

    public static String ArrayOfObjects(String... objects) {
        return SurroundWithBraces(JoinWithComma(objects), SQUARE);
    }

    public static String ArrayOfObjects(List<String> objects) {
        return ArrayOfObjects(objects.toArray(new String[0]));
    }

    public static String SurroundWithBraces(String value) {
        return SurroundWithBraces(value, CURLY);
    }

    public static String SurroundWithBraces(String value, String type) {
        if (type.equals(SQUARE)) {
            return "[" + value + "]";
        }
        return "{" + value + "}";
    }

    public static String SurroundWithQuotes(String value) {
        return "\"" + value + "\"";
    }

    public static String JoinWith(String delim, String... values) {
        return String.join(delim, values);
    }

    public static String JoinWith(String delim, List<String> values) {
        return JoinWith(delim, values.toArray(new String[0]));
    }

    public static String Join(List<String> values) {
        return Join(values.toArray(new String[0]));
    }

    public static String Join(String... values) {
        return JoinWith("", values);
    }

    public static String JoinWithComma(String... values) {
        return JoinWith(",", values);
    }

    public static String JoinWithComma(List<String> values) {
        return JoinWith(",", values);
    }

    public static String SuperKeyValuePair(String key, String value) {
        return "\"" + key + "\":{" + value + "}";
    }

    public static String KeyValuePair(String key, String value) {
        return KeyValuePair(key, value, true);
    }

    private static String KeyValuePair(String key, String value, boolean noBraces) {
        if (noBraces) {
            return "\"" + key + "\":\"" + value + "\"";
        }

        return SurroundWithBraces(key + ":" + value);
    }

    public static String StripFirstAndLast(String text) {
        return text.substring(1, text.length() - 1);
    }

    public static String StripQuotes(String text) {
        return StripFirstAndLast(text);
    }

    public static String StripSquareBraces(String text) {
        return StripFirstAndLast(text);
    }

    public static String StripBraces(String text) {
        return StripFirstAndLast(text);
    }

    public static String ExtractStringKeyFromJson(String key, String json) {
        return json.split("\"" + key + "\":\"", 2)[1].split("\"", 2)[0];
    }

    public static String extractArrayKeyFromJson(String key, String json) {
        return SurroundWithBraces(json.split("\"" + key + "\":\\[", 2)[1].split("]", 2)[0], "square");
    }
}
