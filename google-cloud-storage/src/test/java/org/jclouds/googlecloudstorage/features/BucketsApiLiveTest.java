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
import org.jclouds.googlecloudstorage.domain.DefaultObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.features.ApiResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.features.ApiResourceRefferences.Projection;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.googlecloudstorage.options.DeleteBucketsOptions;
import org.jclouds.googlecloudstorage.options.GetBucketsOptions;
import org.jclouds.googlecloudstorage.options.UpdateBucketsOptions;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class BucketsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   private static final String BUCKET_NAME = "jcloudtestbucket" + (int) (Math.random() * 10000);

   private static final String BUCKET_NAME_WITHOPTIONS = "jcloudtestbucketoptions" + (int) (Math.random() * 10000);

   private Long metageneration;

   private BucketsApi api() {
      return api.getBucketsApi();
   }

   @Test(groups = "live")
   public void testCreateBucket() {

      BucketAccessControls acl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers").role(Role.OWNER)
               .build();
      DefaultObjectAccessControls oac = DefaultObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();
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
      assertEquals(response.getKind(), Kind.BUCKET);
      assertEquals(response.getName(), BUCKET_NAME);
      assertEquals(response.getLocation(), Location.US_CENTRAL2);
      assertTrue(response.getVersioning().isEnabled());
   }

   @Test(groups = "live")
   public void testCreateBucketWithOptions() {
      // acl searching? implement a interface/method
      DefaultObjectAccessControls oac = DefaultObjectAccessControls.builder().bucket(BUCKET_NAME_WITHOPTIONS)
               .entity("allUsers").role(ObjectRole.OWNER).build();
      Cors cors = Cors.builder().addOrigin("http://example.appspot.com").addMethod("GET").addMethod("HEAD")
               .addResponseHeader("x-meta-goog-custom").maxAgeSeconds(10).build();
      Versioning version = Versioning.builder().enalbled(true).build();
      Logging log = new Logging("bhashbucket", BUCKET_NAME_WITHOPTIONS);

      BucketsTemplate template = new BucketsTemplate().name(BUCKET_NAME_WITHOPTIONS)
               .addDefaultObjectAccessControls(oac).versioning(version).location(Location.US_CENTRAL2).logging(log)
               .storageClass(StorageClass.DURABLE_REDUCED_AVAILABILITY).addCORS(cors);

      // InsertBucketsOptions options = new
      // InsertBucketsOptions().predefinedAcl(PredefinedAcl.PUBLIC_READ_WRITE).projection(Projection.FULL);

      Buckets response = api().createBuckets(PROJECT_NUMBER, template);

      assertNotNull(response);
      assertNotNull(response.getCors());
      // assertNotNull(response.getAcl());
      assertEquals(response.getKind(), Kind.BUCKET);
      assertEquals(response.getName(), BUCKET_NAME_WITHOPTIONS);
      assertEquals(response.getLocation(), Location.US_CENTRAL2);
      assertTrue(response.getVersioning().isEnabled());
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucket")
   public void testUpdateBucket() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME)
               .entity("allAuthenticatedUsers").role(Role.OWNER).build();
      BucketsTemplate template = new BucketsTemplate().name(BUCKET_NAME).addAcl(bucketacl);
      Buckets response = api().updateBuckets(BUCKET_NAME, template);

      assertNotNull(response);
      assertEquals(response.getName(), BUCKET_NAME);
      assertNotNull(response.getAcl());
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucketWithOptions")
   public void testUpdateBucketWithOptions() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME_WITHOPTIONS)
               .entity("allAuthenticatedUsers").role(Role.OWNER).build();
      UpdateBucketsOptions options = new UpdateBucketsOptions().projection(Projection.FULL);
      BucketsTemplate template = new BucketsTemplate().name(BUCKET_NAME_WITHOPTIONS).addAcl(bucketacl);
      Buckets response = api().updateBuckets(BUCKET_NAME_WITHOPTIONS, template, options);

      assertNotNull(response);

      metageneration = response.getMetageneration();

      assertEquals(response.getName(), BUCKET_NAME_WITHOPTIONS);
      assertNotNull(response.getAcl());
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucket")
   public void testGetBucket() {
      Buckets response = api().getBuckets(BUCKET_NAME);

      assertNotNull(response);
      assertEquals(response.getName(), BUCKET_NAME);
      assertEquals(response.getKind(), Kind.BUCKET);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateBucketWithOptions")
   public void testGetBucketWithOptions() {
      GetBucketsOptions options = new GetBucketsOptions().ifMetagenerationMatch(metageneration);
      Buckets response = api().getBuckets(BUCKET_NAME_WITHOPTIONS, options);

      assertNotNull(response);
      assertEquals(response.getName(), BUCKET_NAME_WITHOPTIONS);
      assertEquals(response.getKind(), Kind.BUCKET);
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

   @Test(groups = "live", dependsOnMethods = { "testGetBucketWithOptions" })
   public void testDeleteBucketWithOptions() {

      DeleteBucketsOptions options = new DeleteBucketsOptions().ifMetagenerationMatch(metageneration)
               .ifMetagenerationNotMatch(metageneration + 1);

      HttpResponse response = api().deleteBuckets(BUCKET_NAME_WITHOPTIONS, options);

      assertNotNull(response);
      assertEquals(response.getStatusCode(), 204);
   }

}
