package almazko.microjson;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public final class Json {
    private final String src;
    private       int    cursor;

    private Json(String cs) {
        this.src = cs;
    }

    private Object parseValue() {
        Object value;
        skipWhitespaces();

        switch (src.charAt(cursor)) {
            case '[':
                value = parseArray();
                break;
            case '{':
                value = parseObject();
                break;
            case '"':
                value = parseString();
                break;
            case 'n':
                cursor += 3;
                value = null;
                break;
            case 't':
                cursor += 3;
                value = TRUE;
                break;
            case 'f':
                cursor += 4;
                value = FALSE;
                break;
            default:
                value = parseNumber();
                break;
        }

        return value;
    }

    private void skipWhitespaces() {
        while (isWhitespace(src.charAt(cursor))) {
            cursor++;
        }
    }

    private String parseString() {
        cursor++;
        int endIdx = src.indexOf('"', cursor);
        String value = src.substring(cursor, endIdx);
        cursor = endIdx;
        return value;
    }

    private Number parseNumber() throws NumberFormatException {
        boolean isInt = true;
        final int beginIdx = cursor;

        for (; cursor < src.length(); cursor++) {
            char c = src.charAt(cursor);
            if (isInt && c == 'e' || c == 'E' || c == '.') isInt = false;
            if (isWhitespace(c) || c == ',' || c == '}' || c == ']') break;
        }

        int len = cursor - beginIdx;

        if (isInt && len < 10) {
            return Integer.parseInt(src, beginIdx, cursor--, 10);
        } else if (isInt && len < 19) {
            return Long.parseLong(src, beginIdx, cursor--, 10);
        } else if (isInt) {
            return new BigInteger(src.substring(beginIdx, cursor--));
        } else if (len < 15) {
            return Double.parseDouble(src.substring(beginIdx, cursor--));
        } else {
            return new BigDecimal(src.substring(beginIdx, cursor--));
        }
    }

    private JsObject parseObject() {
        JsObject obj = null;
        String key = null;

        for (++cursor; ; cursor++) {
            char c = src.charAt(cursor);
            if (c == '"') {
                key = parseString();
            } else if (c == '}') {
                return (obj == null) ? JsObject.EMPTY : obj;
            } else if (c == ':') {
                if (obj == null) obj = new JsObject();
                cursor++;
                obj.values.put(key, parseValue());
            }
        }
    }


    /**
     * @return Array
     */
    private JsArray parseArray() {
        JsArray array = null;
        for (cursor++; ; cursor++) {
            char c = src.charAt(cursor);
            if (c == ',' || isWhitespace(c)) continue;

            if (c == ']') {
                return (array == null) ? JsArray.EMPTY : array;
            } else {
                if (array == null) array = new JsArray();
                array.values.add(parseValue());
            }
        }
    }

    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    /**
     * @param jsonContent JSON
     * @return JSON element, including {@code null} when "null" is passed
     */
    public static Object parse(String jsonContent) throws IllegalArgumentException {
        return new Json(jsonContent).parseValue();
    }

    /**
     * @param jsonContent JSON
     * @return JSON object
     */
    public static JsObject parseObject(String jsonContent) throws IllegalArgumentException {
        return (JsObject) parse(jsonContent);
    }
}
