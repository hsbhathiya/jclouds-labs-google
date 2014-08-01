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
package org.jclouds.googlecloudstorage.parser;

import java.util.HashMap;
import java.util.Map;

import org.jclouds.googlecloudstorage.domain.ResumableUpload;
import org.jclouds.http.HttpResponse;

import com.google.common.base.Function;

public class ParseToResumableUpload implements Function<HttpResponse, ResumableUpload> {

   @Override
   public ResumableUpload apply(HttpResponse response) {

      String contentLength = response.getFirstHeaderOrNull("Content-Length");
      String session_uri = response.getFirstHeaderOrNull("Location");
      String uploadId = null;
      if (session_uri != null) {
         uploadId = getUploadId(session_uri);
      }
      String range = response.getFirstHeaderOrNull("Range");
      Long upperLimit = null;
      Long lowwerLimit = null;
      if(range != null){
         upperLimit = getUpperLimitFromRange(range);
         upperLimit = getLowerLimitFromRange(range);
      }

      
      return ResumableUpload.builder().statusCode(response.getStatusCode()).contentLength(contentLength)
               .upload_id(uploadId).rangeUpperValue(upperLimit).rangeLowerValue(lowwerLimit).build();
   }

   // Return the Id of the Upload
   private String getUploadId(String session_uri) {

      String[] params = session_uri.split("&");
      Map<String, String> map = new HashMap<String, String>();
      for (String param : params) {
         String name = param.split("=")[0];
         String value = param.split("=")[1];
         map.put(name, value);
      }
      return map.get("upload_id");
   }

   private long getUpperLimitFromRange(String range) {
      String upperLimit = range.split("-")[1];
      return Long.parseLong(upperLimit);

   }

   private long getLowerLimitFromRange(String range) {
      String removeByte =  range.split("=")[1];
      String lowerLimit = removeByte.split("-")[0];
      return Long.parseLong(lowerLimit);

   }
}
