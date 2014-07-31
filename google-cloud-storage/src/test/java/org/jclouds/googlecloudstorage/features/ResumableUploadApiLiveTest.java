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
package org.jclouds.googlecloudstorage.features;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.InitResumbleUpload;
import org.jclouds.googlecloudstorage.domain.templates.ObjectTemplate;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.ByteSourcePayload;
import org.testng.annotations.Test;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class ResumableUploadApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   private static final String DESTINATION_BUCKET_NAME = "jcloudsresumableuploadbucket";
   private static final String UPLOAD_OBJECT_NAME = "jcloudslogo.jpg";
   private static final String CHUNKED_OBJECT_NAME = "jclouds.pdf";

   private ResumableUploadApi api() {
      return api.getResumableUploadApi();
   }

   @Test(groups = "live")
   public void testResumableJpegUpload() throws IOException {

      // Read Object
      ByteSource byteSource = Files.asByteSource(new File(Resources.getResource(getClass(), "/jcloudslogo.jpg")
               .getPath()));

      // Initialize resumableUpload with metadata. ObjectTemaplete must provide the name
      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(DESTINATION_BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ObjectTemplate template = new ObjectTemplate();
      template.contentType("image/jpeg").addAcl(oacl).size(Long.valueOf(byteSource.read().length + ""))
               .name(UPLOAD_OBJECT_NAME).contentLanguage("en").contentDisposition("attachment");

      InitResumbleUpload initResponse = api().initResumableUpload(DESTINATION_BUCKET_NAME, "image/jpeg",
               byteSource.read().length + "", template);

      assertNotNull(initResponse);
      assertEquals(initResponse.getStatusCode().intValue(), 200);
      assertNotNull(initResponse.getUpload_id());

      // Get the upload_id for the session
     // ResumableUploadResponseDecoder decoder = new ResumableUploadResponseDecoder(initResponse);

      //assertNotNull(decoder.getUploadId());
      String uploadId = initResponse.getUpload_id();

      // Upload the payload
      ByteSourcePayload payload = Payloads.newByteSourcePayload(byteSource);
      InitResumbleUpload uploadResponse = api().upload(DESTINATION_BUCKET_NAME, uploadId, "image/jpeg",
               byteSource.read().length + "", payload);

      assertEquals(uploadResponse.getStatusCode().intValue(), 200);

      // CheckStatus
      InitResumbleUpload status = api().checkStatus(DESTINATION_BUCKET_NAME, uploadId, "bytes */*");

      int code = status.getStatusCode();
      assert (code != 308);

   }

   @Test(groups = "live")
   public void testResumableChunkedUpload() throws IOException, InterruptedException {

      // Read Object
      ByteSource byteSource = Files.asByteSource(new File(Resources.getResource(getClass(), "/jclouds.pdf")
               .getPath()));

      // Initialize resumableUpload with metadata. ObjectTemaplete must provide the name
      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(DESTINATION_BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ObjectTemplate template = new ObjectTemplate();
      template.contentType("application/pdf").addAcl(oacl).size(Long.valueOf(byteSource.read().length + ""))
               .name(CHUNKED_OBJECT_NAME).contentLanguage("en").contentDisposition("attachment");

      InitResumbleUpload initResponse = api().initResumableUpload(DESTINATION_BUCKET_NAME, "application/pdf",
               byteSource.read().length + "", template);

      assertNotNull(initResponse);
      assertEquals(initResponse.getStatusCode().intValue(), 200);
      assertNotNull(initResponse.getUpload_id());

      // Get the upload_id for the session
      //ResumableUploadResponseDecoder decoder = new ResumableUploadResponseDecoder(initResponse);

     // assertNotNull(decoder.getUploadId());
      String uploadId = initResponse.getUpload_id();

      // Upload the payload


      //Check the  status first          
      InitResumbleUpload status = api().checkStatus(DESTINATION_BUCKET_NAME, uploadId, "bytes */*");
      int code = status.getStatusCode();
      assertEquals(code, 308);
      
      ///Uploads in 2 chunks.
      long totalSize = byteSource.read().length;
      long offset = 0;
      //Size of the first chunk
      long chunkSize = 256*1000*5; //Min allowed size for a chunk = 256*1000 ; 
    
      //Uploading First chunk
      ByteSourcePayload payload = Payloads.newByteSourcePayload(byteSource.slice(offset, chunkSize));
      long length = byteSource.slice(offset, chunkSize).size();
      String Content_Range = "bytes " + 0 + "-" + length + "/" + totalSize;
      
      InitResumbleUpload uploadResponse = api().chunkUpload(DESTINATION_BUCKET_NAME, uploadId, "application/pdf",
               length + "", Content_Range, payload);
      
      int code2 = uploadResponse.getStatusCode();      
      assertEquals(code2, 308);
      
      //Read uploaded length 
      long uploaded = uploadResponse.getRangeUpperValue();
      
      long resumeLength = totalSize - (uploaded+1); //byteSource.slice(uploaded+1, byteSource.read().length-uploaded-1).size();
      
      //2nd chunk
      ByteSourcePayload payload2 = Payloads.newByteSourcePayload(byteSource.slice(uploaded+1, byteSource.read().length-uploaded-1));
      //Upload the 2nd chunk
      String Content_Range2 = "bytes " + (uploaded+1) + "-" + (totalSize-1) + "/" + totalSize;
      InitResumbleUpload resumeResponse = api().chunkUpload(DESTINATION_BUCKET_NAME, uploadId, "application/pdf",
               resumeLength + "", Content_Range2, payload2);
      
      int code3 = resumeResponse.getStatusCode();      
      assert(code3 == 200 || code3 == 201);    //200 or 201 if upload succeeded 
 
   }

}
