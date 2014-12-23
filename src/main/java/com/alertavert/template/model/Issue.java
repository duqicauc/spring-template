// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

package com.alertavert.template.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Simple entity document, encapsulate an Issue (similar to a bug report).
 *
 * Created by mmassenzio on 12/9/14.
 */

@Document(collection = "issues")
public class Issue {

  @Id
  private String id;

  protected IssueType type;

  String reporter;
  String assignee = "";
  Set<String> watchers = Sets.newHashSet();

  String title;
  List<Comment> comments = Lists.newArrayList();

  Date created;
  Date updated;

  private Issue() {
  }

  public static Builder builder(String reporter, String title) {
    return new Builder(reporter, title);
  }

  public static class Builder {
    private Issue instance = new Issue();
    private Builder(String reporter, String title) {
      instance.reporter = reporter;
      instance.title = title;
      instance.created = new Date();
      instance.updated = new Date();
    }

    public Builder withId(String id) {
      instance.id = id;
      return this;
    }

    public Builder assignTo(String assignee) {
      instance.assignee = assignee;
      return this;
    }

    public Issue newBug() {
      instance.type = IssueType.BUG;
      return instance;
    }

    public Issue newFeature() {
      instance.type = IssueType.FEATURE;
      return instance;
    }

    public Issue newImprovement() {
      instance.type = IssueType.IMPROVEMENT;
      return instance;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IssueType getType() {
    return type;
  }

  public void setType(IssueType type) {
    this.type = type;
  }

  public String getReporter() {
    return reporter;
  }

  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public List<String> getWatchers() {
    return Lists.newArrayList(watchers);
  }

  public void addWatcher(String watcher) {
    this.watchers.add(watcher);
  }

  public void removeWatcher(String watcher) {
    watchers.remove(watcher);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void editComment(int i, Comment comment) {
    this.comments.set(i, comment);
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Issue issue = (Issue) o;

    if (assignee != null ? !assignee.equals(issue.assignee) : issue.assignee != null) return false;
    if (!created.equals(issue.created)) return false;
    if (id != null ? !id.equals(issue.id) : issue.id != null) return false;
    if (!reporter.equals(issue.reporter)) return false;
    if (!title.equals(issue.title)) return false;
    if (type != issue.type) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + type.hashCode();
    result = 31 * result + reporter.hashCode();
    result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
    result = 31 * result + title.hashCode();
    result = 31 * result + created.hashCode();
    return result;
  }
}
