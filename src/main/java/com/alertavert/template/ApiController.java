// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

package com.alertavert.template;

import com.alertavert.template.model.Comment;
import com.alertavert.template.model.Issue;
import com.alertavert.template.resources.IssuesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The main API controller
 *
 * Created by marco on 12/23/14.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    private final IssuesService service;

    @Autowired
    public ApiController(IssuesService service) {
        this.service = service;
    }


    @RequestMapping(value = "/issue", method = RequestMethod.POST,
            produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Issue createIssue(@RequestBody Issue newIssue) {
        String id = service.reportNewIssue(newIssue);
        if (id == null) {
            throw new IllegalStateException("Could not create new issue");
        }
        // TODO: add Location header with URI for this issue
        return newIssue;
    }

    @RequestMapping(value = "/issues", method=RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    List<Issue> getAllIssues() {
        LOG.warn("Retrieving all issues - this may take a while, use paging instead");
        return service.getAllIssues();
    }

    @RequestMapping(value = "/issue/{id}/comment", method = RequestMethod.POST,
                    produces = APPLICATION_JSON_VALUE,
                    consumes = APPLICATION_JSON_VALUE)
    public Issue createIssue(@PathVariable String id, @RequestBody Comment comment) {
        if (StringUtils.isNotBlank(id)) {
            LOG.info("Adding comment to " + id);
            Issue issue = service.getIssue(id);
            if (issue != null) {
                issue.addComment(comment, true);
                service.save(issue);
                return issue;
            }
        } else {
            throw new IllegalArgumentException("Issued ID must be non-blank");
        }
        throw new IllegalStateException(String.format("Could not find issue [%s]", id));
    }
}

