package com.vzurauskas.nereides.javax;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Optional;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

/**
 * A smart JSON. It can represent itself in other data types such as,
 * byte arrays, {@link String}s, byte arrays and so forth. It can also
 * give its nested JSONs and leaves, tell if it is missing and do other useful
 * things. To use it, you need to wrap another {@link Json} in it, e.g.
 * <pre>
 * {@code
 * Json original = new Json.Of(...);
 * SmartJson smart = new SmartJson(original);
 * String textual = smart.textual();
 * InputStream stream = smart.inputStream();
 * SmartJson nested = smart.at("/path/to/nested/json");
 * if (!nested.isMissing()) {
 *     Optional<String> value = nested.leaf("fieldName");
 * }
 * }
 * </pre>
 */
public final class SmartJson implements Json {

    private final Json origin;
    private final Unchecked<JsonStructure> structure;

    /**
     * Constructor.
     * @param origin Original JSON as basis to this {@code SmartJson}.
     */
    public SmartJson(Json origin) {
        this(
            origin,
            new Unchecked<>(
                () -> javax.json.Json.createReader(origin.bytes()).read()
            )
        );
    }

    private SmartJson(Json origin, Unchecked<JsonStructure> structure) {
        this.origin = origin;
        this.structure = structure;
    }

    @Override
    public InputStream bytes() {
        return origin.bytes();
    }

    /**
     * Represent this JSON in textual form.
     * @return String representing this JSON in textual form.
     */
    public String textual() {
        return new Unchecked<>(
            () -> {
                try (Writer writer = new StringWriter()) {
                    javax.json.Json.createWriter(writer).write(
                        structure.value()
                    );
                    return writer.toString();
                }
            }
        ).value();
    }

    /**
     * Represent this JSON in pretty format textual form.
     * @return String representing this JSON in pretty format textual form.
     */
    public String pretty() {
        return new Unchecked<>(
            () -> {
                try (Writer writer = new StringWriter()) {
                    javax.json.Json.createWriterFactory(
                        Collections.singletonMap(
                            JsonGenerator.PRETTY_PRINTING, true
                        )
                    ).createWriter(writer).write(structure.value());
                    return writer.toString();
                }
            }
        ).value();
    }

    /**
     * Represent this JSON in an array of bytes.
     * @return Byte array representing this JSON.
     */
    public byte[] byteArray() {
        return new ByteArray(bytes()).value();
    }

    /**
     * Method to get a {@code String} type field of this JSON.
     * @param name Name of the field to return.
     * @return Optional value of the field.
     */
    public Optional<String> leaf(String name) {
        return Optional.of(structure.value().asJsonObject())
            .filter(
                o -> o.containsKey(name)
                    && o.get(name).getValueType() == JsonValue.ValueType.STRING
            )
            .map(o -> o.getString(name));
    }

    /**
     * Method to get an {@code int} type field of this JSON.
     * @param name Name of the field to return.
     * @return Optional value of the field.
     */
    public Optional<Integer> leafAsInt(String name) {
        JsonObject obj = structure.value().asJsonObject();
        return obj.containsKey(name)
            ? Optional.of(obj.getInt(name, 0))
            : Optional.empty();
    }

    /**
     * Method to get a {@code double} type field of this JSON.
     * @param name Name of the field to return.
     * @return Optional value of the field.
     */
    public Optional<Double> leafAsDouble(String name) {
        return Optional.of(structure.value().asJsonObject())
            .filter(o -> o.containsKey(name))
            .filter(
                o -> o.get(name).getValueType() == JsonValue.ValueType.NUMBER
            )
            .map(o -> o.getJsonNumber(name).doubleValue());
    }

    /**
     * Method to get a {@code boolean} type field of this JSON.
     * @param name Name of the field to return.
     * @return Optional value of the field.
     */
    public Optional<Boolean> leafAsBool(String name) {
        JsonObject obj = structure.value().asJsonObject();
        return obj.containsKey(name)
            ? Optional.of(obj.getBoolean(name, false))
            : Optional.empty();
    }

    /**
     * Represent this JSON as {@link JsonStructure} in case full JSON
     * manipulation capabilities offered by jackson-databind library are needed.
     * @return This JSON as {@link JsonStructure}.
     */
    public JsonStructure jsonStructure() {
        return structure.value();
    }

    /**
     * Method to get a JSON nested within this JSON, specified by path. Path
     * starts with a forward slash, and path elements are separated by forward
     * slashes also, e.g.
     * <pre>
     * {@code
     * SmartJson nested = json.at("/path/to/nested/json");}
     * </pre>
     * This method never returns null. If there is no JSON as specified by the
     * path, a missing JSON is returned, i.e.
     * {@code returned.isMissing() == true}.
     * @param path Path to the nested JSON.
     * @return The nested JSON, which could be missing.
     */
    public SmartJson at(String path) {
        JsonObject whole = structure.value().asJsonObject();
        JsonValue target;
        try {
            target = whole.getValue(path);
        } catch (JsonException e) {
            return new SmartJson(new MissingJson());
        }
        return new SmartJson(new Json.Of(target.asJsonObject()));
    }

    /**
     * Method which tells if this JSON is missing.
     * @return true if this JSON is missing; otherwise false.
     */
    public boolean isMissing() {
        return byteArray().length == 0;
    }
}
