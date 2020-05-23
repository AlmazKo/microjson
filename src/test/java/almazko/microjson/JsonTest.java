package almazko.microjson;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonTest {

    @Test
    void parseObject() throws IOException {
        var example = Files.readString(Path.of("src/test/resources/test.json"));
        var js = Json.parseObject(example);

        assertEquals("Hello ", js.getString("str"));
        assertEquals(" ", js.getString("str_blank"));
        assertEquals("", js.getString("str_empty"));
        assertEquals("Солнце ☀", js.getString("str_utf"));
        // todo: unsupported escaped characters
        // assertEquals("Hot " + '\u2615', js.getString("str_utf_escaped"));
        assertEquals(-12345, js.getNumber("negative_number"));
        assertEquals(1e-12, js.getNumber("number"));
        assertEquals(1, js.getNumber("number1"));
        assertEquals(123456789012345L, js.getNumber("number_long"));
        assertEquals(new BigInteger("1234567890123456789012345678901234567890"), js.getNumber("big_integer"));
        assertEquals(new BigDecimal("0.1234567890123456789012345678901234567890"), js.getNumber("big_decimal"));
        assertEquals(true, js.getBoolean("bool_true"));
        assertEquals(false, js.getBoolean("bool_false"));
        assertNull(js.getNumber("nullable"));
        assertNull(js.getObject("absent"));

        assertEquals(0, js.getArray("nested_array").getArray(0).size());
        assertEquals(0, js.getObject("obj_empty").size());

        var arr = js.getArray("arr_full");
        assertNotNull(arr);
        assertEquals("array", arr.getString(0));
        assertNotNull(arr.getObject(1));
        assertNotNull(arr.getArray(2));
        assertTrue(arr.getBoolean(3));
        assertFalse(arr.getBoolean(4));
        assertNull(arr.get(5));
        assertEquals(0.5, arr.getNumber(6));
    }

    @Test
        //language=JSON
    void parseArray() {
        assertTrue(Json.parse("[1]") instanceof JsArray);
        assertTrue(Json.parse("{}") instanceof JsObject);
        assertTrue(Json.parse("1") instanceof Number);
        assertTrue(Json.parse("true") instanceof Boolean);
        assertTrue(Json.parse("false") instanceof Boolean);
        assertNull(Json.parse("null"));
    }

    @Test
    void checkSizes() {
        /*language=JSON */
        var js = Json.parseObject("{\"id\":1234,\"friends\": []}");
        assertEquals(js.size(), 2);
        assertEquals(js.getArray("friends").size(), 0);
    }

    @Test
    void checkInvalidJson() {
        assertThrows(IllegalArgumentException.class, () -> {
            Json.parseObject("{\"id\":1234,\"friends: []}");
        });
    }
}
