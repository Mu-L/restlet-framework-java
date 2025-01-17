/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.ext.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.ext.spring.SpringBeanFinder;
import org.restlet.ext.spring.SpringBeanRouter;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.routing.Route;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.test.RestletTestCase;
import org.restlet.util.Resolver;
import org.restlet.util.RouteList;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Rhett Sutphin
 */
public class SpringBeanRouterTestCase extends RestletTestCase {

    private static class TestAuthenticator extends ChallengeAuthenticator {
        private TestAuthenticator() throws IllegalArgumentException {
            super(null, ChallengeScheme.HTTP_BASIC, "Test");
        }
    }

    private static class TestFilter extends Filter {
    }

    private static class TestResource extends ServerResource {
    }

    private static class TestRestlet extends Restlet {
    }

    private static final String FISH_URI = "/renewable/fish/{fish_name}";

    private static final String ORE_URI = "/non-renewable/ore/{ore_type}";

    private DefaultListableBeanFactory factory;

    private SpringBeanRouter router;

    private RouteList actualRoutes() {
        doPostProcess();
        return this.router.getRoutes();
    }

    private void assertFinderForBean(String expectedBeanName, Restlet restlet) {
        assertInstanceOf(SpringBeanFinder.class, restlet, "Restlet is not a bean finder restlet: "
                + restlet.getClass().getName());
        final SpringBeanFinder actualFinder = (SpringBeanFinder) restlet;
        assertEquals(expectedBeanName, actualFinder.getBeanName(),
                "Finder does not point to correct bean");
        assertEquals(this.factory, actualFinder.getBeanFactory(), "Finder does not point to correct bean factory");
    }

    private void doPostProcess() {
        this.router.postProcessBeanFactory(this.factory);
    }

    private TemplateRoute matchRouteFor(String uri) {
        Request req = new Request(Method.GET,
                new Template(uri).format(new Resolver<String>() {
                    @Override
                    public String resolve(String name) {
                        return name;
                    }
                }));
        return (TemplateRoute) router.getNext(req, new Response(req));
    }

    private void registerBeanDefinition(String id, String alias,
                                        Class<?> beanClass, String scope) {
        BeanDefinition bd = new RootBeanDefinition(beanClass);
        bd.setScope(scope == null ? BeanDefinition.SCOPE_SINGLETON : scope);
        this.factory.registerBeanDefinition(id, bd);

        if (alias != null) {
            this.factory.registerAlias(id, alias);
        }
    }

    private void registerServerResourceBeanDefinition(String id, String alias) {
        registerBeanDefinition(id, alias, ServerResource.class,
                BeanDefinition.SCOPE_PROTOTYPE);
    }

    private Set<String> routeUris(RouteList routes) {
        Set<String> uris = new HashSet<>();

        for (Route actualRoute : routes) {
            if (actualRoute instanceof TemplateRoute) {
                uris.add(((TemplateRoute) actualRoute).getTemplate()
                        .getPattern());
            }
        }

        return uris;
    }

    @BeforeEach
    protected void setUpEach() throws Exception {
        this.factory = new DefaultListableBeanFactory();
        registerServerResourceBeanDefinition("ore", ORE_URI);
        registerServerResourceBeanDefinition("fish", FISH_URI);
        registerBeanDefinition("someOtherBean", null, String.class, null);
        this.router = new SpringBeanRouter();
    }

    @AfterEach
    protected void tearDownEach() throws Exception {
        this.factory = null;
        this.router = null;
    }

    @Test
    public void testExplicitAttachmentsMayBeRestlets() {
        String expected = "/protected/timber";
        this.router
                .setAttachments(Collections.singletonMap(expected, "timber"));
        registerBeanDefinition("timber", null, TestAuthenticator.class, null);

        doPostProcess();
        TemplateRoute timberRoute = matchRouteFor(expected);
        assertNotNull(timberRoute, "No route for " + expected);
        assertInstanceOf(TestAuthenticator.class, timberRoute.getNext(), "Route is not for correct restlet");
    }

    @Test
    public void testExplicitAttachmentsTrumpBeanNames() {
        this.router.setAttachments(Collections.singletonMap(ORE_URI, "fish"));
        RouteList actualRoutes = actualRoutes();
        assertEquals(2, actualRoutes.size(), "Wrong number of routes");

        TemplateRoute oreRoute = matchRouteFor(ORE_URI);
        assertNotNull(oreRoute, "No route for " + ORE_URI);
        assertFinderForBean("fish", oreRoute.getNext());
    }

