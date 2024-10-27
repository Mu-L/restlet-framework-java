/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.restlet.data.Method;
import org.restlet.engine.resource.AnnotationInfo;
import org.restlet.engine.resource.AnnotationUtils;
import org.restlet.engine.resource.MethodAnnotationInfo;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.test.RestletTestCase;

/**
 * Test case for generic interfaces.
 *
 * @author Valdis Rigdon
 */
public class AnnotationUtilsTestCase extends RestletTestCase {

    public interface IChild extends IParent<String> {

    }

    public interface IParent<T> {

        @Get
        T getType();

        @Put
        void update(T generic);

    }

    @Test
    public void testGetAnnotationsWithGenericParameterType() {
        List<AnnotationInfo> infos = AnnotationUtils.getInstance()
                .getAnnotations(IChild.class);
        assertEquals(4, infos.size(), "Wrong count: " + infos);
        boolean found = false;

        for (AnnotationInfo ai : infos) {
            if (ai instanceof MethodAnnotationInfo) {
                MethodAnnotationInfo mai = (MethodAnnotationInfo) ai;

                if (mai.getJavaClass().equals(IChild.class)
                        && mai.getRestletMethod().equals(Method.PUT)) {
                    found = true;
                    assertEquals(String.class, mai.getJavaInputTypes()[0]);
                }
            }
        }

        assertTrue(found, "Didn't find a method with IChild as the declaring class.");
    }

    @Test
    public void testGetAnnotationsWithGenericReturnType() {
        List<AnnotationInfo> infos = AnnotationUtils.getInstance()
                .getAnnotations(IChild.class);
        assertEquals(4, infos.size(), "Wrong count: " + infos);
        boolean found = false;

        for (AnnotationInfo ai : infos) {
            if (ai instanceof MethodAnnotationInfo) {
                MethodAnnotationInfo mai = (MethodAnnotationInfo) ai;

                if (mai.getJavaClass().equals(IChild.class)
                        && mai.getRestletMethod().equals(Method.GET)) {
                    found = true;
                    assertEquals(String.class, mai.getJavaOutputType());
                }
            }
        }

        assertTrue(found, "Didn't find a method with IChild as the declaring class.");
    }
}
