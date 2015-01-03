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
resources; the starting point is the ``root`` API::

    http://localhost:8080/

which returns something similar to::

    {
        "_links": {
           "issues": {
                "href": "http://localhost:8080/issues{?page,size,sort}",
                "templated": true
            },
            "profile": {
                "href": "http://localhost:8080/alps"
            }
        }
    }

and from which a client application can progress to "discover" services and entities[#]_.

Copyright and License
=====================

This software is (c) 2014 Marco Massenzio and is licensed
according to the `Apache 2 License`_ ; see also the LICENSE_ file in this folder.

.. [#] See the section about `Resource Discoverability`_ for more information.

.. _Spring Boot: http://spring.io/spring-boot
.. _Spring Data: http://spring.io/spring-data
.. _Spring REST: http://docs.spring.io/spring-data/rest/docs/2.3.0.M1/reference/html/
.. _Spring Repositories: http://docs.spring.io/spring-data/data-mongodb/docs/current/reference/html/#repositories
.. _POM: pom.xml
.. _LICENSE: LICENSE
.. _Apache 2 License: http://www.apache.org/licenses/LICENSE-2.0
.. _Resource Discoverability: http://docs.spring.io/spring-data/rest/docs/2.3.0.M1/reference/html/#repository-resources.resource-discoverability
