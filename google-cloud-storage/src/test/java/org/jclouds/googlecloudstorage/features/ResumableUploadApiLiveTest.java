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
import java.net.URI;

import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectTemplate;
import org.jclouds.googlecloudstorage.domain.ResumableUploadResponse;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.ByteSourcePayload;
import org.testng.annotations.Test;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class ResumableUploadApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   private static final String DESTINATION_BUCKET_NAME = "jcloudsresumableupload";
   private static final String UPLOAD_OBJECT_NAME = "jcloudslogo.jpg";

   private ResumableUploadApi api() {
      return api.getResumableUploadApi();
   }

   @Test(groups = "live")
   public void testResumableJpegUpload() throws IOException {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(DESTINATION_BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ByteSource byteSource = Files.asByteSource(new File(Resources.getResource(getClass(), "/jcloudslogo.jpg")
               .getPath()));

      ObjectTemplate template = new ObjectTemplate();
      template.contentType("image/jpeg").addAcl(oacl).size(Long.valueOf(byteSource.read().length + ""))
               .name(UPLOAD_OBJECT_NAME).contentLanguage("en").contentDisposition("attachment");

      HttpResponse initResponse = api().initResumableUpload(DESTINATION_BUCKET_NAME, "image/jpeg",
               byteSource.read().length + "", template);

      assertNotNull(initResponse);
      assertEquals(initResponse.getStatusCode(), 200);
      assertNotNull(initResponse.getFirstHeaderOrNull("Location"));
      
      ResumableUploadResponse decoder = new ResumableUploadResponse(initResponse);
      
      assertNotNull(decoder.getUploadId());
      String uploadId = decoder.getUploadId();

      ByteSourcePayload payload = Payloads.newByteSourcePayload(byteSource);
      HttpResponse uploadResponse = api().upload(DESTINATION_BUCKET_NAME, uploadId, "image/jpeg", byteSource.read().length + "",
               payload);
      
      assertEquals(uploadResponse.getStatusCode(), 200);

   }

}
