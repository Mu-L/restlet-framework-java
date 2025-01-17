/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.html;

import java.io.IOException;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

/**
 * Converter between the HTML API and Representation classes.
 * 
 * @author Jerome Louvel
 * @deprecated Will be removed in next minor release in favor or Jetty extension.
 */
@Deprecated
public class HtmlConverter extends ConverterHelper {

    private static final VariantInfo VARIANT_MULTIPART = new VariantInfo(
            MediaType.MULTIPART_FORM_DATA);

    private static final VariantInfo VARIANT_WWW_FORM = new VariantInfo(
            MediaType.APPLICATION_WWW_FORM);

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_WWW_FORM.isCompatible(source)) {
            result = addObjectClass(result, FormDataSet.class);
        } else if (VARIANT_MULTIPART.isCompatible(source)) {
            result = addObjectClass(result, FormDataSet.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (FormDataSet.class.isAssignableFrom(source)) {
            result = addVariant(result, VARIANT_WWW_FORM);
            result = addVariant(result, VARIANT_MULTIPART);
        }

        return result;
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;

        if (source instanceof FormDataSet) {
            if (target == null) {
                result = 0.5F;
            } else if (MediaType.APPLICATION_WWW_FORM.isCompatible(target
                    .getMediaType())
                    || MediaType.MULTIPART_FORM_DATA.isCompatible(target
                            .getMediaType())) {
                result = 1.0F;
            } else {
                result = 0.5F;
            }
        }

        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target,
            Resource resource) {
        float result = -1.0F;

        if (target != null) {
            if (FormDataSet.class.isAssignableFrom(target)) {
                if (MediaType.APPLICATION_WWW_FORM.isCompatible(source
                        .getMediaType())
                        || MediaType.MULTIPART_FORM_DATA.isCompatible(source
                                .getMediaType())) {
                    result = 1.0F;
                } else {
                    result = 0.5F;
                }
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Representation source, Class<T> target,
            Resource resource) throws IOException {
        Object result = null;

        if (FormDataSet.class.isAssignableFrom(target)) {
            result = new FormDataSet(source);
        }

        return (T) result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target,
            Resource resource) {
        Representation result = null;

        if (source instanceof FormDataSet) {
            result = (FormDataSet) source;
        }

        return result;
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences,
            Class<T> entity) {
        if (FormDataSet.class.isAssignableFrom(entity)) {
            updatePreferences(preferences, MediaType.APPLICATION_WWW_FORM, 1.0F);
        }
    }

}
