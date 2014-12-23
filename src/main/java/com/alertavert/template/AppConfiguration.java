// Copyright (c) 2014 Elementum SCM, Inc.
// All rights reserved.  http://www.elementum.com

package com.alertavert.template;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

/**
 * Main Configuration class.
 *
 * <p>Use this class to store all application configuration values, bean and other configuration
 * utilities
 *
 * <p>Created by mmassenzio on 12/2/14.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.alertavert.template.persistence")
@ComponentScan
public class AppConfiguration extends AbstractMongoConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(AppConfiguration.class);
  public static final String MONGODB_SCHEME = "mongodb://";

  /**
   * The connecting URI string - if specified, this will supersede all the other {@literal db.*}
   * configuration values (which will be ignored). <p>The format is defined <a
   * href='http://docs.mongodb.org/manual/reference/connection-string'>here</a> and takes the
   * form:
   * <pre>
   *   mongodb://[username:password@]host1[:port1][,host2[:port2],...[,
   *   hostN[:portN]]][/[database][?options]]
   * </pre>
   */
  @Value("${db.uri:}")
  String dbUri;

  /**
   * The host(s) to connect to - this actually includes the port(s) to connect to too:
   * <pre>
   *   localhost
   *   localhost:27030
   *   10.10.0.1:27090,10.10.02:27080,192.168.1.99
   * </pre>
   * are all valid hosts strings and will connect to the given host/port or the replica set (in
   * the last example).
   *
   * <p>If no port is specified Mongo will use the 27017 default port. We assume {@literal
   * localhost} by default, if unspecified.
   */
  @Value("@{db.hosts:localhost}")
  String hosts;

  @Value("${db.database:}")
  String databaseName;

  @Value("${db.username:}")
  String username;

  @Value(value = "${db.password:}")
  String password;

  @Override
  protected String getDatabaseName() {
    return getClientURI().getDatabase();
  }

  @Bean
  @Primary
  /**
   * Creates the primary (and only) DB Client for a MongoDB instance.
   * Under the covers, creates a {@link com.mongodb.MongoClient} object (creatin a
   * {@link com.mongodb.Mongo} object is deprecated) and returns it, optionally adding user
   * credentials, is specified in the configuration file.
   *
   * <p>User credentials can be specified by either using the URI format (see
   * <a href='http://docs.mongodb.org/manual/reference/connection-string'>the MongoDB
   * documentation)</a>) or via the {@literal db.username} and {@literal db.password} configuration
   * arguments: the URI will take precedence in case both are defined (which is <strong>NOT</strong>
   * recommended).
   *
   * @see com.mongodb.MongoClient
   */
  public Mongo mongo() throws UnknownHostException {
    MongoClientURI clientURI = getClientURI();
    LOG.info("Connecting to: {}", clientURI);
    return new MongoClient(clientURI);
  }


  @Bean
  public MongoClientURI getClientURI() {
    if (dbUri.isEmpty()) {
      StringBuilder builder = new StringBuilder(MONGODB_SCHEME);
      if (!username.isEmpty()) {
        builder.append(username).append(':').append(password).append('@');
      }
      builder.append(hosts);
      if (!databaseName.isEmpty()) {
        builder.append('/').append(databaseName);
      }
      dbUri = builder.toString();
      LOG.debug("Building MongoDB URI string from configuration values: {}", dbUri);
    }
    LOG.info("MongoDB URI: {}", dbUri);
    return new MongoClientURI(dbUri);
  }

  @Bean
  @Override
  public MongoDbFactory mongoDbFactory() throws UnknownHostException {
    if (databaseName.isEmpty()) {
      databaseName = getClientURI().getDatabase();
    }
    if (username.isEmpty()) {
      LOG.debug("Creating a MongoDBFactory for non-authenticated user ({})", databaseName);
      return new SimpleMongoDbFactory(mongo(), databaseName);
    } else {
      UserCredentials userCredentials = new UserCredentials(username, password);
      LOG.debug("Creating a MongoDBFactory for user [{}]: {}", username, databaseName);
      return new SimpleMongoDbFactory(mongo(), databaseName, userCredentials);
    }
  }
}
