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
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.jclouds.collect.PagedIterable;
import org.jclouds.googlecloudstorage.GoogleCloudStorageClient;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.Role;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Module;

/**
 * @author Bhathiya Supun
 */
public class BucketAccessControlsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {
   
   //This should be replaced by the bucket created in  "Bucket insert" operation when it is   implemented
   private static final String BUCKET_NAME = "jcloudtestbucket2";

   private BucketAccessControlsApi api() {
      return api.getBucketAccessControlsApi();
   }


   /*@Test(groups = "live")
   public void testCreateBucketacl() {
      BucketAccessControls bucketacl = BucketAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers").role(Role.OWNER).build();
      BucketAccessControls response = api().createBucketAccessControls("allUsers", bucketacl,BUCKET_NAME , Role.OWNER);
     
      assertNotNull(response);
      assertEquals(response.getId(), BUCKET_NAME + "/allUsers" );

   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucketacl")
   public void testGetBucketacl() {
      
   }

   @Test(groups = "live", dependsOnMethods = "testCreateBucketacl")
   public void testListBucketacl() {
   }



   private void assertInstanceEquals(BucketAccessControls result, BucketAccessControls expected) {
      assertEquals(result.getName(), expected.getName());      
   }

   @AfterClass(groups = { "integration", "live" })
   protected void tearDownContext() {
      
    }*/

}
