/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.gwt;

import static org.restlet.data.MediaType.APPLICATION_JAVA_OBJECT_GWT;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Converter between Object instances and Representations based on GWT
 * serialization format.
 * 
 * @author Jerome Louvel
 */
public class GwtConverter extends ConverterHelper {

    /** GWT variant. */
    private static final VariantInfo VARIANT_GWT = new VariantInfo(APPLICATION_JAVA_OBJECT_GWT);

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_GWT.isCompatible(source)) {
            result = addObjectClass(result, Serializable.class);
            result = addObjectClass(result, ObjectRepresentation.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (Serializable.class.isAssignableFrom(source)
                || IsSerializable.class.isAssignableFrom(source)
                || ObjectRepresentation.class.isAssignableFrom(source)) {
            result = addVariant(result, VARIANT_GWT);
        }

        return result;
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {

        if (source instanceof Serializable || source instanceof IsSerializable) {
            if (target == null) {
                return 0.5F;
            }
            if (APPLICATION_JAVA_OBJECT_GWT.equals(target.getMediaType())) {
                return 1.0F;
            }
            if (APPLICATION_JAVA_OBJECT_GWT.isCompatible(target.getMediaType())) {
                return 0.6F;
            }
            return 0.5F;
        }

        return -1.0F;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        if (source instanceof ObjectRepresentation<?>) {
            return 1.0F;
        }

        if (target != null) {
            if (ObjectRepresentation.class.isAssignableFrom(target)) {
                return 1.0F;
            }
            if (Serializable.class.isAssignableFrom(target)
                    || IsSerializable.class.isAssignableFrom(target)) {
                if (APPLICATION_JAVA_OBJECT_GWT.equals(source.getMediaType())) {
                    return 1.0F;
                }
                if (APPLICATION_JAVA_OBJECT_GWT.isCompatible(source.getMediaType())) {
                    return 0.6F;
                }
            }
        }

        return -1.0F;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) throws IOException {
        ObjectRepresentation<?> gwtSource = (source instanceof ObjectRepresentation<?>) ?
                (ObjectRepresentation<?>) source :
                new ObjectRepresentation<T>(source.getText(), target);

        if (target == null) {
            return (T) gwtSource.getObject();
        }
        if (ObjectRepresentation.class.isAssignableFrom(target)) {
            return (T) gwtSource;
        }
        if (Serializable.class.isAssignableFrom(target)
                || IsSerializable.class.isAssignableFrom(target)) {
            return (T) gwtSource.getObject();
        }

        return null;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        if (source instanceof Serializable) {
            return new ObjectRepresentation<Serializable>((Serializable) source);
        }
        if (source instanceof IsSerializable) {
            return new ObjectRepresentation<IsSerializable>((IsSerializable) source);
        }
        if (source instanceof Representation) {
            return (Representation) source;
        }

        return null;
    }

    @Override
    public <T> void updatePreferences(List<Preference<MediaType>> preferences,
            Class<T> entity) {
        if (Serializable.class.isAssignableFrom(entity)
                || IsSerializable.class.isAssignableFrom(entity)
                || ObjectRepresentation.class.isAssignableFrom(entity)) {
            updatePreferences(preferences, APPLICATION_JAVA_OBJECT_GWT, 1.0F);
        }
    }

}
