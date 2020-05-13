package com.vzurauskas.nereides.javax;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.json.JsonStructure;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

final class SmartJsonTest {

    private final Path deep;

    SmartJsonTest() throws URISyntaxException {
        this.deep = Paths.get(
            SmartJsonTest.class.getClassLoader()
                .getResource("deep.json").toURI()
        );
    }

    @Test
    void givesByteStream() {
        byte[] bytes = "{\"field1\":\"value1\",\"field2\":\"value2\"}"
            .getBytes();
        assertArrayEquals(
            bytes,
            new ByteArray(
                new SmartJson(
                    new Json.Of(bytes)
                )
            ).value()
        );
    }

    @Test
    void convertsToString() {
        assertEquals(
            "{\"field1\":\"value1\","
                + "\"field2\":\"value2\"}",
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("field1", "value1")
                        .add("field2", "value2")
                        .build()
                )
            ).textual()
        );
    }

    @Test
    void convertsToPrettyString() {
        assertEquals(
            "\n{\n"
                + "    \"field1\": \"value1\",\n"
                + "    \"field2\": \"value2\"\n"
                + '}',
            new SmartJson(
                new Json.Of(
                    javax.json.Json.createObjectBuilder()
                        .add("field1", "value1")
                        .add("field2", "value2")
                        .build()
                )
            ).pretty()
        );
    }

    @Test
    void convertsToByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        javax.json.Json.createWriter(stream).write(
            javax.json.Json.createObjectBuilder()
                .add("field1", "value1")
                .add("field2", "value2")
                .build()
        );
        byte[] bytes = stream.toByteArray();
        assertArrayEquals(
            bytes,
            new SmartJson(new Json.Of(bytes)).byteArray()
        );
    }

    @Test
    void convertsToObjectNode() {
        JsonStructure struct = javax.json.Json.createObjectBuilder()
            .add("field1", "value1")
            .add("field2", "value2")
            .build();
        assertEquals(
            struct,
            new SmartJson(
                new Json.Of(struct)
            ).jsonStructure()
        );
    }

    @Test
    void findsPath() {
        assertEquals(
            "red",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid2").optLeaf("hair").get()
        );
    }

    @Test
    void findsPathInArray() {
        assertEquals(
            "Jason",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid1/associates/0").optLeaf("name").get()
        );
    }

    @Test
    void handlesNonExistentPaths() {
        assertTrue(
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/nothing").isMissing()
        );
    }

    @Disabled("https://github.com/vzurauskas/nereides-javax/issues/26")
    @Test
    void understandsArrays() {
        String array = "[{\"name\":\"Jason\"},{\"name\":\"Thetis\"}]";
        assertArrayEquals(
            array.getBytes(),
            new ByteArray(
                new SmartJson(
                    new Json.Of(deep)
                ).at("/ocean/rock1/nereid1/associates")
            ).value()
        );
    }

    @Disabled("https://github.com/vzurauskas/nereides-javax/issues/26")
    @Test
    void reallyUnderstandsArrays() {
        assertEquals(
            "Jason",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid1/associates").at("/0").optLeaf("name")
                .get()
        );
    }

    @Test
    void knowsIfMissing() {
        assertTrue(new SmartJson(new MissingJson()).isMissing());
    }

    @Test
    void knowsIfNotMissing() {
        assertFalse(new SmartJson(new Json.Of("{}")).isMissing());
    }
}
