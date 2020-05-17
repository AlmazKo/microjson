package micro.json;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            case '[' -> value = parseArray();
            case '{' -> value = parseObject();
            case '"' -> value = parseString();
            case 'n' -> {
                cursor += 3;
                value = null;
            }
            case 't' -> {
                cursor += 3;
                value = TRUE;
            }
            case 'f' -> {
                cursor += 4;
                value = FALSE;
            }
            default -> value = parseNumber();
        }

        return value;
    }

    private void skipWhitespaces() {
        while (isWhitespace(src.charAt(cursor))) {
            cursor++;
        }
    }

    private @NotNull String parseString() {
        cursor++;
        int endIdx = src.indexOf('"', cursor);
        String value = src.substring(cursor, endIdx);
        cursor = endIdx;
        return value;
    }

    private @NotNull Number parseNumber() throws NumberFormatException {
        boolean isInt = true;
        final int beginIdx = cursor;

        for (; cursor < src.length(); cursor++) {
            var c = src.charAt(cursor);
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

    private @NotNull JsObject parseObject() {
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

    private @NotNull JsArray parseArray() {
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

    public static @Nullable Object parse(@Language("JSON") String s) throws IllegalArgumentException {
        return new Json(s).parseValue();
    }

    public static JsObject parseObject(@Language("JSON") String s) throws IllegalArgumentException {
        return (JsObject) parse(s);
    }
}
