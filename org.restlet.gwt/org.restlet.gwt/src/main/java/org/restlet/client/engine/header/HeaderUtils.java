/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.client.engine.header;

import static java.lang.Boolean.parseBoolean;
import static java.util.logging.Level.WARNING;
import static org.restlet.client.data.Disposition.TYPE_NONE;
import static org.restlet.client.data.Range.RANGE_BYTES_UNIT;
import static org.restlet.client.data.Range.isBytesRange;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCEPT;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCEPT_CHARSET;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCEPT_ENCODING;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCEPT_LANGUAGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCEPT_PATCH;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCEPT_RANGES;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_ALLOW_HEADERS;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_ALLOW_METHODS;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_MAX_AGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_REQUEST_HEADERS;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ACCESS_CONTROL_REQUEST_METHOD;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_AGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ALLOW;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_AUTHENTICATION_INFO;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_AUTHORIZATION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CACHE_CONTROL;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONNECTION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_DISPOSITION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_ENCODING;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_LANGUAGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_LENGTH;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_LOCATION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_MD5;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_RANGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_CONTENT_TYPE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_COOKIE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_DATE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_ETAG;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_EXPECT;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_EXPIRES;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_FROM;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_HOST;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_IF_MATCH;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_IF_MODIFIED_SINCE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_IF_NONE_MATCH;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_IF_RANGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_IF_UNMODIFIED_SINCE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_LAST_MODIFIED;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_LOCATION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_MAX_FORWARDS;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_PRAGMA;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_PROXY_AUTHENTICATE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_PROXY_AUTHORIZATION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_RANGE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_REFERRER;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_RETRY_AFTER;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_SERVER;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_SET_COOKIE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_SET_COOKIE2;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_TRAILER;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_TRANSFER_ENCODING;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_TRANSFER_EXTENSION;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_UPGRADE;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_USER_AGENT;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_VARY;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_VIA;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_WARNING;
import static org.restlet.client.engine.header.HeaderConstants.HEADER_WWW_AUTHENTICATE;
import static org.restlet.client.engine.util.StringUtils.isNullOrEmpty;
import static org.restlet.client.representation.Representation.UNKNOWN_SIZE;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;

import org.restlet.client.Context;
import org.restlet.client.Message;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.data.ClientInfo;
import org.restlet.client.data.Conditions;
import org.restlet.client.data.Header;
import org.restlet.client.data.MediaType;
import org.restlet.client.data.Range;
import org.restlet.client.data.Reference;
import org.restlet.client.data.Tag;
import org.restlet.client.engine.util.CaseInsensitiveHashSet;
import org.restlet.client.engine.util.DateUtils;
import org.restlet.client.representation.EmptyRepresentation;
import org.restlet.client.representation.Representation;
import org.restlet.client.util.Series;

/**
 * HTTP-style header utilities.
 * 
 * @author Jerome Louvel
 */
public class HeaderUtils {

