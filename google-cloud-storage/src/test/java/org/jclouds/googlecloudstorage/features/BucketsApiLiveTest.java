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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.PagedIterable;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.Role;
import org.jclouds.googlecloudstorage.domain.Buckets;
import org.jclouds.googlecloudstorage.domain.Buckets.StorageClass;
import org.jclouds.googlecloudstorage.domain.ListBucketAccessControls;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.spi.LogbackLock;

/**
 * @author Bhathiya Supun
 */
public class BucketsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   private BucketsApi api() {
      return api.getBucketsApi();
   }

  /* @Test(groups = "live")
   public void testCreateBucketacl() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(Role.READER).build();
      BucketAccessControls response = api().createBucketAccessControls(BUCKET_NAME, bucketacl);

      assertNotNull(response);
      assertEquals(response.getId(), BUCKET_NAME + "/allUsers");
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucketacl")
   public void testUpdateBucketacl() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(Role.WRITER).build();
      BucketAccessControls response = api().updateBucketAccessControls(BUCKET_NAME, "allUsers", bucketacl);

      assertNotNull(response);
      assertEquals(response.getId(), BUCKET_NAME + "/allUsers");
      assertEquals(response.getRole(), Role.WRITER);
   }*/

   @Test(groups = "live")
   public void testGetBucket() {
      Buckets response = api().getBuckets("jcloudtestbucket");

      assertNotNull(response);
      assertEquals(response.getName(), "jcloudtestbucket" );
      assertEquals(response.getKind(), Kind.bucket );
      assertEquals(response.getStorageClass(), StorageClass.STANDARD );
     // assertEquals(response.getOwner().getEntity(),"project-owners-1082289308625" );
       assertEquals(response.getMetageneration(), Long.valueOf(9) );
     //assertEquals(response.getProjectNumber(),Long.valueOf("1082289308625"));
      
   }

   @Test(groups = "live")
   public void testListBucketacl() {
      ListPage<Buckets> buckets = api().listBuckets("1082289308625");
      
      Iterator<Buckets> pageIterator = buckets.iterator();
      assertTrue(pageIterator.hasNext());

      Buckets singlePageIterator = pageIterator.next();
      List<Buckets> bucketAsList = Lists.newArrayList(singlePageIterator);

      assertNotNull(singlePageIterator);
      assertSame(bucketAsList.size(), 1);

     
   }

 /*@Test(groups = "live", dependsOnMethods = "testUpdateBucketacl")
   public void testPatchBucketacl() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(Role.READER).build();
      BucketAccessControls response = api().patchBucketAccessControls(BUCKET_NAME, "allUsers", bucketacl);

      assertNotNull(response);
      assertEquals(response.getId(), BUCKET_NAME + "/allUsers");
      assertEquals(response.getRole(), Role.READER);
   }
   
   @Test(groups = "live", dependsOnMethods = "testPatchBucketacl")
   public void testDeleteBucketacl() {

      HttpResponse response = api().deleteBucketAccessControls(BUCKET_NAME, "allUsers");

      assertNotNull(response);
      assertEquals(response.getStatusCode(), 204);
   }*/
}
