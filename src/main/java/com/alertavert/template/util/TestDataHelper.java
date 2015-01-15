/*
 * // Copyright Marco Massenzio (c) 2015.
 * // This code is licensed according to the terms of the Apache 2 License.
 * // See http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alertavert.template.util;

import com.google.common.base.Preconditions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h3>TestDataHelper</h3>
 *
 * <p>Utility to read in JSON test data and save it to the Spring Data repository
 *
 * @author mmassenzio (1/7/15)
 */
public class TestDataHelper<T> {

  private final Class<T> clazz;
  MongoOperations operations;

  String resourceName;

  InputStream inTestData;

  ObjectMapper mapper = new ObjectMapper();

  TestData<T> testData;

  /**
   * Constructor, associates the given test resource with this helper.
   *
   * <p>To set a specific {@link org.springframework.data.mongodb.core.MongoOperations}
   * field for this helper use {@link org.springframework.beans.factory.annotation.Autowired} in
   * your test class to obtain an instance of the {@literal MongoOperation} class
   *
   * @param resourceName the full path name of the test resource (must exist in the classpath):
   *                     note that this requires the absolute path (with leading `/`) (it uses
   *                     internally {@link Class#getResource(String)}
   * @param operations the mongo client to use for this helper
   * @param clazz this is sadly necessary, as Java does not allow one to do {@literal T.class}
   *
   */
  public TestDataHelper(String resourceName, MongoOperations operations, Class<T> clazz) {
    this.resourceName = resourceName;
    // Don't ever throw in a constructor; but no point in trying to initialize something that
    // doesn't exist
    if (isDataFileValid()) {
      this.inTestData = getClass().getResourceAsStream(resourceName);
    }
    this.operations = operations;
    this.clazz = clazz;
  }

  /**
   * Convenience method to ensure the resource passed in at construction resolved to a valid
   * data resource.
   *
   * @return {@literal true} if the class loader could resolve the passed in resource name
   */
  public boolean isDataFileValid() {
    return this.getClass().getResource(this.resourceName) != null;
  }

  private void readValues() throws IOException {
    Preconditions.checkNotNull(inTestData,
                               "Resource " + resourceName
                               + " either does not exist or is not a valid data file");
    testData = mapper.readValue(inTestData, new TypeReference<TestData<T>>() { });
    // at this point the inner data is not of the right type, but a List<Map<?, ?>>,
    // so we need to convert it
    List<T> typedResult = new ArrayList<>(testData.data.size());
    for (int i = 0; i < testData.getData().size(); ++i) {
      Map<?, ?> itemAsMap = (Map<?, ?>) testData.getData().get(i);
      T item = mapper.convertValue(itemAsMap, clazz);
      typedResult.add(item);
    }
    testData.data = typedResult;
  }

  public List<T> getTestData() throws IOException {
    if (testData == null) {
      readValues();
    }
    return testData.getData();
  }

  public void saveData() throws IOException {
    if (testData == null) {
      readValues();
    }
    if ((testData == null) || (StringUtils.isEmpty(testData.getCollection())) ||
        (CollectionUtils.isEmpty(testData.getData()))) {
      throw new IllegalStateException(String.format(
          "Could not retrieve a valid data set for %s", resourceName
      ));
    }
    for (T item : testData.getData()) {
      operations.save(item, testData.getCollection());
    }
  }

}
