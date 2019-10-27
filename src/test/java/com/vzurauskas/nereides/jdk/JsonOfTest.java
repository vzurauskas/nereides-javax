package com.vzurauskas.nereides.jdk;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
}
