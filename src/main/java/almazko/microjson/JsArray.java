package almazko.microjson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class JsArray {
    static final JsArray      EMPTY = new JsArray(Collections.emptyList());
    final        List<Object> values;

    JsArray(List<Object> values) {
        this.values = values;
    }

    JsArray() {
        this.values = new ArrayList<>();
    }

    public JsArray getArray(int pos) {
        return (JsArray) values.get(pos);
    }

    public JsObject getObject(int pos) {
        return (JsObject) values.get(pos);
    }

    public boolean getBoolean(int pos) {
        return (Boolean) values.get(pos);
    }

    public String getString(int pos) {
        return (String) values.get(pos);
    }

    public Number getNumber(int pos) {
        return (Number) values.get(pos);
    }

    public Object get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }
}