    @Test
    public void testExplicitRoutingForNonResourceNonRestletBeansFails() {
        this.router.setAttachments(Collections.singletonMap("/fail",
                "someOtherBean"));
        try {
            doPostProcess();
            fail("Exception not thrown");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "someOtherBean is not routable.  It must be either a Resource, a ServerResource or a Restlet.",
                    ise.getMessage());
        }
    }

    @Test
    public void testRoutesCreatedForBeanIdsIfAppropriate() {
        String grain = "/renewable/grain/{grain_type}";
        registerServerResourceBeanDefinition(grain, null);

        final Set<String> actualUris = routeUris(actualRoutes());
        assertEquals(3, actualUris.size(), "Wrong number of URIs");
        assertTrue(actualUris.contains(grain), "Missing grain URI: " + actualUris);
    }

    @Test
    public void testRoutesCreatedForUrlAliases() {
        final Set<String> actualUris = routeUris(actualRoutes());
        assertEquals(2, actualUris.size(), "Wrong number of URIs");
        assertTrue(actualUris.contains(ORE_URI), "Missing ore URI: " + actualUris);
        assertTrue(actualUris.contains(FISH_URI), "Missing fish URI: " + actualUris);
    }

    @Test
    public void testRoutesPointToFindersForBeans() {
        final RouteList actualRoutes = actualRoutes();
        assertEquals(2, actualRoutes.size(), "Wrong number of routes");
        TemplateRoute oreRoute = matchRouteFor(ORE_URI);
        TemplateRoute fishRoute = matchRouteFor(FISH_URI);
        assertNotNull(oreRoute, "ore route not present: " + actualRoutes);
        assertNotNull(fishRoute, "fish route not present: " + actualRoutes);

        assertFinderForBean("ore", oreRoute.getNext());
        assertFinderForBean("fish", fishRoute.getNext());
    }

    @Test
    public void testRoutingIncludesAuthenticators() {
        String expected = "/protected/timber";
        registerBeanDefinition("timber", expected, TestAuthenticator.class,
                null);
        doPostProcess();

        TemplateRoute authenticatorRoute = matchRouteFor(expected);
        assertNotNull(authenticatorRoute, "No route for authenticator");
        assertInstanceOf(TestAuthenticator.class, authenticatorRoute.getNext(), "Route is not for authenticator");
    }

    @Test
    public void testRoutingIncludesFilters() {
        String expected = "/filtered/timber";
        registerBeanDefinition("timber", expected, TestFilter.class, null);
        doPostProcess();

        TemplateRoute filterRoute = matchRouteFor(expected);
        assertNotNull(filterRoute, "No route for filter");
        assertInstanceOf(Filter.class, filterRoute.getNext(), "Route is not for filter");
    }

    @Test
    public void testRoutingIncludesOtherRestlets() {
        String expected = "/singleton";
        registerBeanDefinition("timber", expected, TestRestlet.class, null);
        doPostProcess();

        TemplateRoute restletRoute = matchRouteFor(expected);
        assertNotNull(restletRoute, "No route for restlet");
        assertInstanceOf(TestRestlet.class, restletRoute.getNext(), "Route is not for restlet");
    }

    @Test
    public void testRoutingIncludesResourceSubclasses() {
        String expected = "/renewable/timber/{id}";
        registerBeanDefinition("timber", expected, TestResource.class,
                BeanDefinition.SCOPE_PROTOTYPE);

        doPostProcess();
        TemplateRoute timberRoute = matchRouteFor("/renewable/timber/sycamore");
        assertNotNull(timberRoute, "No route for timber");
        assertFinderForBean("timber", timberRoute.getNext());
    }

    @Test
    public void testRoutingIncludesSpringRouterStyleExplicitlyMappedBeans() {
        final BeanDefinition bd = new RootBeanDefinition(ServerResource.class);
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        this.factory.registerBeanDefinition("timber", bd);
        this.factory.registerAlias("timber", "no-slash");

        String expectedTemplate = "/renewable/timber/{farm_type}";
        router.setAttachments(Collections.singletonMap(expectedTemplate,
                "timber"));
        final RouteList actualRoutes = actualRoutes();

        assertEquals(3, actualRoutes.size(), "Wrong number of routes");
        TemplateRoute timberRoute = matchRouteFor(expectedTemplate);
        assertNotNull(timberRoute, "Missing timber route: " + actualRoutes);
        assertFinderForBean("timber", timberRoute.getNext());
    }

    @Test
    public void testRoutingSkipsResourcesWithoutAppropriateAliases() {
        final BeanDefinition bd = new RootBeanDefinition(ServerResource.class);
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        this.factory.registerBeanDefinition("timber", bd);
        this.factory.registerAlias("timber", "no-slash");

        final RouteList actualRoutes = actualRoutes();
        assertEquals(2, actualRoutes.size(), "Timber resource should have been skipped");
    }
}
