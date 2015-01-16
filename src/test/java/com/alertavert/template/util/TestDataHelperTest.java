//  Copyright Marco Massenzio (c) 2015.
//  This code is licensed according to the terms of the Apache 2 License.
//  See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.util;

import com.alertavert.template.SampleDataRestApplication;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class})
@ActiveProfiles("test")
public class TestDataHelperTest {

  @Document(collection = "tests")
  public static class Simple {

    @Id
    @JsonProperty("id")
    Long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("value")
    String value;

    Simple() {}
  }

  public static final String DATA = "/data/helper_test_data.json";

  TestDataHelper<Simple> helperUnderTest;

  @Inject
  MongoOperations ops;

  @Before
  public void readData() {
    helperUnderTest = new TestDataHelper<>(DATA, ops, Simple.class);
    ops.dropCollection(Simple.class);
  }

  @Test
  public void canDeserializeSimpleData() throws IOException {
    String json = "{\"id\": 1, \"name\": \"test\", \"value\": \"it works\"}";
    ObjectMapper mapper = new ObjectMapper();
    Simple simpleData = mapper.readValue(json, Simple.class);
    assertTrue(simpleData.id == 1);
    assertEquals("test", simpleData.name);
    assertEquals("it works", simpleData.value);
  }

  @Test
  public void testGetTestData() throws Exception {
    List<Simple> myData = helperUnderTest.getTestData();
    assertEquals(2, myData.size());
    assertThat(myData.get(1).value, equalTo("did this and that"));
  }

  @Test
  public void testSaveData() throws Exception {
    helperUnderTest.saveData();
    Simple data = ops.findById(1, Simple.class);
    assertThat(data, notNullValue());
    assertThat(data.name, equalTo("test-1"));
  }
}
