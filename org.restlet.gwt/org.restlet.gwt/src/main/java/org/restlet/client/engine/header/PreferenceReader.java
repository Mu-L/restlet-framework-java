/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.header;

import static org.restlet.client.engine.header.HeaderUtils.isComma;
import static org.restlet.client.engine.header.HeaderUtils.isDoubleQuote;
import static org.restlet.client.engine.header.HeaderUtils.isSpace;
import static org.restlet.client.engine.header.HeaderUtils.isText;
import static org.restlet.client.engine.header.HeaderUtils.isTokenChar;

import java.io.IOException;
import java.util.Iterator;

import org.restlet.client.data.CharacterSet;
import org.restlet.client.data.ClientInfo;
import org.restlet.client.data.Encoding;
import org.restlet.client.data.Language;
import org.restlet.client.data.MediaType;
import org.restlet.client.data.Metadata;
import org.restlet.client.data.Parameter;
import org.restlet.client.data.Preference;
import org.restlet.client.util.Series;

/**
 * Preference header reader. Works for character sets, encodings, languages or
 * media types.
 * 
 * @author Jerome Louvel
 */
public class PreferenceReader<T extends Metadata> extends
        HeaderReader<Preference<T>> {

    public static final int TYPE_CHARACTER_SET = 1;

    public static final int TYPE_ENCODING = 2;

    public static final int TYPE_LANGUAGE = 3;

    public static final int TYPE_MEDIA_TYPE = 4;

    public static final int TYPE_PATCH = 5;

    /**
     * Parses character set preferences from a header.
     * 
     * @param acceptCharsetHeader
     *            The header to parse.
     * @param clientInfo
     *            The client info to update.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addCharacterSets(String acceptCharsetHeader,
            ClientInfo clientInfo) {
        if (acceptCharsetHeader != null) {
            // Implementation according to
            // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.2
            if (acceptCharsetHeader.length() == 0) {
                clientInfo.getAcceptedCharacterSets().add(
                        new Preference<CharacterSet>(CharacterSet.ISO_8859_1));
            } else {
                PreferenceReader pr = new PreferenceReader(
                        PreferenceReader.TYPE_CHARACTER_SET,
                        acceptCharsetHeader);
                pr.addValues(clientInfo.getAcceptedCharacterSets());
            }
        } else {
            clientInfo.getAcceptedCharacterSets().add(
                    new Preference(CharacterSet.ALL));
        }
    }

    /**
     * Parses encoding preferences from a header.
     * 
     * @param acceptEncodingHeader
     *            The header to parse.
     * @param clientInfo
     *            The client info to update.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addEncodings(String acceptEncodingHeader,
            ClientInfo clientInfo) {
        if (acceptEncodingHeader != null) {
            PreferenceReader pr = new PreferenceReader(
                    PreferenceReader.TYPE_ENCODING, acceptEncodingHeader);
            pr.addValues(clientInfo.getAcceptedEncodings());
        } else {
            clientInfo.getAcceptedEncodings().add(
                    new Preference(Encoding.IDENTITY));
        }
    }

    /**
     * Adds language preferences from a header.
     * 
     * @param acceptLanguageHeader
     *            The header to parse.
     * @param clientInfo
     *            The client info to update.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addLanguages(String acceptLanguageHeader,
            ClientInfo clientInfo) {
        if (acceptLanguageHeader != null) {
            PreferenceReader pr = new PreferenceReader(
                    PreferenceReader.TYPE_LANGUAGE, acceptLanguageHeader);
            pr.addValues(clientInfo.getAcceptedLanguages());
        } else {
            clientInfo.getAcceptedLanguages().add(new Preference(Language.ALL));
        }
    }

    /**
     * Parses media type preferences from a header.
     * 
     * @param acceptMediaTypeHeader
     *            The header to parse.
     * @param clientInfo
     *            The client info to update.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addMediaTypes(String acceptMediaTypeHeader,
            ClientInfo clientInfo) {
        if (acceptMediaTypeHeader != null) {
            PreferenceReader pr = new PreferenceReader(
                    PreferenceReader.TYPE_MEDIA_TYPE, acceptMediaTypeHeader);
            pr.addValues(clientInfo.getAcceptedMediaTypes());
        } else {
            clientInfo.getAcceptedMediaTypes().add(
                    new Preference(MediaType.ALL));
        }
    }

    /**
     * Parses patch preferences from a header.
     * 
     * @param acceptPatchHeader
     *            The header to parse.
     * @param clientInfo
     *            The client info to update.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addPatches(String acceptPatchHeader,
            ClientInfo clientInfo) {
        if (acceptPatchHeader != null) {
            PreferenceReader pr = new PreferenceReader(
                    PreferenceReader.TYPE_PATCH, acceptPatchHeader);
            pr.addValues(clientInfo.getAcceptedPatches());
        }
    }

    /**
     * Parses a quality value.<br>
     * If the quality is invalid, an IllegalArgumentException is thrown.
     * 
     * @param quality
     *            The quality value as a string.
     * @return The parsed quality value as a float.
     */
    public static float readQuality(String quality) {
        try {
            float result = Float.valueOf(quality);

            if (PreferenceWriter.isValidQuality(result)) {
                return result;
            }

            throw new IllegalArgumentException(
                    "Invalid quality value detected. Value must be between 0 and 1.");
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(
                    "Invalid quality value detected. Value must be between 0 and 1.");
        }
    }

    /** The type of metadata read. */
    private volatile int type;

    /**
     * Constructor.
     * 
     * @param type
     *            The type of metadata read.
     * @param header
     *            The header to read.
     */
    public PreferenceReader(int type, String header) {
        super(header);
        this.type = type;
    }

    /**
     * Creates a new preference.
     * 
     * @param metadata
     *            The metadata name.
     * @param parameters
     *            The parameters list.
     * @return The new preference.
     */
    @SuppressWarnings("unchecked")
    protected Preference<T> createPreference(CharSequence metadata,
            Series<Parameter> parameters) {
        Preference<T> result;

        if (parameters == null) {
            result = new Preference<T>();

            switch (this.type) {
            case TYPE_CHARACTER_SET:
                result.setMetadata((T) CharacterSet.valueOf(metadata.toString()));
                break;

            case TYPE_ENCODING:
                result.setMetadata((T) Encoding.valueOf(metadata.toString()));
                break;

            case TYPE_LANGUAGE:
                result.setMetadata((T) Language.valueOf(metadata.toString()));
                break;

            case TYPE_MEDIA_TYPE:
            case TYPE_PATCH:
                result.setMetadata((T) MediaType.valueOf(metadata.toString()));
                break;
            }
        } else {
            final Series<Parameter> mediaParams = extractMediaParams(parameters);
            final float quality = extractQuality(parameters);
            result = new Preference<T>(null, quality, parameters);

            switch (this.type) {
            case TYPE_CHARACTER_SET:
                result.setMetadata((T) new CharacterSet(metadata.toString()));
                break;

            case TYPE_ENCODING:
                result.setMetadata((T) new Encoding(metadata.toString()));
                break;

            case TYPE_LANGUAGE:
                result.setMetadata((T) new Language(metadata.toString()));
                break;

            case TYPE_MEDIA_TYPE:
            case TYPE_PATCH:
                result.setMetadata((T) new MediaType(metadata.toString(),
                        mediaParams));
                break;
            }
        }

        return result;
    }

    /**
     * Extract the media parameters. Only leave as the quality parameter if
     * found. Modifies the parameters list.
     * 
     * @param parameters
     *            All the preference parameters.
     * @return The media parameters.
     */
    protected Series<Parameter> extractMediaParams(Series<Parameter> parameters) {
        Series<Parameter> result = null;
        boolean qualityFound = false;
        Parameter param = null;

        if (parameters != null) {
             result = new org.restlet.client.engine.util.ParameterSeries();

            for (final Iterator<Parameter> iter = parameters.iterator(); !qualityFound
                    && iter.hasNext();) {
                param = iter.next();

                if (param.getName().equals("q")) {
                    qualityFound = true;
                } else {
                    iter.remove();
                    result.add(param);
                }
            }
        }

        return result;
    }

    /**
     * Extract the quality value. If the value is not found, 1 is returned.
     * 
     * @param parameters
     *            The preference parameters.
     * @return The quality value.
     */
    protected float extractQuality(Series<Parameter> parameters) {
        float result = 1F;
        boolean found = false;

        if (parameters != null) {
            Parameter param = null;

            for (final Iterator<Parameter> iter = parameters.iterator(); !found
                    && iter.hasNext();) {
                param = iter.next();
                if (param.getName().equals("q")) {
                    result = readQuality(param.getValue());
                    found = true;

                    // Remove the quality parameter as we will directly store it
                    // in the Preference object
                    iter.remove();
                }
            }
        }

        return result;
    }

    /**
     * Read the next preference.
     * 
     * @return The next preference.
     */
    public Preference<T> readValue() throws IOException {
        Preference<T> result = null;

        boolean readingMetadata = true;
        boolean readingParamName = false;
        boolean readingParamValue = false;

        StringBuilder metadataBuffer = new StringBuilder();
        StringBuilder paramNameBuffer = null;
        StringBuilder paramValueBuffer = null;

        Series<Parameter> parameters = null;
        int next = 0;

        while (result == null) {
            next = read();

            if (readingMetadata) {
                if ((next == -1) || isComma(next)) {
                    if (metadataBuffer.length() > 0) {
                        // End of metadata section
                        // No parameters detected
                        result = createPreference(metadataBuffer, null);
                    } else {
                        // Ignore empty metadata name
                        break;
                    }
                } else if (next == ';') {
                    if (metadataBuffer.length() > 0) {
                        // End of metadata section
                        // Parameters detected
                        readingMetadata = false;
                        readingParamName = true;
                        paramNameBuffer = new StringBuilder();
                         parameters = new
                         org.restlet.client.engine.util.ParameterSeries();
                    } else {
                        throw new IOException("Empty metadata name detected.");
                    }
                } else if (isSpace(next)) {
                    // Ignore spaces
                } else if (isText(next)) {
                    metadataBuffer.append((char) next);
                } else {
                    throw new IOException("Unexpected character \""
                            + (char) next + "\" detected.");
                }
            } else if (readingParamName) {
                if (next == '=') {
                    if (paramNameBuffer.length() > 0) {
                        // End of parameter name section
                        readingParamName = false;
                        readingParamValue = true;
                        paramValueBuffer = new StringBuilder();
                    } else {
                        throw new IOException("Empty parameter name detected.");
                    }
                } else if ((next == -1) || isComma(next)) {
                    if (paramNameBuffer.length() > 0) {
                        // End of parameters section
                        parameters.add(Parameter.create(paramNameBuffer, null));
                        result = createPreference(metadataBuffer, parameters);
                    } else {
                        throw new IOException("Empty parameter name detected.");
                    }
                } else if (next == ';') {
                    // End of parameter
                    parameters.add(Parameter.create(paramNameBuffer, null));
                    paramNameBuffer = new StringBuilder();
                    readingParamName = true;
                    readingParamValue = false;
                } else if (isSpace(next) && (paramNameBuffer.length() == 0)) {
                    // Ignore white spaces
                } else if (isTokenChar(next)) {
                    paramNameBuffer.append((char) next);
                } else {
                    throw new IOException("Unexpected character \""
                            + (char) next + "\" detected.");
                }
            } else if (readingParamValue) {
                if ((next == -1) || isComma(next) || isSpace(next)) {
                    if (paramValueBuffer.length() > 0) {
                        // End of parameters section
                        parameters.add(Parameter.create(paramNameBuffer,
                                paramValueBuffer));
                        result = createPreference(metadataBuffer, parameters);
                    } else {
                        throw new IOException("Empty parameter value detected");
                    }
                } else if (next == ';') {
                    // End of parameter
                    parameters.add(Parameter.create(paramNameBuffer,
                            paramValueBuffer));
                    paramNameBuffer = new StringBuilder();
                    readingParamName = true;
                    readingParamValue = false;
                } else if ((next == '"') && (paramValueBuffer.length() == 0)) {
                    // Parse the quoted string
                    boolean done = false;
                    boolean quotedPair = false;

                    while ((!done) && (next != -1)) {
                        next = read();

                        if (quotedPair) {
                            // End of quoted pair (escape sequence)
                            if (isText(next)) {
                                paramValueBuffer.append((char) next);
                                quotedPair = false;
                            } else {
                                throw new IOException(
                                        "Invalid character detected in quoted string. Please check your value");
                            }
                        } else if (isDoubleQuote(next)) {
                            // End of quoted string
                            done = true;
                        } else if (next == '\\') {
                            // Begin of quoted pair (escape sequence)
                            quotedPair = true;
                        } else if (isText(next)) {
                            paramValueBuffer.append((char) next);
                        } else {
                            throw new IOException(
                                    "Invalid character detected in quoted string. Please check your value");
                        }
                    }
                } else if (isTokenChar(next)) {
                    paramValueBuffer.append((char) next);
                } else {
                    throw new IOException("Unexpected character \""
                            + (char) next + "\" detected.");
                }
            }
        }

        if (isComma(next)) {
            // Unread character which isn't part of the value
            unread();
        }

        return result;
    }
}
