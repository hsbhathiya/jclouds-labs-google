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
package org.jclouds.googlecloudstorage.domain;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.jclouds.http.HttpResponse;
import org.jclouds.http.UriTemplates;
import org.jclouds.io.Payload;

import static com.google.common.base.Preconditions.checkNotNull;
import ch.qos.logback.classic.pattern.RelativeTimeConverter;

import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;

public class ResumableUploadResponse {

   private String session_uri;
   private String contentLength;
   private String range;
   private String contentRange;

   public ResumableUploadResponse(HttpResponse response) {
      this.session_uri = response.getFirstHeaderOrNull("Location");
      this.contentLength = response.getFirstHeaderOrNull("Content-Length");
      this.range = response.getFirstHeaderOrNull("Range");
      this.contentRange = response.getFirstHeaderOrNull("Content-Range");
   }

   
   public  String getUploadId() {  
      String query = session_uri;
       String[] params = query.split("&");  
       Map<String, String> map = new HashMap<String, String>();  
       for (String param : params)  
       {  
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
}
