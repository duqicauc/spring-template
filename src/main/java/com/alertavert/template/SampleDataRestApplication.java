// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template;

import com.alertavert.template.resources.IssuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>The main server Application and Controller class.</h3>
 *
 * <p>This class package will also act as the `root package` for Spring auto-configuration (see <a
 * href='http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#using-boot
 * -locating-the-main-class'>Spring
 * Boot - Locating the main Application class</a>).
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class SampleDataRestApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleDataRestApplication.class, args);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    String status() {
        return "{\"state\": \"running\"}";
    }
}
