// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

package com.alertavert.template.api;

import com.alertavert.template.ApiController;
import com.alertavert.template.AppConfiguration;
import com.alertavert.template.SampleDataRestApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class,
        AppConfiguration.class})
@WebAppConfiguration
// Separate profile for web tests to avoid clashing databases
@ActiveProfiles("test")
public class ApiControllerTest {


    @Autowired
    private WebApplicationContext context;

    @Autowired
    ApiController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testCreateIssue() throws Exception {

    }

    @Test
    public void testGetAllIssues() throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/issues")).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
