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

import org.jclouds.googlecloudstorage.domain.Buckets;
import org.jclouds.googlecloudstorage.domain.BucketsTemplate;
import org.jclouds.googlecloudstorage.domain.DefaultObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ListDefaultObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ListObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControlsTemplate;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls.Role;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

/**
 * @author Bhathiya Supun
 */

public class DefaultObjectAccessControlsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

 protected static final String BUCKET_NAME = "jcloudtestbucketdefaultoacl" + (int) (Math.random() * 10000);
   

   private DefaultObjectAccessControlsApi api() {
      return api.getDefaultObjectAccessControlsApi();
   }

   private void createBucket(String BucketName) {

      BucketsTemplate template = new BucketsTemplate().name(BucketName);
      Buckets response = api.getBucketsApi().createBuckets(PROJECT_NUMBER, template);
      assertNotNull(response);
   }

  @Test(groups = "live")
   public void testCreateDefaultObjectacl() {

      createBucket(BUCKET_NAME);      
      ObjectAccessControlsTemplate template = new ObjectAccessControlsTemplate().entity("allUsers").role(Role.READER);
      
      DefaultObjectAccessControls response = api().createDefaultObjectAccessControls(BUCKET_NAME, template);

      assertNotNull(response);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(), Role.READER);      
      
   }

   @Test(groups = "live", dependsOnMethods = "testCreateDefaultObjectacl")
   public void testUpdateDefaultObjectacl() {
      DefaultObjectAccessControls defaultObjectacl = DefaultObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(Role.OWNER).build();
      DefaultObjectAccessControls response = api().updateDefaultObjectAccessControls(BUCKET_NAME,  "allUsers", defaultObjectacl);

      assertNotNull(response);      
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(),Role.OWNER);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateDefaultObjectacl")
   public void testGetDefaultObjectacl() {
      DefaultObjectAccessControls response = api().getDefaultObjectAccessControls(BUCKET_NAME,"allUsers");

      assertNotNull(response);
      //assertEquals(response.getBucket(), BUCKET_NAME);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(),Role.OWNER);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateDefaultObjectacl")
   public void testListDefaultObjectacl() {
      ListDefaultObjectAccessControls response = api().listDefaultObjectAccessControls(BUCKET_NAME);

      assertNotNull(response);
      assertEquals(response.getKind(), Kind.objectAccessControls);
      assertNotNull(response.getItems());
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateDefaultObjectacl")
   public void testPatchDefaultObjectacl() {
      DefaultObjectAccessControls defaultObjectacl = DefaultObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(Role.READER).build();
      DefaultObjectAccessControls response = api().patchObjectAccessControls(BUCKET_NAME, "allUsers", defaultObjectacl);

      assertNotNull(response);
      //assertEquals(response.getBucket(), BUCKET_NAME);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(),Role.READER);
   }

   @Test(groups = "live", dependsOnMethods = "testPatchDefaultObjectacl")
   public void testDeleteBucketacl() {

      HttpResponse response = api().deleteDefaultObjectAccessControls(BUCKET_NAME,"allUsers");
      
      assertNotNull(response);
      assertEquals(response.getStatusCode(), 204);
      
      deleteBucket(BUCKET_NAME);       
      
   }


   private void deleteBucket(String bucketName) {
      api.getBucketsApi().deleteBuckets(bucketName);
   }
   
}