    /**
     * Standard set of headers which cannot be modified.
     */
    private static final Set<String> STANDARD_HEADERS = Collections
            .unmodifiableSet(new CaseInsensitiveHashSet(Arrays.asList(
                    HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    HEADER_ACCESS_CONTROL_ALLOW_HEADERS,
                    HEADER_ACCESS_CONTROL_ALLOW_METHODS,
                    HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                    HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
                    HEADER_ACCESS_CONTROL_MAX_AGE,
                    HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                    HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                    HEADER_ACCEPT,
                    HEADER_ACCEPT_CHARSET,
                    HEADER_ACCEPT_ENCODING,
                    HEADER_ACCEPT_LANGUAGE,
                    HEADER_ACCEPT_PATCH,
                    HEADER_ACCEPT_RANGES,
                    HEADER_AGE,
                    HEADER_ALLOW,
                    HEADER_AUTHENTICATION_INFO,
                    HEADER_AUTHORIZATION,
                    HEADER_CACHE_CONTROL,
                    HEADER_CONNECTION,
                    HEADER_CONTENT_DISPOSITION,
                    HEADER_CONTENT_ENCODING,
                    HEADER_CONTENT_LANGUAGE,
                    HEADER_CONTENT_LENGTH,
                    HEADER_CONTENT_LOCATION,
                    HEADER_CONTENT_MD5,
                    HEADER_CONTENT_RANGE,
                    HEADER_CONTENT_TYPE,
                    HEADER_COOKIE,
                    HEADER_DATE,
                    HEADER_ETAG,
                    HEADER_EXPECT,
                    HEADER_EXPIRES,
                    HEADER_FROM,
                    HEADER_HOST,
                    HEADER_IF_MATCH,
                    HEADER_IF_MODIFIED_SINCE,
                    HEADER_IF_NONE_MATCH,
                    HEADER_IF_RANGE,
                    HEADER_IF_UNMODIFIED_SINCE,
                    HEADER_LAST_MODIFIED,
                    HEADER_LOCATION,
                    HEADER_MAX_FORWARDS,
                    HEADER_PROXY_AUTHENTICATE,
                    HEADER_PROXY_AUTHORIZATION,
                    HEADER_RANGE,
                    HEADER_REFERRER,
                    HEADER_RETRY_AFTER,
                    HEADER_SERVER,
                    HEADER_SET_COOKIE,
                    HEADER_SET_COOKIE2,
                    HEADER_USER_AGENT,
                    HEADER_VARY,
                    HEADER_VIA,
                    HEADER_WARNING,
                    HEADER_WWW_AUTHENTICATE)));

    /**
     * Set of unsupported headers that will be covered in future versions.
     */
    private static final Set<String> UNSUPPORTED_STANDARD_HEADERS = Collections
            .unmodifiableSet(new CaseInsensitiveHashSet(Arrays.asList(
                    HEADER_PRAGMA,
                    HEADER_TRAILER,
                    HEADER_TRANSFER_ENCODING,
                    HEADER_TRANSFER_EXTENSION,
                    HEADER_UPGRADE)));

    /**
     * Adds the entity headers based on the {@link Representation} to the {@link Series}.
     * 
     * @param entity
     *            The source entity {@link Representation}.
     * @param headers
     *            The target headers {@link Series}.
     */
    public static void addEntityHeaders(Representation entity,
            Series<Header> headers) {
        if (entity == null || !entity.isAvailable()) {
            addHeader(HEADER_CONTENT_LENGTH, "0", headers);
        } else if (entity.getAvailableSize() != UNKNOWN_SIZE) {
            addHeader(HEADER_CONTENT_LENGTH, Long.toString(entity.getAvailableSize()), headers);
        }

        if (entity != null) {
            addHeader(HEADER_CONTENT_ENCODING, EncodingWriter.write(entity.getEncodings()), headers);
            addHeader(HEADER_CONTENT_LANGUAGE, LanguageWriter.write(entity.getLanguages()), headers);

            if (entity.getLocationRef() != null) {
                addHeader(HEADER_CONTENT_LOCATION, entity.getLocationRef().getTargetRef().toString(), headers);
            }


            if (entity.getRange() != null) {
                Range range = entity.getRange();
                if (isBytesRange(range)) {
                    addHeader(HEADER_CONTENT_RANGE, RangeWriter.write(range, entity.getSize()), headers);
                } else {
                    addHeader(HEADER_CONTENT_RANGE, RangeWriter.write(range, range.getInstanceSize()), headers);
                }
            }

            if (entity.getMediaType() != null) {
                addHeader(HEADER_CONTENT_TYPE, ContentType.writeHeader(entity), headers);
            }

            if (entity.getExpirationDate() != null) {
                addHeader(HEADER_EXPIRES, DateWriter.write(entity.getExpirationDate()), headers);
            }

            if (entity.getModificationDate() != null) {
                addHeader(HEADER_LAST_MODIFIED,
                        DateWriter.write(entity.getModificationDate()), headers);
            }

            if (entity.getTag() != null) {
                addHeader(HEADER_ETAG,
                        TagWriter.write(entity.getTag()), headers);
            }

            if (entity.getDisposition() != null
                    && !TYPE_NONE.equals(entity.getDisposition().getType())) {
                addHeader(HEADER_CONTENT_DISPOSITION,
                        DispositionWriter.write(entity.getDisposition()),
                        headers);
            }
        }
    }

