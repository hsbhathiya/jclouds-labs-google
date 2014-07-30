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

import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_FULLCONTROL_SCOPE;
import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_READONLY_SCOPE;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectTemplate;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Projection;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Role;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiExpectTest;
import org.jclouds.googlecloudstorage.options.GetBucketOptions;
import org.jclouds.googlecloudstorage.options.ListOptions;
import org.jclouds.googlecloudstorage.options.UpdateBucketOptions;
import org.jclouds.googlecloudstorage.parse.BucketUpdateTest;
import org.jclouds.googlecloudstorage.parse.NoAclBucketTest;
import org.jclouds.googlecloudstorage.parse.NoAclBucketListTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.ByteSourcePayload;
import org.testng.annotations.Test;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;

/*@Test(groups = "unit")
public class ObjectApiExpectTest extends BaseGoogleCloudStorageApiExpectTest {

   private static final String BUCKET_NAME = "jcloudobjectoperations";
   private final String BOUNDARY_HEADER = "multipart_boundary";
   private static final String DESTINATION_BUCKET_NAME = "jcloudobjectdestination";

   // Test getBucket without options
   public void testMultipartUpload() throws Exception {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ByteSource byteSource = Files.asByteSource(new File(Resources.getResource(getClass(), "/jcloudslogo.jpg")
               .getPath()));

      HashFunction hf = Hashing.md5();
      HashCode hc = hf.newHasher().putBytes(byteSource.read()).hash();

      ObjectTemplate template = new ObjectTemplate();
      template.contentType("image/jpeg").addAcl(oacl).size(Long.valueOf(byteSource.read().length + ""))
               .name(DESTINATION_BUCKET_NAME).md5Hash(hc).contentLanguage("en").contentDisposition("attachment");

      ByteSourcePayload payload = Payloads.newByteSourcePayload(byteSource);

      // InsertObjectOptions options = new InsertObjectOptions().name(UPLOAD_OBJECT_NAME2);

      HttpRequest MULTIPART_UPLOAD_REQUEST = HttpRequest.builder().method("POST")
               .endpoint("https://www.googleapis.com/upload/storage/v1/b/jcloudobjectdestination/o")
               .addHeader("Content-Type", "Multipart/related; boundary= " + BOUNDARY_HEADER)
               .addHeader("Content-Length", template.getSize().toString())
               .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse response = HttpResponse.builder().statusCode(204).build();
      ObjectApi api = requestsSendResponses(requestForScopes(STORAGE_READONLY_SCOPE), TOKEN_RESPONSE,
               MULTIPART_UPLOAD_REQUEST, response).getObjectApi();

      assertEquals(api.multipartUpload(DESTINATION_BUCKET_NAME, template, payload), new NoAclBucketTest().expected());
   }

}*/