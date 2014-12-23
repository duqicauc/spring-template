// Copyright (c) 2014 Elementum SCM, Inc.
// All rights reserved.  http://www.elementum.com

package com.alertavert.template.persistence;

import com.alertavert.template.AppConfiguration;
import com.alertavert.template.SampleDataRestApplication;
import com.alertavert.template.model.Issue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests that we can save Site objects in the `sites` collection in Mongo
 *
 * @author mmassenzio@elementum.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleDataRestApplication.class,
                                           AppConfiguration.class})
// Separate profile for web tests to avoid clashing databases
@ActiveProfiles("test")
public class SitesPersistenceTest {

  @Autowired
  MongoOperations ops;

  @Autowired
  IssueRepository repository;

  @Before
  public void cleanupDb() {
    ops.dropCollection(Issue.class);
  }

  @Test
  public void canSave() {
    Issue issue = (Issue.builder("bob", "doesn't work!")).newBug();
    repository.save(issue);
    assertNotNull(issue.getId());
    assertEquals(1, repository.count());
  }

  @Test
  public void canRetrieve() {
    Issue issue = (Issue.builder("bob", "doesn't work!")).newBug();
    issue.setId("foobar");
    repository.save(issue);

    Issue found = repository.findOne("foobar");
    assertEquals(issue, found);
  }
}
