// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template;

import com.alertavert.template.model.Comment;
import com.alertavert.template.model.Issue;
import com.alertavert.template.resources.IssuesService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "/issue/{id}/comment", method = RequestMethod.POST)
    public Issue createIssue(@PathVariable String id,
                             @RequestBody Comment comment,
                             @RequestParam(required = false, defaultValue = "true") boolean track) {
        if (StringUtils.isNotBlank(id)) {
            LOG.info("Adding comment to issue {}", id);
            Issue issue = service.getIssue(id);
            if (issue != null) {
                issue.addComment(comment, track);
                service.save(issue);
                return issue;
            }
        } else {
            throw new IllegalArgumentException("Issued ID must be non-blank");
        }
        throw new IllegalStateException(String.format("Could not find issue [%s]", id));
    }
}

