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
import java.util.List;

import org.apache.velocity.Template;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

/**
 * Converter between the Velocity Template objects and Representations. The
 * adjoined data model is based on the request and response objects.
 * 
 * @author Thierry Boileau.
 */
public class VelocityConverter extends ConverterHelper {

    private static final VariantInfo VARIANT_ALL = new VariantInfo(MediaType.ALL);

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        return null;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (Template.class.isAssignableFrom(source)) {
            result = addVariant(result, VARIANT_ALL);
        }

        return result;
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        if (source instanceof Template) {
            return 1.0f;
        }

        return -1.0f;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        return -1.0f;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target,
            Resource resource) throws IOException {
        return null;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) throws IOException {

        if (source instanceof Template) {
            TemplateRepresentation tr = new TemplateRepresentation((Template) source, target.getMediaType());
            tr.setDataModel(resource.getRequest(), resource.getResponse());
            return tr;
        }

        return null;
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences, Class<T> entity) {
        if (Template.class.isAssignableFrom(entity)) {
            updatePreferences(preferences, MediaType.ALL, 1.0F);
        }
    }
}