    /**
     * Adds extension headers if they are non-standard headers.
     * 
     * @param existingHeaders
     *            The headers to update.
     * @param additionalHeaders
     *            The headers to add.
     */
    public static void addExtensionHeaders(Series<Header> existingHeaders,
            Series<Header> additionalHeaders) {
        if (additionalHeaders != null) {
            for (Header param : additionalHeaders) {
                if (STANDARD_HEADERS.contains(param.getName())) {
                    // Standard headers that can't be overridden
                    Context.getCurrentLogger()
                            .warning(
                                    "Addition of the standard header \""
                                            + param.getName()
                                            + "\" is not allowed. Please use the equivalent property in the Restlet API.");
                } else if (UNSUPPORTED_STANDARD_HEADERS.contains(param.getName())) {
                    Context.getCurrentLogger()
                            .warning(
                                    "Addition of the standard header \""
                                            + param.getName()
                                            + "\" is discouraged as a future version of the Restlet API will directly support it.");
                    existingHeaders.add(param);
                } else {
                    existingHeaders.add(param);
                }
            }
        }
    }

    /**
     * Adds the general headers from the {@link Message} to the {@link Series}.
     * 
     * @param message
     *            The source {@link Message}.
     * @param headers
     *            The target headers {@link Series}.
     */
    public static void addGeneralHeaders(Message message, Series<Header> headers) {
        addHeader(HEADER_CACHE_CONTROL,
                CacheDirectiveWriter.write(message.getCacheDirectives()),
                headers);

        if (message.getDate() == null) {
            message.setDate(new Date());
        }

        addHeader(HEADER_DATE, DateWriter.write(message.getDate()), headers);

        addHeader(HEADER_VIA, RecipientInfoWriter.write(message.getRecipientsInfo()), headers);

        addHeader(HEADER_WARNING, WarningWriter.write(message.getWarnings()), headers);
    }

    /**
     * Adds a header to the given list. Checks for exceptions and logs them.
     * 
     * @param headerName
     *            The header name.
     * @param headerValue
     *            The header value.
     * @param headers
     *            The headers list.
     */
    public static void addHeader(String headerName, String headerValue,
            Series<Header> headers) {
        if (headerName != null && !isNullOrEmpty(headerValue)) {
            try {
                headers.add(headerName, headerValue);
            } catch (Throwable t) {
                Context.getCurrentLogger().log(Level.WARNING,
                        "Unable to format the " + headerName + " header", t);
            }
        }
    }

    /**
     * Adds the entity headers based on the {@link Representation} to the {@link Series} when a 304 (Not Modified)
     * status is returned.
     * 
     * @param entity
     *            The source entity {@link Representation}.
     * @param headers
     *            The target headers {@link Series}.
     */
    public static void addNotModifiedEntityHeaders(Representation entity,
            Series<Header> headers) {
        if (entity != null) {
            if (entity.getTag() != null) {
                addHeader(HEADER_ETAG, TagWriter.write(entity.getTag()), headers);
            }

            if (entity.getLocationRef() != null) {
                addHeader(HEADER_CONTENT_LOCATION,
                        entity.getLocationRef().getTargetRef().toString(),
                        headers);
            }
        }
    }

