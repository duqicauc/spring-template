// Copyright Marco Massenzio (c) 2014.
// This code is licensed according to the terms of the Apache 2 License.
// See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.persistence;

import com.alertavert.template.model.Issue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * <p>This is a very basic initial implementation that only enables basic upsert and querying
 * capabilities</p>
 *
 * Created by mmassenzio on 12/9/14.
 */
@RepositoryRestResource(path = "issue")
public interface IssueRepository extends PagingAndSortingRepository<Issue, String> {

  Issue findByReporter(@Param("reporter")String reporter);
}
