// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.resources;

import com.alertavert.template.model.Comment;
import com.alertavert.template.model.Issue;
import com.alertavert.template.persistence.IssueRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Layer - sits between the REST API and the Data layer.
 *
 * Created by marco on 12/23/14.
 */
@Service
public class IssuesService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssuesService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public Issue getIssue(String issueId) {
        return issueRepository.findOne(issueId);
    }

    public String reportNewIssue(Issue issue) {
        Issue saved = issueRepository.save(issue);
        return saved.getId();
    }

    public String addComment(String issueId, Comment comment, boolean shouldWatch) {
        Issue found = issueRepository.findOne(issueId);
        if (found == null) {
          return null;
        }
        found.addComment(comment, shouldWatch);
        Issue saved = issueRepository.save(found);
        return saved.getId();
    }

    public List<Issue> getAllIssues() {
        return Lists.newArrayList(issueRepository.findAll());
    }

    public void save(Issue issue) {
        issueRepository.save(issue);
    }
}


