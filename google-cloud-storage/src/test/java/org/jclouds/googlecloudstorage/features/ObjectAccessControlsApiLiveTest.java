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

import org.jclouds.googlecloudstorage.domain.Bucket;
import org.jclouds.googlecloudstorage.domain.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.ListObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControlsTemplate;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

public class ObjectAccessControlsApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   protected static final String BUCKET_NAME = "jcloudtestbucketoacl"; // + (int) (Math.random() * 10000);
   protected static final String OBJECT_NAME = "jcloudtestdoc.txt";

   private ObjectAccessControlsApi api() {
      return api.getObjectAccessControlsApi();
   }

   private void createBucket(String BucketName) {

      BucketTemplate template =  new  BucketTemplate().name(BucketName);
      Bucket response = api.getBucketApi().createBucket(PROJECT_NUMBER, template);
      assertNotNull(response);
   }

   private void uploadObject(String ObjectName) {
      /** Upload foo.txt */
   }

   @Test(groups = "live")
   public void testCreateObjectacl() {

    //  createBucket(BUCKET_NAME);
      uploadObject(OBJECT_NAME);
      ObjectAccessControlsTemplate template = new ObjectAccessControlsTemplate().entity("allUsers").role(ObjectRole.READER);
      
      ObjectAccessControls response = api().createObjectAccessControls(BUCKET_NAME, OBJECT_NAME, template);

      assertNotNull(response);
      assertEquals(response.getBucket(), BUCKET_NAME);
      assertEquals(response.getObject(), OBJECT_NAME);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(), ObjectRole.READER);
      
      
   }

   @Test(groups = "live", dependsOnMethods = "testCreateObjectacl")
   public void testUpdateObjectacl() {
      ObjectAccessControls objectacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).object(OBJECT_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();
      ObjectAccessControls response = api().updateObjectAccessControls(BUCKET_NAME, OBJECT_NAME, "allUsers", objectacl);

      assertNotNull(response);
      assertEquals(response.getBucket(), BUCKET_NAME);
      assertEquals(response.getObject(), OBJECT_NAME);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(),ObjectRole.OWNER);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateObjectacl")
   public void testGetBucketacl() {
      ObjectAccessControls response = api().getObjectAccessControls(BUCKET_NAME,OBJECT_NAME,"allUsers");

      assertNotNull(response);
      assertEquals(response.getBucket(), BUCKET_NAME);
      assertEquals(response.getObject(), OBJECT_NAME);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(),ObjectRole.OWNER);
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateObjectacl")
   public void testListObjectacl() {
      ListObjectAccessControls response = api().listObjectAccessControls(BUCKET_NAME,OBJECT_NAME);

      assertNotNull(response);
      assertEquals(response.getKind(), Kind.BUCKET_ACCESS_CONTROLS);
      assertNotNull(response.getItems());
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateObjectacl")
   public void testPatchObjectacl() {
      ObjectAccessControls objectacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).object(OBJECT_NAME).entity("allUsers")
               .role(ObjectRole.READER).build();
      ObjectAccessControls response = api().patchObjectAccessControls(BUCKET_NAME, OBJECT_NAME ,"allUsers", objectacl);

      assertNotNull(response);
      assertEquals(response.getBucket(), BUCKET_NAME);
      assertEquals(response.getObject(), OBJECT_NAME);
      assertEquals(response.getEntity(), "allUsers");
      assertEquals(response.getRole(),ObjectRole.READER);
   }

   @Test(groups = "live", dependsOnMethods = "testPatchObjectacl")
   public void testDeleteObjectacl() {

      HttpResponse response = api().deleteObjectAccessControls(BUCKET_NAME,OBJECT_NAME, "allUsers");
      
      assertNotNull(response);
      assertEquals(response.getStatusCode(), 204);
      
     // deleteBucket(BUCKET_NAME);
       deleteObject(OBJECT_NAME); 
      
   }

   private void deleteObject(String objectName) {
     //Delete the uploaded object
   }
   private void deleteBucket(String bucketName) {
      api.getBucketApi().deleteBucket(bucketName);
   }
   
}
