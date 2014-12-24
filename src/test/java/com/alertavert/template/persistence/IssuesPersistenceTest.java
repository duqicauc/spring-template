// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

package com.alertavert.template.persistence;

import com.alertavert.template.AppConfiguration;
import com.alertavert.template.BasePersistenceTest;
import com.alertavert.template.SampleDataRestApplication;
import com.alertavert.template.model.Comment;
import com.alertavert.template.model.Issue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
// Separate profile for web tests to avoid clashing databases
@ActiveProfiles("test")
public class IssuesPersistenceTest extends BasePersistenceTest {

  @Autowired
  IssueRepository repository;

  /**
   * Create a simple issue for test purposes, saves to DB and returns it.
   *
   * @return the newly created issue
   */
  public Issue createAndSaveIssue() {
    Issue issue = (Issue.builder("Joe Reporter", "The gizmo foomps the droid")).newBug();
    return repository.save(issue);
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

  @Test
  public void canAddComment() {
    Issue one = createAndSaveIssue();
    one.addComment(new Comment("what?", "Who?"));
    repository.save(one);

    // verify it updated the current one, did not save it as new
    assertEquals(1, repository.count());
    Issue found = repository.findOne(one.getId());
    assertEquals(one, found);
    assertEquals(1, found.getComments().size());
  }

  @Test
  public void commentContentIsSaved() {
    Issue one = createAndSaveIssue();
    Comment description = new Comment(
            "When I grid the foop, the foor tohe does not wimps the timps",
            "Shrewd Commenter");
    one.addComment(description);
    repository.save(one);

    // verify it updated the current one, did not save it as new
    assertEquals(1, repository.count());
    Issue found = repository.findOne(one.getId());
    assertEquals(one, found);
    assertEquals(1, found.getComments().size());
    assertEquals(description.getContent(), found.getComments().get(0).getContent());
  }

  @Test
  public void commenterIsAddedAsWatcher() {
    Issue one = createAndSaveIssue();
    Comment description = new Comment(
            "Wanna really watch this!",
            "user_1");
    one.addComment(description, true);
    repository.save(one);
    Issue found = repository.findOne(one.getId());
    assertEquals(one, found);
    assertEquals(1, found.getWatchers().size());
    assertEquals("user_1", found.getWatchers().get(0));
  }

}
