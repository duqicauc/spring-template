====================
Spring Boot Template
====================

.. image:: https://travis-ci.org/massenz/spring-template.svg?branch=develop
    :target: https://travis-ci.org/massenz/spring-template

.. image:: https://coveralls.io/repos/massenz/spring-template/badge.png?branch=develop
    :target: https://coveralls.io/r/massenz/spring-template?branch=develop

:Author: Marco Massenzio (marco@alertavert.com)
:Revision: 1.0-SNAPSHOT
:Created: 2014-12-21
:Updated: 2014-12-23

Overview
========

This is a skeleton project to get going quickly with the Spring Boot framework,
using Spring Data for the backend and Spring Rest for the frontend.

Just clone and modify.

Data Structure
==============

The Persistence Layer is based on MongoDB, using DAOs to separate data
concerns from business logic concerns.

Data access is via `Spring Repositories`_


Application Framework
=====================

This project uses Spring as the main application framework, `Spring Boot`_
to manage the application,
and `Spring Data`_ to manage the repositories.

Dependencies are managed via Maven (see the POM_)

All endpoints are exposed via `Spring REST`_ and take the form::

    [METHOD] /issues/{id}

where ``METHOD`` is one of the usual ``{GET, POST, PUT, DELETE}`` set and the ``{id}``
uniquely identifies the entity in the DB.

HATEOAS
-------

`Spring REST`_ implements the HATEOAS principles, and enables discovery of endpoints and
resources; the starting point is the ``root`` API [#]_::

    http://localhost:8080/api/v1

which returns something similar to::

    {
        "_links": {
           "issues": {
                "href": "http://localhost:8080/api/v1/issue{?page,size,sort}",
                "templated": true
            },
            "profile": {
                "href": "http://localhost:8080/api/v1/alps"
            }
        }
    }

and from which a client application can progress to "discover" services and entities [#]_.

Search Queries
--------------

`Spring REST`_ also supports out-of-the-box search querying capabilities, linked to the
methods exposed by the `Spring Repositories`_, via URI parameters mapping.

To discover the search capabilities of a given entity (again, following HATEOAS principles) one
uses the `search resource`_::

    http://localhost:8080/api/v1/issue/search

and queries against the entity can be executed by hitting an endpoint such as::

    http://localhost:8080/api/v1/issue/search/findByReporter?reporter=user-1

which is mapped from the repository method::

    @RepositoryRestResource(path = "issue")
    public interface IssueRepository extends PagingAndSortingRepository<Issue, String> {
        Issue findByReporter(@Param("reporter")String reporter);
    }

As this is a ``PagingAndSortingRepository``, the usual ``page`` etc. query params are allowed too.

Testing, Deploying & Running the Application
============================================

Testing and running locally is achieved via Maven::

    mvn clean test
    mvn clean cobertura:cobertura test
    mvn clean cobertura:check test

the second and third invocation will, respectively, generate the code coverage report
(in ``target/site/cobertura/index.html``) and check that test coverage exceeds the minimum threshold
set in ``pom.xml``::

        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>cobertura-maven-plugin</artifactId>
            <version>2.6</version>
            <configuration>
                ...
                <check>
                    <totalLineRate>70</totalLineRate>
                </check>
            </configuration>
        </plugin>

see the `cobertura plugin`_ documentation for more info.

`Spring Boot`_ generates the application as an embedded Tomcat JAR application, so this can
be run either via ``mvn`` or directly via ``java``::

    mvn spring-boot:run
    java -jar target/spring-template-1.0-SNAPSHOT.jar -Dsecurity.user.password=azekre7

see `.travis.yml`_ for an example of how to deploy and execute on a "blank" Linux (Debian) server.

Using AWS Beanstalk
-------------------

TODO: experiment & document here


Copyright and License
=====================

This software is (c) 2014 Marco Massenzio and is licensed
according to the `Apache 2 License`_ ; see also the LICENSE_ file in this folder.

.. [#] The *root* endpoint has been customized via the RestConfiguration_ class from the default
``/``
.. [#] See the section about `Resource Discoverability`_ for more information.

.. _Spring Boot: http://spring.io/spring-boot
.. _Spring Data: http://spring.io/spring-data
.. _Spring REST: http://docs.spring.io/spring-data/rest/docs/2.3.0.M1/reference/html/
.. _Spring Repositories: http://docs.spring.io/spring-data/data-mongodb/docs/current/reference/html/#repositories
.. _POM: pom.xml
.. _LICENSE: LICENSE
.. _Apache 2 License: http://www.apache.org/licenses/LICENSE-2.0
.. _Resource Discoverability: http://docs.spring.io/spring-data/rest/docs/2.3.0.M1/reference/html/#repository-resources.resource-discoverability
.. _RestConfiguration: https://github.com/massenz/spring-template/blob/develop/src/main/java/com/alertavert/template/RestConfiguration.java
.. _search resource: http://docs.spring.io/spring-data/rest/docs/2.3.0.M1/reference/html/#repository-resources.search-resource
.. _cobertura plugin: http://mojo.codehaus.org/cobertura-maven-plugin/usage.html
.. _.travis.yml: https://github.com/massenz/spring-template/blob/develop/.travis.yml
