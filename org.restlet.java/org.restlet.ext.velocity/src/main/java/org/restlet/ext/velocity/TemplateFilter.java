/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.velocity;

import java.io.IOException;
import java.util.Map;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Encoding;
import org.restlet.data.Status;
import org.restlet.routing.Filter;
import org.restlet.util.Resolver;

/**
 * Filter response's entity and wrap it with a Velocity's template
 * representation. By default, the template representation provides a data model
 * based on the request and response objects. In order for the wrapping to
 * happen, the representations must have the {@link Encoding#VELOCITY} encoding
 * set.<br>
 * <br>
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Thierry Boileau
 */
public class TemplateFilter extends Filter {

    /** The template's data model as a map. */
    private volatile Map<String, Object> mapDataModel;

    /** The template's data model as a resolver. */
    private volatile Resolver<Object> resolverDataModel;

    /**
     * Constructor.
     */
    public TemplateFilter() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     */
    public TemplateFilter(Context context) {
        super(context);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     * @param next
     *            The next Restlet.
     */
    public TemplateFilter(Context context, Restlet next) {
        super(context, next);
        this.mapDataModel = null;
        this.resolverDataModel = null;
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     * @param next
     *            The next Restlet.
     * @param dataModel
     *            The filter's data model.
     */
    public TemplateFilter(Context context, Restlet next,
            Map<String, Object> dataModel) {
        super(context, next);
        this.mapDataModel = dataModel;
        this.resolverDataModel = null;
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     * @param next
     *            The next Restlet.
     * @param dataModel
     *            The filter's data model.
     */
    public TemplateFilter(Context context, Restlet next,
            Resolver<Object> dataModel) {
        super(context, next);
        this.mapDataModel = null;
        this.resolverDataModel = dataModel;
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        if (response.isEntityAvailable()
                && response.getEntity().getEncodings()
                        .contains(Encoding.VELOCITY)) {
            try {
                final TemplateRepresentation representation = new TemplateRepresentation(
                        response.getEntity(), response.getEntity()
                                .getMediaType());

                if ((this.mapDataModel == null)
                        && (this.resolverDataModel == null)) {
                    representation.setDataModel(request, response);
                } else {
                    if (this.mapDataModel == null) {
                        representation.setDataModel(this.resolverDataModel);
                    } else {
                        representation.setDataModel(this.mapDataModel);
                    }
                }

                response.setEntity(representation);
            } catch (ResourceNotFoundException e) {
                response.setStatus(Status.CLIENT_ERROR_NOT_FOUND, e);
            } catch (ParseErrorException e) {
                response.setStatus(Status.SERVER_ERROR_INTERNAL, e);
            } catch (IOException e) {
                response.setStatus(Status.SERVER_ERROR_INTERNAL, e);
            }
        }
    }
}
