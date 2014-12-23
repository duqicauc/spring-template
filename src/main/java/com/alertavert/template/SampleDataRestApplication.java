// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

package com.alertavert.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>The main server Application and Controller class.</h3>
 *
 * <p>This class package will also act as the `root package` for Spring auto-configuration
 * (see <a href='http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#using-boot-locating-the-main-class'>Spring Boot - Locating the main Application class</a>).
 *
 */
@RestController
@EnableAutoConfiguration
public class SampleDataRestApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleDataRestApplication.class, args);
    }

    @RequestMapping("/status")
    String status() {
        return "running";
    }
}
