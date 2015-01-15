/*
 * // Copyright Marco Massenzio (c) 2015.
 * // This code is licensed according to the terms of the Apache 2 License.
 * // See http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alertavert.template;

import com.alertavert.template.model.Issue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.UnknownHostException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test to run the application and verify configuration settings.
 *
 * @author mmassenzio@elementum.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class,
                                           AppConfiguration.class})
@WebAppConfiguration
// Separate profile for web tests to avoid clashing databases
@ActiveProfiles("test")
public class SampleDataRestApplicationTests {

  @Value("${test.value}")
  String value;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  MongoOperations ops;

  private MockMvc mvc;

  @Before
  public void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
//    ops.dropCollection(Issue.class);
  }

  @Test
  public void testStatus() throws Exception {
    this.mvc.perform(get("/status")).andExpect(status().isOk())
        .andExpect(content().string(containsString("running")));
  }

  @Test
  public void canGetConfigValues() {
    String dbUrl = this.context.getEnvironment().getProperty("db.uri");
    assertThat(dbUrl, containsString("localhost"));
    assertThat(dbUrl, endsWith("server-test"));
  }

  @Test
  public void canAccessDb() throws UnknownHostException {
    Issue.Builder builder = Issue.builder("rep", "A test issue");
    Issue issue = builder.assignTo("Joe Dev").newBug();
    ops.insert(issue);
    Issue testBug = ops.findOne(new Query(where("assignee").is("Joe Dev")),
                                Issue.class);
    assertThat(testBug.getTitle(), equalTo("A test issue"));
  }

  @Test
  public void testConfigurationValues() {
    String dbUrl = this.context.getEnvironment().getProperty("db.uri");
    assertThat(dbUrl, equalTo("mongodb://localhost:27017/server-test"));
    assertThat(value, equalTo("this is a test"));
    assertThat(ops, is(notNullValue()));
  }
}
