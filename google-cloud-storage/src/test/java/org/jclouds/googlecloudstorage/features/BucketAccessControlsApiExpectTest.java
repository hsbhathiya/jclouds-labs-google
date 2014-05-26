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
 * 
 */

/*
 *delete
 *list
 *update(put) 
 *patch
 * */
package org.jclouds.googlecloudstorage.features;

import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_FULLCONTROL_SCOPE;
import static java.net.URI.create;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.Role;
import org.jclouds.googlecloudstorage.domain.ListBucketAccessControls;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiExpectTest;
import org.jclouds.googlecloudstorage.parse.BucketaclGetTest;
import org.jclouds.googlecloudstorage.parse.BucketaclInsertTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/**
 * @author Bhathiya Supun
 */
@Test(groups = "unit")
public class BucketAccessControlsApiExpectTest extends BaseGoogleCloudStorageApiExpectTest {

   public static final HttpRequest GET_BUCKETACL_REQUEST = HttpRequest.builder().method("GET")
         .endpoint("https://www.googleapis.com/storage/v1/b/jcloudtestbucket/acl/allUsers")
         .addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + TOKEN)
         .build();

   public static final HttpResponse GET_BUCKETACL_RESPONSE = HttpResponse.builder().statusCode(200)
         .payload(staticPayloadFromResource("/bucketacl_get.json")).build();

   public static final HttpRequest LIST_BUCKETACL_REQUEST = HttpRequest.builder().method("GET")
         .endpoint("https://www.googleapis.com/storage/v1/b/jcloudtestbucket/acl")
         .addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + TOKEN)
         .build();
   public static final HttpResponse CREATE_BUCKETACL_RESPONSE = HttpResponse.builder()
         .statusCode(200).payload(staticPayloadFromResource("/bucketacl_insert_response")).build();

   public static final HttpResponse LIST_BUCKETACL_RESPONSE = HttpResponse.builder()
         .statusCode(200).payload(staticPayloadFromResource("/bucketacl_list.json")).build();

   public static final HttpResponse UPDATE_BUCKETACL_RESPONSE = HttpResponse.builder()
         .statusCode(200).payload(staticPayloadFromResource("/bucketacl_update_response")).build();

   public void testGetBucketaclResponseIs2xx() throws Exception {

      BucketAccessControlsApi api = requestsSendResponses(requestForScopes(), TOKEN_RESPONSE,
            GET_BUCKETACL_REQUEST, GET_BUCKETACL_RESPONSE).getBucketAccessControlsApi();

      assertEquals(api.getBucketAccessControls("jcloudtestbucket", "allUSers"),
            new BucketaclGetTest().expected());
   }

   public void testGetBucketaclResponseIs4xx() throws Exception {

      HttpResponse operationResponse = HttpResponse.builder().statusCode(404).build();

      BucketAccessControlsApi api = requestsSendResponses(requestForScopes(), TOKEN_RESPONSE,
            GET_BUCKETACL_REQUEST, operationResponse).getBucketAccessControlsApi();

      assertNull(api.getBucketAccessControls("jcloudbuckettest", "allUSers"));
   }

   public void testInsertBucketaclResponseIs2xx() {
      HttpRequest insert = HttpRequest
            .builder()
            .method("POST")
            .endpoint("https://www.googleapis.com/storage/v1/b/jcloudtestbucket/acl")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + TOKEN)
            .payload(
                  payloadFromResourceWithContentType("/bucketacl_insert_requestpayload.json",
                        MediaType.APPLICATION_JSON)).build();

      BucketAccessControlsApi api = requestsSendResponses(
            ImmutableMap.of(requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE,
                  GET_BUCKETACL_REQUEST, GET_BUCKETACL_RESPONSE,
                  requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE, insert,
                  CREATE_BUCKETACL_RESPONSE)).getBucketAccessControlsApi();

      BucketAccessControls options = BucketAccessControls.builder().bucket("jcloudbuckettest")
            .entity("allAuthenticatedUsers").role(Role.WRITER).build();

      assertEquals(
            api.createBucketAccessControls("allAuthenticatedUsers", options, "jcloudbuckettest",
                  Role.WRITER), new BucketaclInsertTest().expected());
            
   }

   public void testDeleteInstanceResponseIs2xx() {
      HttpRequest delete = HttpRequest
            .builder()
            .method("DELETE")
            .endpoint(
                  "https://www.googleapis.com/storage/v1/b/jcloudtestbucket/acl/allAuthenticatedUsers")
            .addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + TOKEN)
            .build();

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(204).build();

      BucketAccessControlsApi api = requestsSendResponses(
            requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE, delete, deleteResponse)
            .getBucketAccessControlsApi();

      assertEquals(api.deleteBucketAccessControls("jcloudtestbucket", "allAuthenticatedUsers"),
            deleteResponse);
      /* deleteResponse? */
   }

   public void testDeleteInstanceResponseIs4xx() {
      HttpRequest delete = HttpRequest
            .builder()
            .method("DELETE")
            .endpoint(
                  "https://www.googleapis.com/storage/v1/b/jcloudtestbucket/acl/allAuthenticatedUsers")
            .addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + TOKEN)
            .build();

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(404).build();

      BucketAccessControlsApi api = requestsSendResponses(
            requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE, delete, deleteResponse)
            .getBucketAccessControlsApi();

      assertNull(api.deleteBucketAccessControls("jcloudtestbucket", "allAuthenticatedUsers"));
   }

   public void testListInstancesResponseIs2xx() {

      BucketAccessControlsApi api = requestsSendResponses(
            requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE, LIST_BUCKETACL_REQUEST,
            LIST_BUCKETACL_RESPONSE).getBucketAccessControlsApi();

      assertEquals(api.listBucketAccessControls("jcloudtestbucket"), ListBucketAccessControls
            .builder().build());
      // Build a listbucket with output/bucketacl_list.json
   }

   public void testListInstancesResponseIs4xx() {
      HttpRequest list = HttpRequest.builder().method("GET")
            .endpoint("https://www.googleapis.com/storage/v1/b/jcloudtestbucket45667/acl")
            .addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + TOKEN)
            .build();

      HttpResponse listResponse = HttpResponse.builder().statusCode(404).build();

      BucketAccessControlsApi api = requestsSendResponses(
            requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE, list, listResponse)
            .getBucketAccessControlsApi();

      assertNull(api.listBucketAccessControls("jcloudtestbucket45667"));

   }

   public void testUpdateBucketaclResponseIs2xx() {
      HttpRequest update = HttpRequest
            .builder()
            .method("PUT")
            .endpoint("https://www.googleapis.com/storage/v1/b/jcloudtestbbucket/acl/allUsers")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + TOKEN)
            .payload(
                  payloadFromResourceWithContentType("/bucketacl_update_requestpayload.json",
                        MediaType.APPLICATION_JSON)).build();

      BucketAccessControlsApi api = requestsSendResponses(
            ImmutableMap.of(requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE,
                  GET_BUCKETACL_REQUEST, GET_BUCKETACL_RESPONSE,
                  requestForScopes(STORAGE_FULLCONTROL_SCOPE), TOKEN_RESPONSE, update,
                  UPDATE_BUCKETACL_RESPONSE)).getBucketAccessControlsApi();

      BucketAccessControls options = BucketAccessControls.builder().bucket("jcloudbuckettest")
            .entity("allUsers").role(Role.OWNER).build();

      assertEquals(
            api.updateBucketAccessControls("jcloudbuckettest", "allUsers", options),
            BucketAccessControls
                  .builder()
                  .id("jcloudtestbucket/allUsers")
                  .selfLink(
                        URI.create("https://content.googleapis.com/storage/v1/b/jcloudtestbucket/acl/allUsers"))
                  .bucket("jcloudbuckettest").entity("allUsers").role(Role.OWNER)
                  .etag("CAM=").build());

      // EDIT ASSERT ACCORDING TO RESPOSNE
      // SAVE RESOURCE PAYLOAD&& RESPONSE
   }

}
