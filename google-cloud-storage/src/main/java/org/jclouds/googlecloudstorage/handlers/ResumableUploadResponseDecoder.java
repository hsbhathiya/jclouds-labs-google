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
package org.jclouds.googlecloudstorage.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jclouds.http.HttpResponse;

public class ResumableUploadResponseDecoder {

   private String session_uri;
   private String contentLength;
   private String range;
   private String contentRange;

   public ResumableUploadResponseDecoder(HttpResponse response) {
      this.session_uri = response.getFirstHeaderOrNull("Location");
      this.contentLength = response.getFirstHeaderOrNull("Content-Length");
      this.range = response.getFirstHeaderOrNull("Range");
      this.contentRange = response.getFirstHeaderOrNull("Content-Range");
   }

   // Return the Id of the Upload
   public String getUploadId() {
      String query = session_uri;
      String[] params = query.split("&");
      Map<String, String> map = new HashMap<String, String>();
      for (String param : params) {
         String name = param.split("=")[0];
         String value = param.split("=")[1];
         map.put(name, value);
      }
      return map.get("upload_id");
   }

   public Long getContentLength() {
      if (this.contentLength != null) {
         return Long.valueOf(contentLength);
      }
      return null;
   }

   public String getRange() {
      return this.range;
   }

   public String getContentRange() {
      return this.contentRange;
   }

   public long getUpperLimitFromRange() {
      String range308 = this.range;
      String upperLimit = range308.split("-")[1];
      return Long.parseLong(upperLimit);

   }
}
