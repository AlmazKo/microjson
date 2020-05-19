package almazko.microjson;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class JsObject {
    static final JsObject            EMPTY = new JsObject(Collections.emptyMap());
    final        Map<String, Object> values;

    private JsObject(Map<String, Object> values) {
        this.values = values;
    }

    JsObject() {
        this.values = new HashMap<>();
    }

    @Nullable public JsArray getArray(String key) {
        return (JsArray) values.get(key);
    }

    @Nullable public JsObject getObject(String key) {
        return (JsObject) values.get(key);
    }

    @Nullable public Number getNumber(String key) {
        return (Number) values.get(key);
    }

    @Nullable public String getString(String key) {
        return (String) values.get(key);
    }

    @Nullable public Boolean getBoolean(String key) {
        return (Boolean) values.get(key);
    }

    public int size() {
        return values.size();
    }
}
