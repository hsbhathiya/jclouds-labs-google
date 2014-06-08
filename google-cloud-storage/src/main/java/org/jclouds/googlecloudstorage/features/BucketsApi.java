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

import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_FULLCONTROL_SCOPE;
import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_READONLY_SCOPE;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.googlecloudstorage.domain.Buckets;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.handlers.BucketsBinder;
import org.jclouds.googlecloudstorage.options.DeleteBucketsOptions;
import org.jclouds.googlecloudstorage.options.GetBucketsOptions;
import org.jclouds.googlecloudstorage.options.InsertBucketsOptions;
import org.jclouds.googlecloudstorage.options.ListOptions;
import org.jclouds.googlecloudstorage.options.UpdateBucketsOptions;
import org.jclouds.http.HttpResponse;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.oauth.v2.config.OAuthScopes;
import org.jclouds.oauth.v2.filters.OAuthAuthenticator;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PATCH;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SkipEncoding;
import org.jclouds.rest.binders.BindToJsonPayload;

/**
 * Provides access to Bucket entities via their REST API.
 * 
 * @author Bhathiya Supun
 * @see <a href = " https://developers.google.com/storage/docs/json_api/v1/buckets"/>
 * 
 */

@SkipEncoding({ '/', '=' })
@RequestFilters(OAuthAuthenticator.class)
public interface BucketsApi {

   /**
    * Returns metadata for the specified bucket.
    * 
    * @param bucketName
    *           Name of a bucket
    * 
    * @return an Buckets resource
    * 
    */

   @Named("Buckets:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_READONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   Buckets getBuckets(@PathParam("bucket") String bucketName);

   /**
    * Returns metadata for the specified bucket.
    * 
    * @param bucketName
    *           Name of the bucket
    * @param options
    *           Supply {@link GetBucketsOptions} with optional query parameters
    * 
    * @return an Buckets resource
    * 
    */

   @Named("Buckets:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_READONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   Buckets getBuckets(@PathParam("bucket") String bucketName, GetBucketsOptions options);

   /**
    * Creates a new bucket
    * 
    * @param projectId
    *           A valid API project identifier
    * 
    * @param bucket
    *           In the request body, supply a bucket resource
    * 
    * @return If successful, this method returns a Buckets resource in the response body
    */

   @Named("Buckets:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/b")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(BucketsBinder.class)
   Buckets createBuckets(@QueryParam("project") String projectId, @PayloadParam("bucket") Buckets bucket);

   /**
    * Creates a new bucket
    * 
    * @param projectNumber
    *           A valid API project identifier.
    * 
    * @param bucket
    *           In the request body, supply a bucket resource
    * 
    * @param options
    *           Supply {@link InsertBucketsOptions} with optional query parameters
    * 
    * @return If successful, this method returns a Buckets resource in the response body
    */

   @Named("Buckets:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/b")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(BucketsBinder.class)
   Buckets createBuckets(@QueryParam("project") String projectNumber, @PayloadParam("bucket") Buckets bucket,
            InsertBucketsOptions options);

   /**
    * Permanently deletes an empty bucket
    * 
    * @param bucketName
    *           Name of the bucket
    * 
    * @return If successful, this method returns an empty response body.
    */

   @Named("Buckets:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   HttpResponse deleteBuckets(@PathParam("bucket") String bucketName);

   /**
    * Permanently deletes an empty bucket
    * 
    * @param bucketName
    *           Name of the bucket
    * @param options
    *           Supply {@link DeleteBucketsOptions} with optional query parameters
    * 
    * @return If successful, this method returns an empty response body.
    */

   @Named("Buckets:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   HttpResponse deleteBuckets(@PathParam("bucket") String bucketName, DeleteBucketsOptions options);

   /**
    * Retrieves a list of buckets for a given project
    * 
    * @param project
    *           Name of the project to retrieve the buckets
    * */

   @Named("Buckets:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ListPage<Buckets> listBuckets(@QueryParam("project") String project);

   /**
    * Retrieves a list of buckets for a given project
    * 
    * @param project
    *           Name of the project to retrieve the buckets
    * @param options
    *           Supply {@link ListOptions} with optional query parameters
    * 
    * */

   @Named("Buckets:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ListPage<Buckets> listBuckets(@QueryParam("project") String project, ListOptions options);

   /**
    * Updates a bucket
    * 
    * @param bucketName
    *           Name of the bucket
    * @param bucket
    *           In the request body, supply a bucket resource with acl[]
    * 
    * @return If successful, this method returns a Buckets resource in the response body
    */

   @Named("Buckets:update")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   Buckets updateBuckets(@PathParam("bucket") String bucketName, @BinderParam(BindToJsonPayload.class) Buckets bucket);

   /**
    * Updates a bucket
    * 
    * @param bucketName
    *           In the request body, supply a bucket resource with acl[]
    * 
    * @param options
    *           Supply {@link UpdateBucketsOptions} with optional query parameters
    * 
    * @return If successful, this method returns a Buckets resource in the response body
    */

   @Named("Buckets:update")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/acl/{entity}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   Buckets updateBuckets(@PathParam("bucket") String bucketName, @BinderParam(BindToJsonPayload.class) Buckets buckets,
            UpdateBucketsOptions options);

   /**
    * Updates a bucket.This method supports patch semantics.
    * 
    * @param bucketName
    *           In the request body, supply a bucket resource with acl[]
    * 
    * @param bucket
    *           In the request body, supply the relevant portions of a bucket resource, according to the rules of patch
    *           semantics
    * 
    * @return If successful, this method returns a Buckets resource in the response body
    */

   @Named("Buckets:patch")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   Buckets patchBuckets(@PathParam("bucket") String bucketName, @BinderParam(BindToJsonPayload.class) Buckets bucket);

   /**
    * Updates a bucket.This method supports patch semantics.
    * 
    * @param bucketName
    *           In the request body, supply a bucket resource with acl[]
    * 
    * @param options
    *           Supply {@link UpdateBucketsOptions} with optional query parameters
    * 
    * @param bucket
    *           In the request body, supply the relevant portions of a bucket resource, according to the rules of patch
    *           semantics
    * 
    * @return If successful, this method returns a Buckets resource in the response body
    */

   @Named("Buckets:patch")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   Buckets patchBuckets(@PathParam("bucket") String bucketName, @BinderParam(BindToJsonPayload.class) Buckets bucket,
            UpdateBucketsOptions options);
}
