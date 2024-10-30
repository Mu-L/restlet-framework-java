Changes log
===========

- 2.5 Milestone 2 (???/2024)
    - Enhancements
      - Changed the minimum requirements back to Java 8 like for Restlet Framework 2.4 to facilitate upgrade.
      - Transitioned project to native Maven including code base, build and delivery.
      - Artifacts are now published in Maven Central and Sonatype OSS repository for snapshots and only there.
      - Updated all libraries used as dependency for various extensions.
      - RestletFileUpload can now dynamically adapt to Java SE or Java EE environments.
    - Bugs fixed
      - Ensured that all 2.4.4 fixes have been ported forward to the 2.5 branch.
      - Non-serializable internal attributes (status, request, and message) marked as transient,
        this is to allow ResourceException to be serialized, passing over the message and stack-trace
        (which is typically all that is needed to show the error or print it in the log).
    - Misc
      - Replaced separate Android, GAE, Java EE and OSGi editions by a single 'Java' edition
        (tested to work in those various environments).
      - Kept a distinct 'GWT' edition using a forked code base due to many GWT specificities.
      - Removed incubator to rely instead on pull requests.
      - Renamed extension Inject into Guice as it isn't truly generic.
      - Updated the links in all Javadocs file for accuracy.
      - Deprecated GAE, GWT, OSGi and RDF extensions for removal in next major release.
      - Removed code deprecated in version 2.4, including native Restlet XML configuration mechasism.
        Please use Spring for an alternative approach if needed.

- 2.5 Milestone 1 (03/09/2020)
    - Bugs fixed
      - Allow parsing of double values like "2.0" or "4.0" being received in
        the Retry-After header. Issue #1355.
        Reported by Brett Cooper.
      - MemoryRealm.unmap method leads to ArrayOutOfBound exception. Issue #1358.
        Reported by j-perrin.
    - Misc
      - Removed deprecated extensions emf, javamail, jaxrs, jibx, lucene, nio, 
        oauth, openid, platform, raml, simple, wadl. Issue #1351.
      - Removed generation of Eclipse p2 artifacts from the build since the 
        Eclipse p2 site is no more maintained.
      - Refresh copyright headers. Issue #1351.
      - Upgraded to JDK 11. Issue #1351.
      - Upgraded FileUpload library to version 1.4. Issue #1353.
      - Upgraded GSON library to version 2.8.6. Issue #1353.
      - Upgraded Guice library to version 4.2.2. Issue #1353.
      - Upgraded JaxB library to version 2.4.0-b180830.0438. Issue #1353.
      - Upgraded Spring Framework library to version 5.2.2-RELEASE. Issue #1353.
      - Upgraded Thymeleaf library to version 3.0.11. Issue #1353.
      - Upgraded Velocity library to version 2.1. Issue #1353.
      - Upgraded Guava library to version 28.1-jre. Issue #1353.
      - Upgraded Jackson library to version 2.10.1. Issue #1353.
      - Upgraded Snakeyaml library to version 1.24. Issue #1353.
      - Upgraded Jetty library to version 9.4.24.v20191120. Issue #1353.
      - Upgraded GWT library to version 2.8.2. Issue #1363.