    /**
     * Adds the headers based on the {@link Request} to the given {@link Series} .
     * 
     * @param request
     *            The {@link Request} to copy the headers from.
     * @param headers
     *            The {@link Series} to copy the headers to.
     */
    public static void addRequestHeaders(Request request, Series<Header> headers) {
        ClientInfo clientInfo = request.getClientInfo();

        if (!clientInfo.getAcceptedMediaTypes().isEmpty()) {
            addHeader(HEADER_ACCEPT,
                    PreferenceWriter.write(clientInfo.getAcceptedMediaTypes()),
                    headers);
        } else {
            addHeader(HEADER_ACCEPT, MediaType.ALL.getName(), headers);
        }

        if (!clientInfo.getAcceptedCharacterSets().isEmpty()) {
            addHeader(HEADER_ACCEPT_CHARSET,
                    PreferenceWriter.write(clientInfo
                            .getAcceptedCharacterSets()), headers);
        }

        if (!clientInfo.getAcceptedEncodings().isEmpty()) {
            addHeader(HEADER_ACCEPT_ENCODING,
                    PreferenceWriter.write(clientInfo.getAcceptedEncodings()),
                    headers);
        }

        if (!clientInfo.getAcceptedLanguages().isEmpty()) {
            addHeader(HEADER_ACCEPT_LANGUAGE,
                    PreferenceWriter.write(clientInfo.getAcceptedLanguages()),
                    headers);
        }

        if (!clientInfo.getAcceptedPatches().isEmpty()) {
            addHeader(HEADER_ACCEPT_PATCH,
                    PreferenceWriter.write(clientInfo.getAcceptedPatches()),
                    headers);
        }


        if (clientInfo.getFrom() != null) {
            addHeader(HEADER_FROM, request.getClientInfo().getFrom(), headers);
        }

        // Manually add the host name and port when it is potentially
        // different from the one specified in the target resource reference.
        Reference hostRef = (request.getResourceRef().getBaseRef() != null) ? request
                .getResourceRef().getBaseRef() : request.getResourceRef();

        if (hostRef.getHostDomain() != null) {
            String host = hostRef.getHostDomain();
            int hostRefPortValue = hostRef.getHostPort();

            if ((hostRefPortValue != -1)
                    && (hostRefPortValue != request.getProtocol().getDefaultPort())) {
                host = host + ':' + hostRefPortValue;
            }

            addHeader(HEADER_HOST, host, headers);
        }

        Conditions conditions = request.getConditions();
        addHeader(HEADER_IF_MATCH, TagWriter.write(conditions.getMatch()), headers);
        addHeader(HEADER_IF_NONE_MATCH, TagWriter.write(conditions.getNoneMatch()), headers);

        if (conditions.getModifiedSince() != null) {
            addHeader(HEADER_IF_MODIFIED_SINCE, DateWriter.write(conditions.getModifiedSince()), headers);
        }

        if (conditions.getRangeTag() != null
                && conditions.getRangeDate() != null) {
            Context.getCurrentLogger()
                    .log(WARNING,
                            "Unable to format the HTTP If-Range header due to the presence of both entity tag and modification date.");
        } else if (conditions.getRangeTag() != null) {
            addHeader(HEADER_IF_RANGE, TagWriter.write(conditions.getRangeTag()), headers);
        } else if (conditions.getRangeDate() != null) {
            addHeader(HEADER_IF_RANGE, DateWriter.write(conditions.getRangeDate()), headers);
        }

        if (conditions.getUnmodifiedSince() != null) {
            addHeader(HEADER_IF_UNMODIFIED_SINCE, DateWriter.write(conditions.getUnmodifiedSince()), headers);
        }

        if (request.getMaxForwards() > -1) {
            addHeader(HEADER_MAX_FORWARDS, Integer.toString(request.getMaxForwards()), headers);
        }

        if (!request.getRanges().isEmpty()) {
            addHeader(HEADER_RANGE, RangeWriter.write(request.getRanges()), headers);
        }

        if (request.getReferrerRef() != null) {
            addHeader(HEADER_REFERRER, request.getReferrerRef().toString(), headers);
        }

        if (request.getClientInfo().getAgent() != null) {
            addHeader(HEADER_USER_AGENT, request.getClientInfo().getAgent(), headers);
        }


        // CORS headers

        if (request.getAccessControlRequestHeaders() != null) {
            addHeader(
                    HEADER_ACCESS_CONTROL_REQUEST_HEADERS,
                    StringWriter.write(request.getAccessControlRequestHeaders()),
                    headers);
        }

        if (request.getAccessControlRequestMethod() != null) {
            addHeader(HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                    request.getAccessControlRequestMethod().getName(), headers);
        }

        // ----------------------------------
        // 3) Add supported extension headers
        // ----------------------------------

        if (!request.getCookies().isEmpty()) {
            addHeader(HEADER_COOKIE, CookieWriter.write(request.getCookies()), headers);
        }

        // -------------------------------------
        // 4) Add user-defined extension headers
        // -------------------------------------

        Series<Header> additionalHeaders = request.getHeaders();
        addExtensionHeaders(headers, additionalHeaders);

        // ---------------------------------------
        // 5) Add authorization headers at the end
        // ---------------------------------------

    }


