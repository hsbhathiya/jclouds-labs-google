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

import java.util.Map;

import org.jclouds.googlecloudstorage.domain.ObjectTemplate;
import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.rest.MapBinder;

import com.google.common.hash.HashCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleUploadBinder implements MapBinder {

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams)
            throws IllegalArgumentException {
      ObjectTemplate postObject = (ObjectTemplate) postParams.get("template");
      Payload payload = postObject.getPayload();

      String contentType = checkNotNull(postObject.getContentType(), "contentType");
      Long lenght = checkNotNull(postObject.getSize(), "contetLength");
      String md5 = postObject.getMd5Hash();

      request.toBuilder().removeHeader("Content-Type").addHeader("Content-Type", contentType)
               .addHeader("Content-Length", lenght.toString()).build();
      if(md5 != null){
         request.toBuilder().addHeader("Content-MD5", md5);
      }
      
      //request.getPayload().getContentMetadata().setContentType(contentType);
      request.setPayload(payload);
      return bindToRequest(request, payload);
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      return request;
   }
}
