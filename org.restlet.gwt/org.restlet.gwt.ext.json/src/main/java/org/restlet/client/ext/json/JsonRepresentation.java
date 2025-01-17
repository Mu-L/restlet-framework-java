/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.ext.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.restlet.client.data.CharacterSet;
import org.restlet.client.data.MediaType;
import org.restlet.client.engine.io.StringInputStream;
import org.restlet.client.representation.Representation;
import org.restlet.client.representation.StringRepresentation;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * JSON representation based on an JSON value. JSON (JavaScript Object Notation)
 * is a common serialization format similar to XML but lighter.
 * 
 * @see <a href="http://www.json.org">JSON<a/>
 * @author Jerome Louvel
 */
public class JsonRepresentation extends Representation {
    /** The source JSON representation. */
    private Representation jsonRepresentation;

    /** The wrapped JSON value. */
    private JSONValue value;

    /**
     * Constructor for an empty document.
     * 
     * @param mediaType
     *            The representation's media type.
     */
    public JsonRepresentation(MediaType mediaType) {
        super(mediaType);
        this.value = JSONNull.getInstance();
    }

    /**
     * Constructor from an existing DOM document.
     * 
     * @param mediaType
     *            The representation's media type.
     * @param value
     *            The source JSON value.
     */
    public JsonRepresentation(MediaType mediaType, JSONValue value) {
        super(mediaType);
        this.value = value;
    }

    /**
     * Constructor.
     * 
     * @param jsonRepresentation
     *            A source JSON representation to parse.
     */
    public JsonRepresentation(Representation jsonRepresentation) {
        super((jsonRepresentation == null) ? null : jsonRepresentation
                .getMediaType());
        this.jsonRepresentation = jsonRepresentation;
    }

    /**
     * Constructor from a JSON string.
     * 
     * @param jsonString
     *            The JSON string.
     */
    public JsonRepresentation(String jsonString) {
        super(MediaType.APPLICATION_JSON);
        setCharacterSet(CharacterSet.UTF_8);
        this.jsonRepresentation = new StringRepresentation(jsonString);
    }

    /**
     * Converts the representation to a JSON array. This method might trigger
     * the parsing of the wrapped JSON textual representation.
     * 
     * @return The converted JSON array.
     * @throws IOException
     */
    public JSONArray getJsonArray() throws IOException {
        if (getValue() != null) {
            return getValue().isArray();
        }

        return null;
    }

    /**
     * Converts the representation to a JSON Boolean. This method might trigger
     * the parsing of the wrapped JSON textual representation.
     * 
     * @return The converted JSON Boolean.
     * @throws IOException
     */
    public JSONBoolean getJsonBoolean() throws IOException {
        if (getValue() != null) {
            return getValue().isBoolean();
        }

        return null;
    }

    /**
     * Converts the representation to a JSON null value. This method might
     * trigger the parsing of the wrapped JSON textual representation.
     * 
     * @return The converted JSON null value.
     * @throws IOException
     */
    public JSONNull getJsonNull() throws IOException {
        if (getValue() != null) {
            return getValue().isNull();
        }

        return null;
    }

    /**
     * Converts the representation to a JSON object. This method might trigger
     * the parsing of the wrapped JSON textual representation.
     * 
     * @return The converted JSON object.
     * @throws IOException
     */
    public JSONObject getJsonObject() throws IOException {
        if (getValue() != null) {
            return getValue().isObject();
        }

        return null;
    }

    /**
     * Converts the representation to a JSON string. This method might trigger
     * the parsing of the wrapped JSON textual representation.
     * 
     * @return The converted JSON string.
     * @throws IOException
     */
    public JSONString getJsonString() throws IOException {
        if (getValue() != null) {
            return getValue().isString();
        }

        return null;
    }

    @Override
    public StringReader getReader() throws IOException {
        return new StringReader(getText());
    }

    @Override
    public long getSize() {
        if (this.jsonRepresentation != null) {
            return this.jsonRepresentation.getSize();
        }
        return super.getSize();
    }

    @Override
    public InputStream getStream() throws IOException {
        return new StringInputStream(getValue().toString());
    }

    @Override
    public String getText() throws IOException {
        return (getValue() != null) ? getValue().toString() : null;
    }

    /**
     * Returns the wrapped JSON value. If no value is defined yet, it attempts
     * to parse the JSON representation eventually given at construction time.
     * Otherwise, it just creates a null JSON value.
     * 
     * @return The wrapped DOM document.
     * @throws IOException
     */
    public JSONValue getValue() throws IOException {
        if (this.value == null) {
            if (this.jsonRepresentation != null) {
                this.value = JSONParser
                        .parse(this.jsonRepresentation.getText());
            } else {
                this.value = JSONNull.getInstance();
            }
        }

        return this.value;
    }

    /**
     * Releases the wrapped JSON value and the source JSON representation if
     * they have been defined.
     */
    @Override
    public void release() {
        setValue(null);

        if (this.jsonRepresentation != null) {
            this.jsonRepresentation.release();
        }

        super.release();
    }

    /**
     * Sets the wrapped JSON value.
     * 
     * @param json
     *            The wrapped JSON value.
     */
    public void setValue(JSONValue json) {
        this.value = json;
    }
}
