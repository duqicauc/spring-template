// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

package com.alertavert.template.resources;

import com.alertavert.template.BasePersistenceTest;
import com.alertavert.template.model.Comment;
import com.alertavert.template.model.Issue;
import com.alertavert.template.persistence.IssueRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class IssuesServiceTest extends BasePersistenceTest {

    @Autowired
    private IssueRepository repo;

    @Autowired
    private IssuesService service;

    private Issue buildIssue() {
        return (Issue.builder("Joe Reporter", "The gizmo foomps the droid")).newBug();
    }

    @Test
    public void testGetIssue() throws Exception {
        Issue one = repo.save(buildIssue());
        Issue found = service.getIssue(one.getId());
        assertEquals(one, found);
    }

    @Test
    public void testReportNewIssue() throws Exception {
        Issue newIssue = buildIssue();
        service.reportNewIssue(newIssue);
        Iterator<Issue> found = repo.findAll().iterator();
        int count = 0;
        while (found.hasNext()) {
            count++;
            found.next();
        }
        assertEquals(1, count);
    }

    @Test
    public void testAddComment() throws Exception {
        Issue newIssue = buildIssue();
        service.reportNewIssue(newIssue);

        Comment comment = new Comment("This is a serious issue", "the_president");
        service.addComment(newIssue.getId(), comment, true);

        Issue found = repo.findOne(newIssue.getId());
        assertEquals(1, found.getComments().size());
        assertEquals(1, found.getWatchers().size());
        assertEquals(found.getWatchers().get(0), found.getComments().get(0).getCommenter());
    }
}
