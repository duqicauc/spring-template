// Copyright Marco Massenzio (c) 2015.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.net.URI;

/**
 * General REST configuration goes here.
 *
 * <p>We override the default repository REST endpoint configuration, by adding a versioned
 * {@link #BASE_URI} to all endpoints.
 *
 * @author marco
 */
@Configuration
public class RestConfiguration extends RepositoryRestMvcConfiguration {

    public static final String BASE_URI = "/api/v1";

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);
        config.setBaseUri(URI.create(BASE_URI));
    }
}
