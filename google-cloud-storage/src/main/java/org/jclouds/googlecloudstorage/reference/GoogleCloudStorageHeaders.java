/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.googlecloudstorage.reference;

import com.google.common.annotations.Beta;

public final class GoogleCloudStorageHeaders {

   private GoogleCloudStorageHeaders() {
   }

   // Request headers
   public static final String DEFAULT_GOOGLE_CLOUD_HEADERTAG = "goog";

   public static final String HEADER_PREFIX = "x-" + DEFAULT_GOOGLE_CLOUD_HEADERTAG + "-";

   public static final String ACL = HEADER_PREFIX + "acl";

   public static final String API_VERSION = HEADER_PREFIX + "api-version";

   public static final String COPY_SOURCE = HEADER_PREFIX + "copy-source";

   public static final String COPY_SOURCE_GENERATION = HEADER_PREFIX + "copy-source-generation";

   public static final String COPY_SOURCE_IF_GENERATION_MATCH = HEADER_PREFIX + "copy-source-if-generation-match";

   public static final String COPY_SOURCE_IF_MATCH = HEADER_PREFIX + "copy-source-if-match";

   public static final String COPY_SOURCE_IF_METAGENERATION_MATCH = HEADER_PREFIX + "metageneration-match";

   public static final String COPY_SOURCE_IF_MODIFIED_SINCE = HEADER_PREFIX + "copy-source-if-modified-since";

   public static final String COPY_SOURCE_IF_NONE_MATCH = HEADER_PREFIX + "copy-source-if-none-match";

   public static final String COPY_SOURCE_IF_UNMODIFIED_SINCE = HEADER_PREFIX + "copy-source-if-unmodified-since";

   public static final String METADATA_DIRECTIVE = HEADER_PREFIX + "metadata-directive";

   public static final String PROJECT_ID = HEADER_PREFIX + "project-id";

   public static final String RESUMABLE = HEADER_PREFIX + "resumable";

   public static final String DATE = HEADER_PREFIX + "date";

   // Used both request and response

   public static final String HASH = HEADER_PREFIX + "hash";

   public static final String META_ = HEADER_PREFIX + "meta-";

   // Response Headers

   public static final String COMPONENT_COUNT = HEADER_PREFIX + "component-count";

   public static final String EXPIRATION = HEADER_PREFIX + "expiration";

   public static final String GENERATION = HEADER_PREFIX + "generation";

   public static final String METAGENERATION = HEADER_PREFIX + "metageneration";

   public static final String STORED_CONTENT_ENCODING = HEADER_PREFIX + "stored-content-encoding";

   public static final String STORED_CONTENT_LENGTH = HEADER_PREFIX + "stored-content-length";

}
