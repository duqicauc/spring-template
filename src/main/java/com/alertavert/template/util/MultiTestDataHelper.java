/*
 * // Copyright Marco Massenzio (c) 2015.
 * // This code is licensed according to the terms of the Apache 2 License.
 * // See http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alertavert.template.util;

import org.springframework.data.mongodb.core.MongoOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>MultiTestDataHelper</h3>
 *
 * <p>Enhances the functionality of a {@link TestDataHelper} for those situations where we need
 * to save multiple data files to (possibly) different collections.
 *
 * <p>It does this a naive way, but just owning references to several TestDataHelpers and
 * delegating the work to them</p>
 *
 * @author mmassenzio (1/14/15)
 */
public class MultiTestDataHelper {

  private Map<Class<?>, TestDataHelper<?>> helpersMap = new HashMap<>();

  private final MongoOperations ops;

  public MultiTestDataHelper(MongoOperations ops) {
    this.ops = ops;
  }

  public void addTestHelper(String resourceName, Class<?> classFor) {
    helpersMap.put(classFor, new TestDataHelper<>(resourceName, ops, classFor));
  }

  @SuppressWarnings("unchecked")
  public<T> TestDataHelper<T> getHelperFor(Class<T> classFor) {
    return (TestDataHelper<T>) helpersMap.get(classFor);
  }

  public void saveAllData() throws IOException {
    for (TestDataHelper<?> helper : helpersMap.values()) {
      helper.saveData();
    }
  }

  public<T> void saveDataFor(Class<T> clazz) throws IOException {
    getHelperFor(clazz).saveData();
  }

  public<T> List<T> getDataFor(Class<T> clazz) throws IOException {
    return getHelperFor(clazz).getTestData();
  }

  public boolean isAllDataValid() {
    for (TestDataHelper<?> helper : helpersMap.values()) {
      if (!helper.isDataFileValid()) return false;
    }
    return true;
  }
}
