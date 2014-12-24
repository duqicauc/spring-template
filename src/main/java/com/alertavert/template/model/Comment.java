// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.model;

import java.util.Date;

/**
 * Value class, encapsulates the concept of a comment to an Issue
 *
 * This is an immutable class, once created all values cannot be modified.
 *
 * Created by marco on 12/22/14.
 */
public class Comment {
    private final String content;
    private final String commenter;
    private final Date created;

    private Comment() {
        created = new Date();
        commenter = "";
        content = "";
    }


    public Comment(String content, String commenter) {
        this.created = new Date();
        this.commenter = commenter;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getCommenter() {
        return commenter;
    }

    public Date getCreated() {
        return created;
    }
}
