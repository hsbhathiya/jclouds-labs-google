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
package org.jclouds.googlecloudstorage.binders;

import java.util.Map;

import org.jclouds.googlecloudstorage.domain.templates.ObjectTemplate;
import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;

import com.google.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class MultipartUploadBinder implements MapBinder {
      
   //Not functioning
   @Inject
   private BindToJsonPayload jsonBinder;
  
   private final String BOUNDARY_HEADER = "multipart_boundary";
   
    @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams)
            throws IllegalArgumentException {   
      
      ObjectTemplate postObject = (ObjectTemplate) postParams.get("template");
      Payload payload  = (Payload) postParams.get("payload");
           
      String contentType = checkNotNull(postObject.getContentType(), "contentType");
      Long lenght = checkNotNull(postObject.getSize(), "contetLength");
    
      //HeaderPart
      request.toBuilder().removeHeader("Content-Type").addHeader("Content-Type", "Multipart/related; boundary= "+ BOUNDARY_HEADER )
               .addHeader("Content-Length", lenght.toString()).build();
      
      return bindToRequest(request, postObject);
      
      
      //Metadata Part
    /*  request.setPayload("--" + BOUNDARY_HEADER);
      request.toBuilder().addHeader("Content-Type", MediaType.APPLICATION_JSON);
     
      request.setPayload("--" + BOUNDARY_HEADER);  //End of metadapart
      
      //Media Part
      request.toBuilder().addHeader("Content-Type", contentType);  
      request.setPayload(payload);  
      request.setPayload("--" + BOUNDARY_HEADER +"--");    
      
      payload.getContentMetadata().setContentType("image/jpeg");
      payload.getContentMetadata().setContentLength(lenght);
      payload.getContentMetadata().setContentLanguage("en");
      request.setPayload(payload);
      return request;*/
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      return jsonBinder.bindToRequest(request, input);
   }
}
