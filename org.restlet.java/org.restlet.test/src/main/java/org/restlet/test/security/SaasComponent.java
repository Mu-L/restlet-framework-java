/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.test.security;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.security.Group;
import org.restlet.security.MemoryRealm;
import org.restlet.security.User;
import org.restlet.test.RestletTestCase;

/**
 * Sample SAAS component with declared organizations.
 * 
 * @author Jerome Louvel
 */
public class SaasComponent extends Component {

    public SaasComponent() {
        Context context = getContext().createChildContext();
        SaasApplication app = new SaasApplication(context);

        MemoryRealm realm = new MemoryRealm();
        context.setDefaultEnroler(realm.getEnroler());
        context.setDefaultVerifier(realm.getVerifier());

        // Add users
        User stiger = new User("stiger", "pwd", "Scott", "Tiger",
                "scott.tiger@foobar.com");
        realm.getUsers().add(stiger);

        User larmstrong = new User("larmstrong", "pwd", "Louis", "Armstrong",
                "la@foobar.com");
        realm.getUsers().add(larmstrong);

        // Add groups
        Group employees = new Group("employees ", "All FooBar employees");
        employees.getMemberUsers().add(larmstrong);
        realm.getRootGroups().add(employees);

        Group contractors = new Group("contractors ", "All FooBar contractors");
        contractors.getMemberUsers().add(stiger);
        realm.getRootGroups().add(contractors);

        Group managers = new Group("managers", "All FooBar managers");
        realm.getRootGroups().add(managers);

        Group directors = new Group("directors ", "Top-level directors");
        directors.getMemberUsers().add(larmstrong);
        managers.getMemberGroups().add(directors);

        Group developers = new Group("developers", "All FooBar developers");
        realm.getRootGroups().add(developers);

        Group engineers = new Group("engineers", "All FooBar engineers");
        engineers.getMemberUsers().add(stiger);
        developers.getMemberGroups().add(engineers);

        // realm.map(customer1, app.getRole("user"));
        realm.map(managers, app.getRole("admin"));

        getDefaultHost().attach(app);
        getServers().add(Protocol.HTTP, RestletTestCase.TEST_PORT);
    }
}
