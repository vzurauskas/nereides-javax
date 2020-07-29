package com.vzurauskas.nereides.javax;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import org.junit.jupiter.api.Test;

final class JsonOfTest {

    @Test
    void constructsFromBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JsonWriter writer = javax.json.Json.createWriter(stream);
        writer.write(
            javax.json.Json.createObjectBuilder()
                .add("field1", "value1")
                .add("field2", "value2")
                .build()
        );
        byte[] bytes = stream.toByteArray();
        assertArrayEquals(
            bytes,
            new ByteArray(new Json.Of(bytes)).value()
        );
    }

    @Test
    void constructsFromString() {
        String string = "{\"number\": 12}";
        assertArrayEquals(
            string.getBytes(),
            new ByteArray(new Json.Of(string)).value()
        );
    }

    @Test
    void constructsFromInputStream() {
        final byte[] bytes = "{\"number\": 12}".getBytes();
        assertArrayEquals(
            bytes,
            new ByteArray(
                new Json.Of(
                    new ByteArrayInputStream(bytes)
                )
            ).value()
        );
    }

    @Test
    void constructsFromJsonStructure() {
        JsonStructure struct = javax.json.Json.createObjectBuilder()
            .add("field1", "value1")
            .add("field2", "value2")
            .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        javax.json.Json.createWriter(stream).write(struct);
        assertArrayEquals(
            stream.toByteArray(),
            new ByteArray(new Json.Of(struct)).value()
        );
    }

    @Test
    void constructsFromFile() throws IOException, URISyntaxException {
        Path path = Paths.get(
            JsonOfTest.class.getClassLoader().getResource("deep.json").toURI()
        );
        assertArrayEquals(
            Files.readAllBytes(path),
            new ByteArray(new Json.Of(path)).value()
        );
    }

    @Test
    void understandsArrays() {
        String string = "[{\"name\":\"Jason\"},{\"name\":\"Thetis\"}]";
        assertArrayEquals(
            string.getBytes(),
            new ByteArray(new Json.Of(string)).value()
        );
    }

    @Test
    void toStringWorksEvenIfMalformed() {
        assertEquals(
            "malformed",
            new Json.Of("malformed").toString()
        );
    }

    @Test
    void canReadTwice() {
        String string = "{\"number\": 12}";
        Json json = new Json.Of(string);
        assertArrayEquals(string.getBytes(), new ByteArray(json).value());
        assertArrayEquals(string.getBytes(), new ByteArray(json).value());
    }

    @Test
    void doesntReadFileEachTimeJsonIsAccessed() throws IOException {
        String string = "{\"number\": 12}";
        File file = File.createTempFile("whatever", "whatever");
        try (PrintStream ps = new PrintStream(file)) {
            ps.print(string);
        }
        Json json = new Json.Of(file.toPath());
        assertArrayEquals(string.getBytes(), new ByteArray(json).value());
        file.delete();
        assertArrayEquals(string.getBytes(), new ByteArray(json).value());
    }
}
