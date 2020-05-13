package com.vzurauskas.nereides.javax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

final class SmartJsonLeafTest {

    // String

    @Test
    void findsOptLeaf() {
        assertEquals(
            "value2",
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("field1", "value1")
                        .add("field2", "value2")
                        .build()
                )
            ).optLeaf("field2").get()
        );
    }

    @Test
    void findsLeaf() {
        assertEquals(
            "value2",
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("field1", "value1")
                        .add("field2", "value2")
                        .build()
                )
            ).leaf("field2")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder().build()
                )
            ).optLeaf("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        javax.json.Json.createObjectBuilder().build()
                    )
                ).leaf("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsEmptyOptionalIfLeafIsNotString() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringVal")
                        .add("intField", 5)
                        .build()
                )
            ).optLeaf("intField").isPresent()
        );
    }

    @Test
    void throwsIfLeafIsNotString() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        javax.json.Json.createObjectBuilder()
                            .add("stringField", "stringVal")
                            .add("intField", 5)
                            .build()
                    )
                ).leaf("intField")
            ).getMessage().contains("No such field")
        );
    }

    // Int

    @Test
    void findsOptLeafAsInt() {
        assertEquals(
            14,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 14)
                        .build()
                )
            ).optLeafAsInt("intField").get().intValue()
        );
    }

    @Test
    void findsLeafAsInt() {
        assertEquals(
            14,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 14)
                        .build()
                )
            ).leafAsInt("intField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentIntLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder().build()
                )
            ).optLeafAsInt("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentIntLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        javax.json.Json.createObjectBuilder().build()
                    )
                ).leafAsInt("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsZeroIfOptLeafIsNotInt() {
        assertEquals(
            0,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 5)
                        .build()
                )
            ).optLeafAsInt("stringField").get().intValue()
        );
    }

    @Test
    void returnsZeroIfLeafIsNotInt() {
        assertEquals(
            0,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 5)
                        .build()
                )
            ).leafAsInt("stringField")
        );
    }

    // Double

    @Test
    void returnsIntEvenIfOptLeafIsDouble() {
        assertEquals(
            5,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("doubleField", 5.9)
                        .build()
                )
            ).optLeafAsInt("doubleField").get().intValue()
        );
    }

    @Test
    void returnsIntEvenIfLeafIsDouble() {
        assertEquals(
            5,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("doubleField", 5.9)
                        .build()
                )
            ).leafAsInt("doubleField")
        );
    }

    @Test
    void findsOptLeafAsDouble() {
        assertEquals(
            14.9,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("doubleField", 14.9)
                        .build()
                )
            ).optLeafAsDouble("doubleField").get().doubleValue()
        );
    }

    @Test
    void findsLeafAsDouble() {
        assertEquals(
            14.9,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("doubleField", 14.9)
                        .build()
                )
            ).leafAsDouble("doubleField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentDoubleLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder().build()
                )
            ).optLeafAsDouble("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentDoubleLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        javax.json.Json.createObjectBuilder().build()
                    )
                ).leafAsDouble("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsEmptyOptionalIfOptLeafIsNotDouble() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 14)
                        .build()
                )
            ).optLeafAsDouble("stringField").isPresent()
        );
    }

    @Test
    void throwsIfLeafIsNotDouble() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        javax.json.Json.createObjectBuilder()
                            .add("stringField", "stringValue")
                            .add("doubleField", 5.9)
                            .build()
                    )
                ).leafAsDouble("stringField")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsDoubleEvenIfOptLeafIsInt() {
        assertEquals(
            5.0,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 5)
                        .build()
                )
            ).optLeafAsDouble("intField").get().doubleValue()
        );
    }

    @Test
    void returnsDoubleEvenIfLeafIsInt() {
        assertEquals(
            5.0,
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 5)
                        .build()
                )
            ).leafAsDouble("intField")
        );
    }

    @Test
    void returnsEmptyOptionalIfLeafIsNotDouble() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("intField", 14)
                        .build()
                )
            ).optLeafAsDouble("stringField").isPresent()
        );
    }

    // Boolean

    @Test
    void findsOptLeafAsBool() {
        assertTrue(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("boolField", true)
                        .build()
                )
            ).optLeafAsBool("boolField").get()
        );
    }

    @Test
    void findsLeafAsBool() {
        assertTrue(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("boolField", true)
                        .build()
                )
            ).leafAsBool("boolField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentBooleanLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder().build()
                )
            ).optLeafAsBool("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentBooleanLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        javax.json.Json.createObjectBuilder().build()
                    )
                ).leafAsBool("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsFalseIfOptLeafIsNotBool() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("boolField", true)
                        .build()
                )
            ).optLeafAsBool("stringField").get()
        );
    }

    @Test
    void returnsFalseIfLeafIsNotBool() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("stringField", "stringValue")
                        .add("boolField", true)
                        .build()
                )
            ).leafAsBool("stringField")
        );
    }
}
