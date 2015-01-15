/*
 * // Copyright Marco Massenzio (c) 2015.
 * // This code is licensed according to the terms of the Apache 2 License.
 * // See http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alertavert.template.util;

import com.alertavert.template.SampleDataRestApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.repository.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * <h3>AbstractTestBase</h3>
 *
 * <p>TODO: Please add class description here
 *
 * @author mmassenzio (1/14/15)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class})
// Separate profile for web tests to avoid clashing databases
@ActiveProfiles("test")
public abstract class AbstractTestBase<T> {

  protected final static Logger LOG = LoggerFactory.getLogger(AbstractTestBase.class);

  @Inject
  protected MongoOperations ops;

  protected ObjectMapper mapper = new ObjectMapper();

  protected abstract Repository<T, ? extends Serializable> getRepository();

  /**
   * <p>This is required because Java does not allow to extract the {@literal class} static
   * attribute from a generic type:
   * <pre>
   *   Class&lt;T&gt; = T.class
   * </pre>
   * will cause a compilation error.
   *
   * <p>Any derived (concrete) implementations of this test class will have to "inject" the
   * correct value into {@literal clazz} <strong>before</strong> the test starts.
   */
  protected Class<T> clazz;

  /**
   * Convenience for derived classes to use a pre-configured {@link TestDataHelper} accessing
   * data from the default test data file.
   *
   * <p>Best not to access it directly, but to use {@link #getTestDataHelper()}</p>
   *
   * @see #getDefaultResource()
   * @see #getTestDataHelper()
   */
  protected TestDataHelper<T> testDataHelper;

  /**
   * The full path of the default data set that will be loaded when {@link #testDataHelper} is
   * constructed.
   *
   * @return the full path of a valid data file, in the classpath
   */
  protected abstract String getDefaultResource();

  /**
   * Drops the collection associated with the class under test in this test.
   * You must initialize the {@link #clazz} member first.
   */
  public void clearColl() {
    assertNotNull("The `clazz` attribute must be initialized before you call the clearColl() method",
                  clazz);
    String collectionName = ops.getCollectionName(clazz);
    LOG.debug("Dropping collection {}", collectionName);
    ops.getCollection(collectionName).drop();
  }

  /**
   * Gets the default data helper, lazily created (once).
   *
   * @return the {@link #testDataHelper}, initializing it, if necessary
   */
  public TestDataHelper<T> getTestDataHelper() {
    if (testDataHelper == null) {
      LOG.debug("Creating default TestDataHelper for {} ({})", clazz.getName(),
                getDefaultResource());
      testDataHelper = createTestDataHelper(getDefaultResource());
      assertNotNull("Could not initialize the TestDataHelper Object", testDataHelper);
    }
    return testDataHelper;
  }

  /**
   * Convenience method to create a {@link TestDataHelper}
   *
   * @param resourceName the name of the classpath resource containing the data to persist
   * @return a newly created data helper, from the {@literal resourceName}
   */
  public TestDataHelper<T> createTestDataHelper(String resourceName) {
    return new TestDataHelper<>(resourceName, ops, clazz);
  }

  public ObjectMapper getMapper() {
    return mapper;
  }

  public MongoOperations getOps() {
    return ops;
  }
}
