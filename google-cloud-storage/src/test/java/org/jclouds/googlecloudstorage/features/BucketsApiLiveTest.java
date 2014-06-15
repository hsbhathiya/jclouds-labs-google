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

import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.Role;
import org.jclouds.googlecloudstorage.domain.Buckets;
import org.jclouds.googlecloudstorage.domain.Buckets.Location;
import org.jclouds.googlecloudstorage.domain.Buckets.StorageClass;
import org.jclouds.googlecloudstorage.domain.BucketsTemplate;
import org.jclouds.googlecloudstorage.domain.BucketsTemplate.Cors;
import org.jclouds.googlecloudstorage.domain.BucketsTemplate.Logging;
import org.jclouds.googlecloudstorage.domain.BucketsTemplate.Versioning;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author Bhathiya Supun
 */
public class BucketsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   protected static final String BUCKET_NAME = "jcloudtestbucket" + (int) (Math.random() * 10000);

   private BucketsApi api() {
      return api.getBucketsApi();
   }

   @Test(groups = "live")
   public void testCreateBucket() {

      BucketAccessControls acl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers").role(Role.OWNER)
               .build();
      ObjectAccessControls oac = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(org.jclouds.googlecloudstorage.domain.ObjectAccessControls.Role.OWNER).build();
      Cors cors = Cors.builder().addOrigin("http://example.appspot.com").addMethod("GET").addMethod("HEAD")
               .addResponseHeader("x-meta-goog-custom").maxAgeSeconds(10).build();
      Versioning version = Versioning.builder().enalbled(true).build();
      Logging log = new Logging("bhashbucket", BUCKET_NAME);

      BucketsTemplate template = new BucketsTemplate().name(BUCKET_NAME).addAcl(acl)
               .addDefaultObjectAccessControls(oac).versioning(version).location(Location.US_CENTRAL2).logging(log)
               .storageClass(StorageClass.DURABLE_REDUCED_AVAILABILITY).addCORS(cors);

      Buckets response = api().createBuckets(PROJECT_NUMBER, template);

      assertNotNull(response);
      assertNotNull(response.getCors());
      assertEquals(response.getKind(), Kind.bucket);
      assertEquals(response.getName(), BUCKET_NAME);
      assertEquals(response.getLocation(), Location.US_CENTRAL2);
      assertTrue(response.getVersioning().isEnabled());
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucket")
   public void testUpdateBucket() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allAuthenticatedUsers")
               .role(Role.OWNER).build();
      BucketsTemplate template = new BucketsTemplate().name(BUCKET_NAME).addAcl(bucketacl);
      Buckets response = api().updateBuckets(BUCKET_NAME, template);

      assertNotNull(response);
      assertEquals(response.getName(), BUCKET_NAME);
      assertNotNull(response.getAcl());
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucket")
   public void testGetBucket() {
      Buckets response = api().getBuckets(BUCKET_NAME);

      assertNotNull(response);
      assertEquals(response.getName(), BUCKET_NAME);
      assertEquals(response.getKind(), Kind.bucket);

   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucket")
   public void testListBucket() {
      ListPage<Buckets> buckets = api().listBuckets(PROJECT_NUMBER);

      Iterator<Buckets> pageIterator = buckets.iterator();
      assertTrue(pageIterator.hasNext());

      Buckets singlePageIterator = pageIterator.next();
      List<Buckets> bucketAsList = Lists.newArrayList(singlePageIterator);

      assertNotNull(singlePageIterator);
      assertSame(bucketAsList.size(), 1);

   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucket")
   public void testPatchBucket() {
      Logging logging = new Logging("bhashbuck", BUCKET_NAME);
      BucketsTemplate template = new BucketsTemplate().name(BUCKET_NAME).logging(logging);

      Buckets response = api().patchBuckets(BUCKET_NAME, template);

      assertNotNull(response);
      assertEquals(response.getName(), BUCKET_NAME);
      assertEquals(response.getLogging().getLogBucket(), "bhashbuck");
   }

   @Test(groups = "live", dependsOnMethods = { "testListBucket", "testGetBucket", "testUpdateBucket" })
   public void testDeleteBucket() {
      HttpResponse response = api().deleteBuckets(BUCKET_NAME);

      assertNotNull(response);
      assertEquals(response.getStatusCode(), 204);
   }

}
