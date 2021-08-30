package almazko.microjson;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public final class JsObject {
    static final JsObject            EMPTY = new JsObject(Collections.emptyMap());
    final        Map<String, Object> values;

    private JsObject(Map<String, Object> values) {
        this.values = values;
    }

    JsObject() {
        this.values = new HashMap<>();
    }

    /**
     * @param key JSON field
     * @return Array or {@code null}
     */
    public JsArray getArray(String key) {
        return (JsArray) values.get(key);
    }

    /**
     * @param key JSON field
     * @return JsObject or {@code null}
     */
    public JsObject getObject(String key) {
        return (JsObject) values.get(key);
    }

    /**
     * @param key JSON field
     * @return Number or {@code null}
     */
    public Number getNumber(String key) {
        return (Number) values.get(key);
    }

    /**
     * @param key JSON field
     * @return int
     */
    public int getInt(String key) throws NullPointerException {
        return ((Number) values.get(key)).intValue();
    }

    /**
     * @param key JSON field
     * @return String or {@code null}
     */
    public String getString(String key) {
        return (String) values.get(key);
    }

    /**
     * @param key JSON field
     * @return Boolean or {@code null}
     */
    public Boolean getBoolean(String key) {
        return (Boolean) values.get(key);
    }

    public int size() {
        return values.size();
    }

    @Override public String toString() {
        if (values.isEmpty()) return "{}";

        StringJoiner sj = new StringJoiner(", ", "{", "}");
        for (Map.Entry<String, Object> e : values.entrySet()) {
            if (e.getValue() instanceof CharSequence) {
                sj.add("\"" + e.getKey() + "\":\"" + e.getValue() + '"');
            } else {
                sj.add("\"" + e.getKey() + "\":" + e.getValue());
            }
        }
        return sj.toString();
    }
}
