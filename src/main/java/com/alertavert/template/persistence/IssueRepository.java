// Copyright (c) 2014 Elementum SCM, Inc.
// All rights reserved.  http://www.elementum.com

package com.alertavert.template.persistence;

import com.alertavert.template.model.Issue;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * <p>This is a very basic initial implementation that only enables basic upsert and querying
 * capabilities</p>
 *
 * Created by mmassenzio on 12/9/14.
 */
public interface IssueRepository extends PagingAndSortingRepository<Issue, String> {

  Issue findByReporter(String reporter);
}
