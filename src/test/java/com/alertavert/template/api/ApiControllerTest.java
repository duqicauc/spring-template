/*
 * // Copyright Marco Massenzio (c) 2015.
 * // This code is licensed according to the terms of the Apache 2 License.
 * // See http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alertavert.template.api;

import com.alertavert.template.ApiController;
import com.alertavert.template.AppConfiguration;
import com.alertavert.template.SampleDataRestApplication;
import com.alertavert.template.model.Issue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class, AppConfiguration.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class ApiControllerTest {

    public static final String ISSUES_API = "/api/v1/issue";

    // TODO: is there a simpler way of extracting the mapped Issues from the HAL format?
    public static class HalIssues {
        @JsonProperty("_links")
        Map<String, ?> links;

        @JsonProperty("_embedded")
        Map<String, List<Issue>> embedded;

        @JsonProperty("page")
        Map<String, ?> page;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

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
        Issue newIssue = Issue.builder("reporter", "title").newBug();
        MockHttpServletRequestBuilder builder = post(ISSUES_API).content(
                mapper.writeValueAsString(newIssue));
        MvcResult result = mvc.perform(builder).andReturn();
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);
        String id = location.substring(location.lastIndexOf('/') + 1);
        assertTrue(ObjectId.isValid(id));
        // clean up
        mvc.perform(delete(String.format("%s/%s", ISSUES_API, "{id}"), id));
    }

    @Test
    public void testGetAllIssues() throws Exception {
        createIssues(5);
        MvcResult result = mvc.perform(get(ISSUES_API)).andReturn();
        List<Issue> issues = extractEmbeddedIssues(result);
        assertThat(issues.size(), greaterThanOrEqualTo(5));
    }

    public void createIssues(int num) throws Exception {
        for (int i = 0; i < num; ++i) {
            Issue newIssue = Issue.builder("reporter", "title").newBug();
            MockHttpServletRequestBuilder builder = post(ISSUES_API).content(
                mapper.writeValueAsString(newIssue));
            mvc.perform(builder);
        }
    }

    public static List<Issue> extractEmbeddedIssues(MvcResult result) throws Exception {
        HalIssues body = mapper.readValue(result.getResponse().getContentAsString(),
                HalIssues.class);
        assertNotNull(body.embedded);
        assertTrue(body.embedded.containsKey("issues"));
        List<Issue> listIssues = body.embedded.get("issues");
        return listIssues;
    }
}
