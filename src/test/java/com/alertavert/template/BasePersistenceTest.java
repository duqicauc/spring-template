// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template;

import com.alertavert.template.model.Issue;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by marco on 12/23/14.
 */
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class,
        AppConfiguration.class})
public class BasePersistenceTest {
  @Autowired
  MongoOperations ops;

  @Before
  public void cleanupDb() {
    ops.dropCollection(Issue.class);
  }
}
