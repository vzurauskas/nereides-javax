package com.vzurauskas.nereides.javax;

import java.io.InputStream;
import javax.json.JsonObject;
import javax.json.JsonPatchBuilder;

/**
 * JSON which is mutable and can be used to build custom JSONs, e.g.
 * <pre>
 * {@code
 * new MutableJson().with(
 *     "ocean",
 *     new MutableJson().with(
 *         "nereid",
 *         new MutableJson()
 *             .with("hair", "black")
 *             .with("age", 100)
 *             .with("fair", true)
 *     )
 * )
 * }
 * </pre>
 */
public final class MutableJson implements Json {

    private JsonObject base;
    private final JsonPatchBuilder patch;

    /**
     * Constructor.
     */
    public MutableJson() {
        this(
            javax.json.Json.createObjectBuilder().build(),
            javax.json.Json.createPatchBuilder()
        );
    }

    /**
     * Constructor.
     * @param base The base JSON to build upon.
     */
    public MutableJson(Json base) {
        this(
            (JsonObject) javax.json.Json.createReader(base.bytes()).read(),
            javax.json.Json.createPatchBuilder()
        );
    }

    private MutableJson(JsonObject base, JsonPatchBuilder patch) {
        this.base = base;
        this.patch = patch;
    }

    @Override
    public InputStream bytes() {
        this.base = patch.build().apply(base);
        return new Json.Of(base).bytes();
    }

    /**
     * Add a {@code String} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, String value) {
        patch.add('/' + name, value);
        return this;
    }

    /**
     * Add an {@code int} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, int value) {
        patch.add('/' + name, value);
        return this;
    }

    /**
     * Add a {@code double} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, double value) {
        patch.add('/' + name, javax.json.Json.createValue(value));
        return this;
    }

    /**
     * Add a {@code boolean} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, boolean value) {
        patch.add('/' + name, value);
        return this;
    }

    /**
     * Add a {@link Json} field to this JSON. If the added {@link Json} is a,
     * other fields can be added to it, thus enabling nesting.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, Json value) {
        patch.add('/' + name, new SmartJson(value).jsonStructure());
        return this;
    }
}
