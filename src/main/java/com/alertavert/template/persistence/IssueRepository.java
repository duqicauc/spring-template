// Copyright AlertAvert.com (c) 2014. All rights reserved.
// Commercial use or modification of this software without a valid license is expressly forbidden

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
