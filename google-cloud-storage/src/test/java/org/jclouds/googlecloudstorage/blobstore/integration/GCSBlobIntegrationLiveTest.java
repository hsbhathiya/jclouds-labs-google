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
package org.jclouds.googlecloudstorage.blobstore.integration;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.MediaType;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobBuilder.PayloadBlobBuilder;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.integration.internal.BaseBlobIntegrationTest;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.ByteSourcePayload;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

@Test(groups = { "live", "blobstorelive" })
public class GCSBlobIntegrationLiveTest extends BaseBlobIntegrationTest {

   public GCSBlobIntegrationLiveTest() {
      provider = "google-cloud-storage";
      // BaseBlobStoreIntegrationTest.SANITY_CHECK_RETURNED_BUCKET_NAME = true;
   }

   @Override
   @Test(enabled = false)
   public void testGetTwoRanges() {
      // not supported in GCS
   }

   @Override
   @Test(enabled = false)
   public void testGetRange() {
      // not supported in GCS
   }

   @Override
   @Test(enabled = false)
   public void testCreateBlobWithExpiry() throws InterruptedException {
      // not supported in object level.
   }

   @Override
   @Test(enabled = false)
   public void testFileGetParallel() throws Exception {
      // Implement Parallel uploads
   }

   @Override
   @Test(enabled = false)
   public void testPutFileParallel() throws InterruptedException, IOException, TimeoutException {
      // Implement Parallel uploads
   }

   @Override
   @Test(groups = { "integration", "live" }, dataProvider = "gcsPutTest")
   public void testPutObject(String name, String type, Object content, Object realObject) throws InterruptedException,
            IOException {
      PayloadBlobBuilder blobBuilder = view.getBlobStore().blobBuilder(name).payload(Payloads.newPayload(content))
               .contentType(type);
      addContentMetadata(blobBuilder);
      Blob blob = blobBuilder.build();
      blob.getPayload().setContentMetadata(blob.getMetadata().getContentMetadata());
      String container = getContainerName();

      try {
         assertNotNull(view.getBlobStore().putBlob(container, blob));
         blob = view.getBlobStore().getBlob(container, blob.getMetadata().getName());
         validateMetadata(blob.getMetadata(), container, name);
         checkContentMetadata(blob);

         String returnedString = getContentAsStringOrNullAndClose(blob);
         assertEquals(returnedString, realObject);
         PageSet<? extends StorageMetadata> set = view.getBlobStore().list(container);
         assert set.size() == 1 : set;
      } finally {
         returnContainer(container);
      }
   }

   private void addContentMetadata(PayloadBlobBuilder blobBuilder) {
      blobBuilder.contentType("text/csv");
      blobBuilder.contentDisposition("attachment; filename=photo.jpg");
      blobBuilder.contentLanguage("en");
   }

   protected void checkContentMetadata(Blob blob) {
      checkContentType(blob, "text/csv");
      checkContentDisposition(blob, "attachment; filename=photo.jpg");
      checkContentLanguage(blob, "en");
   }

   @DataProvider(name = "gcsPutTest")
   public Object[][] createData1() throws IOException {
      File file = new File("pom.xml");
      String realObject = Files.toString(file, Charsets.UTF_8);

      return new Object[][] { { "file.xml", "text/xml", file, realObject },
               { "string.xml", "text/xml", realObject, realObject },
               { "bytes.xml", "application/octet-stream", realObject.getBytes(), realObject } };
   }

   //Content Length shoul not be null
   @Override
   public void testPutObjectStream() throws InterruptedException, IOException, java.util.concurrent.ExecutionException {

      ByteSource byteSource = ByteSource.wrap("foo".getBytes());
      ByteSourcePayload payload =new ByteSourcePayload(byteSource);      
      PayloadBlobBuilder blobBuilder = view.getBlobStore().blobBuilder("streaming")
               .payload(payload).contentLength(byteSource.read().length);
      addContentMetadata(blobBuilder);

      Blob blob = blobBuilder.build();

      String container = getContainerName();
      try {

         assertNotNull(view.getBlobStore().putBlob(container, blob));

         blob = view.getBlobStore().getBlob(container, blob.getMetadata().getName());
         String returnedString = getContentAsStringOrNullAndClose(blob);
         assertEquals(returnedString, "foo");
         validateMetadata(blob.getMetadata(), container, blob.getMetadata().getName());
         checkContentMetadata(blob);
         PageSet<? extends StorageMetadata> set = view.getBlobStore().list(container);
         assert set.size() == 1 : set;
      } finally {
         returnContainer(container);
      }
   };

   @Override
   public void testMetadata() throws InterruptedException, IOException {
      String name = "hello";

      HashFunction hf = Hashing.md5();
      HashCode md5 = hf.newHasher().putString(TEST_STRING, Charsets.UTF_8).hash();
      Blob blob = view.getBlobStore().blobBuilder(name).userMetadata(ImmutableMap.of("adrian", "powderpuff"))
               .payload(TEST_STRING).contentType(MediaType.TEXT_PLAIN).contentMD5(md5).build();
      String container = getContainerName();
      try {
         assertNull(view.getBlobStore().blobMetadata(container, "powderpuff"));

         addBlobToContainer(container, blob);
         Blob newObject = validateContent(container, name);

         BlobMetadata metadata = newObject.getMetadata();

         validateMetadata(metadata);
         validateMetadata(metadata, container, name);
         validateMetadata(view.getBlobStore().blobMetadata(container, name));

         // write 2 items with the same name to ensure that provider doesn't
         // accept dupes
         blob.getMetadata().getUserMetadata().put("adrian", "wonderpuff");
         blob.getMetadata().getUserMetadata().put("adrian", "powderpuff");

         addBlobToContainer(container, blob);
         validateMetadata(view.getBlobStore().blobMetadata(container, name));

      } finally {
         returnContainer(container);
      }
   }

   @Override
   protected void checkMD5(BlobMetadata metadata) throws IOException {
      HashFunction hf = Hashing.md5();
      HashCode md5 = hf.newHasher().putString(TEST_STRING, Charsets.UTF_8).hash();
      assertEquals(metadata.getContentMetadata().getContentMD5AsHashCode(), md5);
   }

}
