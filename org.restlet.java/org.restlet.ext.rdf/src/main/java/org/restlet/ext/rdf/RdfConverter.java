/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.rdf;

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
 * Converter between the Graph and RDF Representation classes.
 * 
 * @author Thierry Boileau
 * @deprecated Will be removed in next major release.
 */
@Deprecated
public class RdfConverter extends ConverterHelper {

    private static final VariantInfo VARIANT_RDF_N3 = new VariantInfo(
            MediaType.TEXT_RDF_N3);

    private static final VariantInfo VARIANT_RDF_NTRIPLES = new VariantInfo(
            MediaType.TEXT_RDF_NTRIPLES);

    private static final VariantInfo VARIANT_RDF_TURTLE = new VariantInfo(
            MediaType.TEXT_TURTLE);

    private static final VariantInfo VARIANT_RDF_XML = new VariantInfo(
            MediaType.APPLICATION_ALL_XML);

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (isCompatible(source)) {
            result = addObjectClass(result, Graph.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (source != null && Graph.class.isAssignableFrom(source)) {
            result = addVariant(result, VARIANT_RDF_N3);
            result = addVariant(result, VARIANT_RDF_NTRIPLES);
            result = addVariant(result, VARIANT_RDF_NTRIPLES);
            result = addVariant(result, VARIANT_RDF_XML);
        }

        return result;
    }

    /**
     * Indicates if the given variant is compatible with the media types
     * supported by this converter.
     * 
     * @param variant
     *            The variant.
     * @return True if the given variant is compatible with the media types
     *         supported by this converter.
     */
    protected boolean isCompatible(Variant variant) {
        return (variant != null)
                && (VARIANT_RDF_N3.isCompatible(variant)
                        || VARIANT_RDF_NTRIPLES.isCompatible(variant)
                        || VARIANT_RDF_TURTLE.isCompatible(variant) || VARIANT_RDF_XML
                            .isCompatible(variant));
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;

        if (source instanceof Graph) {
            if (target == null) {
                result = 0.5F;
            } else if (isCompatible(target)) {
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
        float result = -1.0f;

        if (target != null) {
            if (Graph.class.isAssignableFrom(target)) {
                result = 1.0f;
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Representation source, Class<T> target,
            Resource resource) throws IOException {
        RdfRepresentation rdfSource = null;
        if (source instanceof RdfRepresentation) {
            rdfSource = (RdfRepresentation) source;
        } else {
            rdfSource = new RdfRepresentation(source);
        }

        T result = null;
        if (target == null) {
            result = (T) rdfSource.getGraph();
        } else if (source instanceof RdfRepresentation) {
            result = target.cast(rdfSource.getGraph());
        }

        return result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target,
            Resource resource) throws IOException {
        if (source instanceof Graph) {
            return new RdfRepresentation((Graph) source, target.getMediaType());
        }

        return null;
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences,
            Class<T> entity) {
        if (Graph.class.isAssignableFrom(entity)) {
            updatePreferences(preferences, MediaType.TEXT_RDF_N3, 1.0F);
            updatePreferences(preferences, MediaType.TEXT_RDF_NTRIPLES, 1.0F);
            updatePreferences(preferences, MediaType.TEXT_TURTLE, 1.0F);
            updatePreferences(preferences, MediaType.APPLICATION_RDF_XML, 1.0F);
        }
    }

}