    /**
     * Remove the headers that are mapped to the framework's API from the given message's list of headers.
     * 
     * @param message
     *            The message to update.
     */
    public static void keepExtensionHeadersOnly(Message message) {
        Series<Header> headers = message.getHeaders();
         Series<Header> extensionHeaders = new org.restlet.client.engine.util.HeaderSeries();
        for (Header header : headers) {
            if (!STANDARD_HEADERS.contains(header.getName())) {
                extensionHeaders.add(header);
            }
        }
        message.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, extensionHeaders);
    }

    /**
     * Copies extension headers into a request or a response.
     * 
     * @param headers
     *            The headers to copy.
     * @param message
     *            The message to update.
     */
    public static void copyExtensionHeaders(Series<Header> headers, Message message) {
        if (headers != null) {
            Series<Header> extensionHeaders = message.getHeaders();
            for (Header header : headers) {
                if (!STANDARD_HEADERS.contains(header.getName())) {
                    extensionHeaders.add(header);
                }
            }
        }
    }

    /**
     * Copies headers into a response.
     * 
     * @param headers
     *            The headers to copy.
     * @param response
     *            The response to update.
     */
    public static void copyResponseTransportHeaders(Series<Header> headers, Response response) {
        if (headers != null) {
            for (Header header : headers) {
                if (HEADER_LOCATION.equalsIgnoreCase(header.getName())) {
                    response.setLocationRef(header.getValue());
                } else if (HEADER_AGE.equalsIgnoreCase(header.getName())) {
                    try {
                        response.setAge(Integer.parseInt(header.getValue()));
                    } catch (NumberFormatException nfe) {
                        Context.getCurrentLogger().log(
                                Level.WARNING,
                                "Error during Age header parsing. Header: "
                                        + header.getValue(), nfe);
                    }
                } else if (HEADER_DATE.equalsIgnoreCase(header.getName())) {
                    Date date = DateUtils.parse(header.getValue());

                    if (date == null) {
                        date = new Date();
                    }

                    response.setDate(date);
                } else if (HEADER_RETRY_AFTER.equalsIgnoreCase(header.getName())) {
                } else if (HEADER_SET_COOKIE.equalsIgnoreCase(header.getName())
                        || HEADER_SET_COOKIE2.equalsIgnoreCase(header.getName())) {
                    try {
                        CookieSettingReader cr = new CookieSettingReader(header.getValue());
                        response.getCookieSettings().add(cr.readValue());
                    } catch (Exception e) {
                        Context.getCurrentLogger().log(
                                Level.WARNING,
                                "Error during cookie setting parsing. Header: "
                                        + header.getValue(), e);
                    }
                } else if (HEADER_WWW_AUTHENTICATE.equalsIgnoreCase(header.getName())) {
                } else if (HEADER_PROXY_AUTHENTICATE.equalsIgnoreCase(header.getName())) {
                } else if (HEADER_AUTHENTICATION_INFO.equalsIgnoreCase(header.getName())) {
                } else if (HEADER_SERVER.equalsIgnoreCase(header.getName())) {
                    response.getServerInfo().setAgent(header.getValue());
                } else if (HEADER_ALLOW.equalsIgnoreCase(header.getName())) {
                    MethodReader.addValues(header, response.getAllowedMethods());
                } else if (HEADER_VARY.equalsIgnoreCase(header.getName())) {
                    DimensionReader.addValues(header, response.getDimensions());
                } else if (HEADER_VIA.equalsIgnoreCase(header.getName())) {
                    RecipientInfoReader.addValues(header, response.getRecipientsInfo());
                } else if (HEADER_WARNING.equalsIgnoreCase(header.getName())) {
                    WarningReader.addValues(header, response.getWarnings());
                } else if (HEADER_CACHE_CONTROL.equalsIgnoreCase(header.getName())) {
                    CacheDirectiveReader.addValues(header, response.getCacheDirectives());
                } else if (HEADER_ACCEPT_RANGES.equalsIgnoreCase(header.getName())) {
                    TokenReader tr = new TokenReader(header.getValue());
                    response.getServerInfo().setAcceptingRanges(tr.readValues().contains(RANGE_BYTES_UNIT));
                } else if (HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS.equalsIgnoreCase(header.getName())) {
                    response.setAccessControlAllowCredentials(parseBoolean(header.getValue()));
                    StringReader.addValues(header, response.getAccessControlAllowHeaders());
                } else if (HEADER_ACCESS_CONTROL_ALLOW_ORIGIN.equalsIgnoreCase(header.getName())) {
                    response.setAccessControlAllowOrigin(header.getValue());
                } else if (HEADER_ACCESS_CONTROL_ALLOW_METHODS.equalsIgnoreCase(header.getName())) {
                    MethodReader.addValues(header, response.getAccessControlAllowMethods());
                } else if (HEADER_ACCESS_CONTROL_MAX_AGE.equalsIgnoreCase(header.getName())) {
                    response.setAccessControlMaxAge(Integer.parseInt(header.getValue()));
                }
            }
        }
    }

    /**
     * Extracts entity headers and updates a given representation or create an
     * empty one when at least one entity header is present.
     * 
     * @param headers
     *            The headers to copy.
     * @param representation
     *            The representation to update or null.
     * @return a representation updated with the given entity headers.
     * @throws NumberFormatException
     * @see HeaderUtils#copyResponseTransportHeaders(Series, Response)
     */
    public static Representation extractEntityHeaders(Iterable<Header> headers,
            Representation representation) throws NumberFormatException {
        Representation result = (representation == null) ? new EmptyRepresentation() : representation;
        boolean entityHeaderFound = false;

        if (headers != null) {
            for (Header header : headers) {
                if (HEADER_CONTENT_TYPE.equalsIgnoreCase(header.getName())) {
                    ContentType contentType = new ContentType(header.getValue());
                    result.setMediaType(contentType.getMediaType());

                    if ((result.getCharacterSet() == null)
                            || (contentType.getCharacterSet() != null)) {
                        result.setCharacterSet(contentType.getCharacterSet());
                    }

                    entityHeaderFound = true;
                } else if (HEADER_CONTENT_LENGTH.equalsIgnoreCase(header.getName())) {
                    entityHeaderFound = true;
                } else if (HEADER_EXPIRES.equalsIgnoreCase(header.getName())) {
                    result.setExpirationDate(HeaderReader.readDate(header.getValue(), false));
                    entityHeaderFound = true;
                } else if (HEADER_CONTENT_ENCODING.equalsIgnoreCase(header.getName())) {
                    new EncodingReader(header.getValue()).addValues(result.getEncodings());
                    entityHeaderFound = true;
                } else if (HEADER_CONTENT_LANGUAGE.equalsIgnoreCase(header.getName())) {
                    new LanguageReader(header.getValue()).addValues(result.getLanguages());
                    entityHeaderFound = true;
                } else if (HEADER_LAST_MODIFIED.equalsIgnoreCase(header.getName())) {
                    result.setModificationDate(HeaderReader.readDate(header.getValue(), false));
                    entityHeaderFound = true;
                } else if (HEADER_ETAG.equalsIgnoreCase(header.getName())) {
                    result.setTag(Tag.parse(header.getValue()));
                    entityHeaderFound = true;
                } else if (HEADER_CONTENT_LOCATION.equalsIgnoreCase(header.getName())) {
                    result.setLocationRef(header.getValue());
                    entityHeaderFound = true;
                } else if (HEADER_CONTENT_DISPOSITION.equalsIgnoreCase(header.getName())) {
                    try {
                        result.setDisposition(new DispositionReader(header.getValue()).readValue());
                        entityHeaderFound = true;
                    } catch (IOException ioe) {
                        Context.getCurrentLogger().log(
                                Level.WARNING,
                                "Error during Content-Disposition header parsing. Header: "
                                        + header.getValue(), ioe);
                    }
                } else if (HEADER_CONTENT_RANGE.equalsIgnoreCase(header.getName())) {
                } else if (HEADER_CONTENT_MD5.equalsIgnoreCase(header.getName())) {
                }
            }
        }

        // If no representation was initially expected and no entity header
        // is found, then do not return any representation
        if ((representation == null) && !entityHeaderFound) {
            result = null;
        }

        return result;
    }

    /**
     * Returns the content length of the request entity if know, {@link Representation#UNKNOWN_SIZE} otherwise.
     * 
     * @return The request content length.
     */
    public static long getContentLength(Series<Header> headers) {
        long contentLength = UNKNOWN_SIZE;

        if (headers != null) {
            // Extract the content length header
            for (Header header : headers) {
                if (HEADER_CONTENT_LENGTH.equalsIgnoreCase(header.getName())) {
                    try {
                        contentLength = Long.parseLong(header.getValue());
                    } catch (NumberFormatException e) {
                        contentLength = UNKNOWN_SIZE;
                    }
                }
            }
        }

        return contentLength;
    }

    /**
     * Indicates if the given character is alphabetical (a-z or A-Z).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is alphabetical (a-z or A-Z).
     */
    public static boolean isAlpha(int character) {
        return isUpperCase(character) || isLowerCase(character);
    }

    /**
     * Indicates if the given character is in ASCII range.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is in ASCII range.
     */
    public static boolean isAsciiChar(int character) {
        return (character >= 0) && (character <= 127);
    }

    /**
     * Indicates if the given character is a carriage return.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a carriage return.
     */
    public static boolean isCarriageReturn(int character) {
        return (character == 13);
    }

    /**
     * Indicates if the entity is chunked.
     * 
     * @return True if the entity is chunked.
     */
    public static boolean isChunkedEncoding(Series<Header> headers) {
        boolean result = false;

        if (headers != null) {
            final String header = headers.getFirstValue(
                    HeaderConstants.HEADER_TRANSFER_ENCODING, true);
            result = "chunked".equalsIgnoreCase(header);
        }

        return result;
    }

    /**
     * Indicates if the given character is a comma, the character used as header
     * value separator.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a comma.
     */
    public static boolean isComma(int character) {
        return (character == ',');
    }

    /**
     * Indicates if the given character is a comment text. It means {@link #isText(int)} returns true and the character
     * is not '(' or ')'.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a quoted text.
     */
    public static boolean isCommentText(int character) {
        return isText(character) && (character != '(') && (character != ')');
    }

    /**
     * Indicates if the connection must be closed.
     * 
     * @param headers
     *            The headers to test.
     * @return True if the connection must be closed.
     */
    public static boolean isConnectionClose(Series<Header> headers) {
        boolean result = false;

        if (headers != null) {
            String header = headers.getFirstValue(
                    HeaderConstants.HEADER_CONNECTION, true);
            result = "close".equalsIgnoreCase(header);
        }

        return result;
    }

    /**
     * Indicates if the given character is a control character.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a control character.
     */
    public static boolean isControlChar(int character) {
        return ((character >= 0) && (character <= 31)) || (character == 127);
    }

    /**
     * Indicates if the given character is a digit (0-9).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a digit (0-9).
     */
    public static boolean isDigit(int character) {
        return (character >= '0') && (character <= '9');
    }

    /**
     * Indicates if the given character is a double quote.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a double quote.
     */
    public static boolean isDoubleQuote(int character) {
        return (character == 34);
    }

    /**
     * Indicates if the given character is an horizontal tab.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is an horizontal tab.
     */
    public static boolean isHorizontalTab(int character) {
        return (character == 9);
    }

    /**
     * Indicates if the given character is in ISO Latin 1 (8859-1) range. Note
     * that this range is a superset of ASCII and a subrange of Unicode (UTF-8).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is in ISO Latin 1 range.
     */
    public static boolean isLatin1Char(int character) {
        return (character >= 0) && (character <= 255);
    }

    /**
     * Indicates if the given character is a value separator.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a value separator.
     */
    public static boolean isLinearWhiteSpace(int character) {
        return (isCarriageReturn(character) || isSpace(character)
                || isLineFeed(character) || HeaderUtils
                    .isHorizontalTab(character));
    }

    /**
     * Indicates if the given character is a line feed.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a line feed.
     */
    public static boolean isLineFeed(int character) {
        return (character == 10);
    }

    /**
     * Indicates if the given character is lower case (a-z).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is lower case (a-z).
     */
    public static boolean isLowerCase(int character) {
        return (character >= 'a') && (character <= 'z');
    }

    /**
     * Indicates if the given character marks the start of a quoted pair.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character marks the start of a quoted pair.
     */
    public static boolean isQuoteCharacter(int character) {
        return (character == '\\');
    }

    /**
     * Indicates if the given character is a quoted text. It means {@link #isText(int)} returns true and
     * {@link #isDoubleQuote(int)} returns
     * false.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a quoted text.
     */
    public static boolean isQuotedText(int character) {
        return isText(character) && !isDoubleQuote(character);
    }

    /**
     * Indicates if the given character is a semicolon, the character used as
     * header parameter separator.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a semicolon.
     */
    public static boolean isSemiColon(int character) {
        return (character == ';');
    }

    /**
     * Indicates if the given character is a separator.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a separator.
     */
    public static boolean isSeparator(int character) {
        switch (character) {
        case '(':
        case ')':
        case '<':
        case '>':
        case '@':
        case ',':
        case ';':
        case ':':
        case '\\':
        case '"':
        case '/':
        case '[':
        case ']':
        case '?':
        case '=':
        case '{':
        case '}':
        case ' ':
        case '\t':
            return true;

        default:
            return false;
        }
    }

    /**
     * Indicates if the given character is a space.
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a space.
     */
    public static boolean isSpace(int character) {
        return (character == 32);
    }

    /**
     * Indicates if the given character is textual (ISO Latin 1 and not a
     * control character).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is textual.
     */
    public static boolean isText(int character) {
        return isLatin1Char(character) && !isControlChar(character);
    }

    /**
     * Indicates if the token is valid.<br>
     * Only contains valid token characters.
     * 
     * @param token
     *            The token to check
     * @return True if the token is valid.
     */
    public static boolean isToken(CharSequence token) {
        for (int i = 0; i < token.length(); i++) {
            if (!isTokenChar(token.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Indicates if the given character is a token character (text and not a
     * separator).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is a token character (text and not a
     *         separator).
     */
    public static boolean isTokenChar(int character) {
        return isAsciiChar(character) && !isSeparator(character);
    }

    /**
     * Indicates if the given character is upper case (A-Z).
     * 
     * @param character
     *            The character to test.
     * @return True if the given character is upper case (A-Z).
     */
    public static boolean isUpperCase(int character) {
        return (character >= 'A') && (character <= 'Z');
    }



    /**
     * Private constructor to ensure that the class acts as a true utility class
     * i.e. it isn't instantiable and extensible.
     */
    private HeaderUtils() {
    }
}
