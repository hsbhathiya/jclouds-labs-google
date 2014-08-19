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

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Throwables.propagateIfPossible;
import static com.google.common.collect.Iterables.get;
import static com.google.common.hash.Hashing.md5;
import static org.jclouds.blobstore.options.ListContainerOptions.Builder.afterMarker;
import static org.jclouds.blobstore.options.ListContainerOptions.Builder.inDirectory;
import static org.jclouds.blobstore.options.ListContainerOptions.Builder.maxResults;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.integration.internal.BaseContainerIntegrationTest;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.googlecloudstorage.blobstore.GCSBlobStore;
import org.testng.annotations.Test;

import clojure.lang.Compiler.C;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;

@Test(groups = { "live" })
public class GCSContainerIntegrationTest extends BaseContainerIntegrationTest {

   public GCSContainerIntegrationTest() {
      provider = "google-cloud-storage";
   }

   @Override
   @Test(groups = { "integration", "live" })
   public void testWithDetails() throws InterruptedException, IOException {
      String key = "hello";
      String containerName = getContainerName();
      try {
         addBlobToContainer(containerName,
         // NOTE all metadata in jclouds comes out as lowercase, in an effort to
         // normalize the providers.
                  view.getBlobStore().blobBuilder(key).userMetadata(ImmutableMap.of("adrian", "powderpuff"))
                           .payload(TEST_STRING).contentType(MediaType.TEXT_PLAIN)
                           .contentMD5(Hashing.md5().newHasher().putString(TEST_STRING, Charsets.UTF_8).hash()).build());
         validateContent(containerName, key);

         PageSet<? extends StorageMetadata> container = view.getBlobStore().list(containerName,
                  maxResults(1).withDetails());

         BlobMetadata metadata = BlobMetadata.class.cast(get(container, 0));

         assert metadata.getContentMetadata().getContentType().startsWith("text/plain") : metadata.getContentMetadata()
                  .getContentType();
         assertEquals(metadata.getContentMetadata().getContentLength(), Long.valueOf(TEST_STRING.length()));
         assertEquals(metadata.getUserMetadata().get("adrian"), "powderpuff");
         checkMD5(metadata);
      } finally {
         returnContainer(containerName);
      }
   }

   /* Google Cloud Storage lists prefixes and objects in two different lists */
   @Override
   public void testListRootUsesDelimiter() throws InterruptedException {
      String containerName = getContainerName();
      try {
         String prefix = "rootdelimiter";
         addTenObjectsUnderPrefix(containerName, prefix);
         add15UnderRoot(containerName);
         PageSet<? extends StorageMetadata> container = view.getBlobStore().list(containerName,
                  new ListContainerOptions());
         assert container.getNextMarker() == null;
         assertEquals(container.size(), 15);
      } finally {
         returnContainer(containerName);
      }
   }

   @Override
   @Test(enabled = false)
   public void testDirectory() throws InterruptedException {
      /**
       * By using slashes in an object name, you can make objects appear as though they're stored in a hierarchical
       * structure. For example, you could name one object /europe/france/paris.jpg and another object
       * /europe/france/cannes.jpg. When you list these objects they appear to be in a hierarchical directory structure
       * based on location; however, Google Cloud Storage sees the objects as independent objects with no hierarchical
       * relationship whatsoever.
       */
   }

}
