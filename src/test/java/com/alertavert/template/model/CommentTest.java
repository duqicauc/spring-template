// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testThis() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Comment c = new Comment("this is the content", "commenter");
        String parsed = mapper.writeValueAsString(c);
        assertThat(parsed, stringContainsInOrder(Arrays.asList("this is the content",
                "commenter")));

        String json = "{\"commenter\": \"joe\", \"content\": \"A report of an issue\"}";
        Comment v = mapper.readValue(json, Comment.class);
        assertEquals("joe", v.getCommenter());
        assertEquals("A report of an issue", v.getContent());
    }
}
