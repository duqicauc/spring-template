//  Copyright Marco Massenzio (c) 2015.
//  This code is licensed according to the terms of the Apache 2 License.
//  See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.util;

import com.google.common.collect.Sets;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MultiTestDataHelperTest extends AbstractTestBase<MultiTestDataHelperTest.Dog> {

  @Override
  protected Repository<Dog, ? extends Serializable> getRepository() {
    return null;
  }

  @Override
  protected String getDefaultResource() {
    return null;
  }

  @Document(collection = "dogs")
  public static class Dog {

    @Id
    @JsonProperty("id")
    Long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("species")
    String species;

    @JsonProperty("apple_id")
    Long appleId;

    Dog() {}
  }

  @Document(collection = "apples")
  public static class Apple {

    @Id
    @JsonProperty("id")
    Long id;

    @JsonProperty("variety")
    String variety;

    @JsonProperty("origin")
    String origin;

    Apple() {}
  }

  @Inject
  MongoOperations operations;

  MultiTestDataHelper instance;

  @Before
  public void init() {
    instance  = new MultiTestDataHelper(operations);
    operations.dropCollection(Dog.class);
    operations.dropCollection(Apple.class);
  }

  @Test
  public void testAddTestHelper() throws Exception {
    instance.addTestHelper("/data/multi/test-apples.json", Apple.class);
    assertNotNull(instance.getHelperFor(Apple.class));
  }

  @Test
  public void testGetHelperFor() throws Exception {
    instance.addTestHelper("/data/multi/test-dogs.json", Dog.class);
    assertNotNull(instance.getHelperFor(Dog.class));
    assertNull(instance.getHelperFor(Apple.class));
  }

  @Test
  public void testSaveAllData() throws Exception {
    instance.addTestHelper("/data/multi/test-dogs.json", Dog.class);
    instance.addTestHelper("/data/multi/test-apples.json", Apple.class);
    instance.saveAllData();
    assertEquals(2, operations.findAll(Apple.class).size());
    assertEquals(3, operations.findAll(Dog.class).size());
  }

  @Test
  public void testSaveDataFor() throws Exception {
    instance.addTestHelper("/data/multi/test-dogs.json", Dog.class);
    instance.addTestHelper("/data/multi/test-apples.json", Apple.class);
    instance.saveDataFor(Dog.class);
    assertEquals(0, operations.findAll(Apple.class).size());
    assertEquals(3, operations.findAll(Dog.class).size());
    instance.saveDataFor(Apple.class);
    assertEquals(2, operations.findAll(Apple.class).size());
  }

  @Test
  public void testGetDataFor() throws Exception {
    instance.addTestHelper("/data/multi/test-dogs.json", Dog.class);
    instance.addTestHelper("/data/multi/test-apples.json", Apple.class);
    List<Dog> dogs = instance.getDataFor(Dog.class);
    assertEquals(3, dogs.size());
    Set<Long> ids = Sets.newHashSet(197L, 245L, 246L);
    for (Dog dog : dogs) {
      ids.remove(dog.id);
    }
    assertThat(ids, empty());
  }

  @Test
  public void testIsAllDataValid() throws Exception {
    instance.addTestHelper("/data/multi/test-dogs.json", Dog.class);
    instance.addTestHelper("/data/multi/test-apples.json", Apple.class);
    assertTrue(instance.isAllDataValid());
    instance.addTestHelper("/data/multi/non-exist.json", Dog.class);
    assertFalse(instance.isAllDataValid());
  }

  /**
   * This is a contrived test that verifies that we can use the {@link MultiTestDataHelper} to
   * save data in multiple collections, that have references from one to the other and we can
   * navigate the relationship
   *
   * @throws IOException
   */
  @Test
  public void testCanCrossRef() throws IOException {
    instance.addTestHelper("/data/multi/test-dogs.json", Dog.class);
    instance.addTestHelper("/data/multi/test-apples.json", Apple.class);
    instance.saveAllData();
    Dog german = operations.findOne(query(where("name").is("Fritz")), Dog.class);
    assertNotNull(german);
    assertEquals((Long)2L, german.appleId);
    Apple appleFritzLikes = operations.findById(german.appleId, Apple.class);
    assertNotNull(appleFritzLikes);
    assertEquals("Trentina", appleFritzLikes.variety);
  }
}
