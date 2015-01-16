//  Copyright Marco Massenzio (c) 2015.
//  This code is licensed according to the terms of the Apache 2 License.
//  See http://www.apache.org/licenses/LICENSE-2.0

package com.alertavert.template.util;

import java.util.List;

/**
 * <h3>TestData</h3>
 *
 * <p>Generic test data wrapper, to be used alongside Jackson's ObjectMapper to read in JSON test
 * data.
 *
 * See the design doc in docs/JsonTestParser.rst
 *
 * @author mmassenzio (1/7/15)
 */
public class TestData<E> {

  String title;
  String collection;
  List<E> data;

  /** This should not be created directly, use {@link com.fasterxml.jackson.databind .ObjectMapper} */
  private TestData() {}

  public String getTitle() {
    return title;
  }

  public String getCollection() {
    return collection;
  }

  public List<E> getData() {
    return data;
  }
}
