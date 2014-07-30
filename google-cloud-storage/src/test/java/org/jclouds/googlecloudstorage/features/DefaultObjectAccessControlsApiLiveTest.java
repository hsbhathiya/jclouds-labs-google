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

import java.util.UUID;

import org.jclouds.googlecloudstorage.domain.Bucket;
import org.jclouds.googlecloudstorage.domain.DefaultObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.ListDefaultObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.domain.templates.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.templates.DefaultObjectAccessControlsTemplate;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.rest.ResourceNotFoundException;
import org.testng.annotations.Test;

public class DefaultObjectAccessControlsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   protected static final String BUCKET_NAME = "jcloudtestbucketdefaultoacl" + UUID.randomUUID();

   private DefaultObjectAccessControlsApi api() {
      return api.getDefaultObjectAccessControlsApi();
   }

   private void createBucket(String BucketName) {
      BucketTemplate template = new BucketTemplate().name(BucketName);
      Bucket response = api.getBucketApi().createBucket(PROJECT_NUMBER, template);
      assertNotNull(response);
   }

   @Test(groups = "live")
   public void testCreateDefaultObjectacl() {
      createBucket(BUCKET_NAME);
      DefaultObjectAccessControlsTemplate template = new DefaultObjectAccessControlsTemplate().entity("allUsers").role(
               ObjectRole.READER);

      DefaultObjectAccessControls response = api().createDefaultObjectAccessControls(BUCKET_NAME, template);

      assertNotNull(response);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(), ObjectRole.READER);
   }

   @Test(groups = "live", dependsOnMethods = "testCreateDefaultObjectacl")
   public void testUpdateDefaultObjectacl() {
      DefaultObjectAccessControls defaultObjectacl = DefaultObjectAccessControls.builder().bucket(BUCKET_NAME)
               .entity("allUsers").role(ObjectRole.OWNER).build();
      DefaultObjectAccessControls response = api().updateDefaultObjectAccessControls(BUCKET_NAME, "allUsers",
               defaultObjectacl);

      assertNotNull(response);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(), ObjectRole.OWNER);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateDefaultObjectacl")
   public void testGetDefaultObjectacl() {
      DefaultObjectAccessControls response = api().getDefaultObjectAccessControls(BUCKET_NAME, "allUsers");

      assertNotNull(response);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(), ObjectRole.OWNER);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateDefaultObjectacl")
   public void testListDefaultObjectacl() {
      ListDefaultObjectAccessControls response = api().listDefaultObjectAccessControls(BUCKET_NAME);

      assertNotNull(response);
      assertEquals(response.getKind(), Kind.OBJECT_ACCESS_CONTROLS);
      assertNotNull(response.getItems());
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateDefaultObjectacl")
   public void testPatchDefaultObjectacl() {
      DefaultObjectAccessControls defaultObjectacl = DefaultObjectAccessControls.builder().bucket(BUCKET_NAME)
               .entity("allUsers").role(ObjectRole.READER).build();
      DefaultObjectAccessControls response = api().patchDefaultObjectAccessControls(BUCKET_NAME, "allUsers",
               defaultObjectacl);

      assertNotNull(response);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(), ObjectRole.READER);
   }

   @Test(groups = "live", dependsOnMethods = "testPatchDefaultObjectacl")
   public void testDeleteBucketacl() {
      api().deleteDefaultObjectAccessControls(BUCKET_NAME, "allUsers");
      deleteBucket(BUCKET_NAME);
   }
   
   @Test(groups = "live", dependsOnMethods = { "testDeleteBucketacl" }, expectedExceptions = { ResourceNotFoundException.class })
   public void testDeleteNotExistingBucketAccessControls() {
      api().deleteDefaultObjectAccessControls(BUCKET_NAME, "allUsers");
   }

   private void deleteBucket(String bucketName) {
      api.getBucketApi().deleteBucket(bucketName);
   }
}
