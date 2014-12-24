// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

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
