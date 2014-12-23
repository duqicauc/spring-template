================
transport-server
================

.. image:: https://travis-ci.org/massenz/spring-template.svg?branch=develop
    :target: https://travis-ci.org/massenz/spring-template

.. image:: https://coveralls.io/repos/massenz/spring-template/badge.png?branch=develop
    :target: https://coveralls.io/r/massenz/spring-template?branch=develop

:Author: Marco Massenzio (marco@alertavert.com)
:Revision: 1.0-SNAPSHOT
:Created: 2014-12-21
:Updated: 2014-12-21

Overview
========

This is a skeleton project to get going quickly with the Spring Boot framework,
using Spring Data for the backend and Spring Rest for the frontend.

Just clone and modify.

Data Structure
==============

The Persistence Layer is based on MongoDB, using DAOs to separate data
concerns from business logic concerns.

See the `Data Schema`_


Application Framework
=====================

This project uses Spring as the main application framework, `Spring Boot`_
to manage the application,
and `Spring Data`_ to manage the repositories.

Dependencies are managed via Maven (see the POM_)

.. _Data Schema: docs/data_schema.rst
.. _Spring Boot: http://spring.io/spring-boot
.. _Spring Data: http://spring.io/spring-data
.. _POM: pom.xml

