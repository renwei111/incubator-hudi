/*
 * Copyright (c) 2016 Uber Technologies, Inc. (hoodie-dev-group@uber.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uber.hoodie.common.util;

import com.twitter.common.objectsize.ObjectSizeCalculator;
import com.uber.hoodie.common.model.HoodieRecord;
import com.uber.hoodie.common.model.HoodieRecordPayload;
import org.apache.avro.Schema;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Size Estimator for Hoodie record payload
 * @param <T>
 */
public class HoodieRecordSizeEstimator<T extends HoodieRecordPayload> implements SizeEstimator<HoodieRecord<T>> {

  private static Logger log = LogManager.getLogger(HoodieRecordSizeEstimator.class);

  // Schema used to get GenericRecord from HoodieRecordPayload then convert to bytes and vice-versa
  private final Schema schema;

  public HoodieRecordSizeEstimator(Schema schema) {
    this.schema = schema;
  }

  @Override
  public long sizeEstimate(HoodieRecord<T> hoodieRecord) {
    // Most HoodieRecords are bound to have data + schema. Although, the same schema object is shared amongst
    // all records in the JVM. Calculate and print the size of the Schema and of the Record to
    // note the sizes and differences. A correct estimation in such cases is handled in
    /** {@link com.uber.hoodie.common.util.collection.ExternalSpillableMap} **/
    long sizeOfRecord = ObjectSizeCalculator.getObjectSize(hoodieRecord);
    long sizeOfSchema = ObjectSizeCalculator.getObjectSize(schema);
    log.info("SizeOfRecord => " + sizeOfRecord + " SizeOfSchema => " + sizeOfSchema);
    return sizeOfRecord;
  }
}
