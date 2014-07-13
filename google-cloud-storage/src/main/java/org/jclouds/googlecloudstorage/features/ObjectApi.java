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
import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_WRITEONLY_SCOPE;

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
import org.jclouds.googlecloudstorage.domain.Bucket;
import org.jclouds.googlecloudstorage.domain.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.UploadType;
import org.jclouds.googlecloudstorage.domain.ObjectTemplate;
import org.jclouds.googlecloudstorage.handlers.ObjectBinder;
import org.jclouds.googlecloudstorage.options.DeleteBucketOptions;
import org.jclouds.googlecloudstorage.options.GetBucketOptions;
import org.jclouds.googlecloudstorage.options.InsertBucketOptions;
import org.jclouds.googlecloudstorage.options.ListOptions;
import org.jclouds.googlecloudstorage.options.UpdateBucketOptions;
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
 * Provides access to Object entities via their REST API.
 * 
 * @see <a href = " https://developers.google.com/storage/docs/json_api/v1/objects"/>
 */

@SkipEncoding({ '/', '=' })
@RequestFilters(OAuthAuthenticator.class)
public interface ObjectApi {

   /**
    * Retrieves objects or their metadata
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * 
    * @return a {@link Object} resource
    */
   @Named("Object:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   GCSObject getObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName);

   /**
    * Retrieves objects or their metadata
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * 
    * @param options
    *           Supply {@link GetObjectOptions} with optional query parameters
    * 
    * @return a {@link GCSObject} resource
    */
   @Named("Object:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   GCSObject getBucket(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            GetBucketOptions options); // Change to GetObjectOptions

   /**
    * Stores a new object and metadata
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be stored
    * @param uploadType
    *           The type of upload request.
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters.
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/upload/storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_WRITEONLY_SCOPE)
   @MapBinder(ObjectBinder.class)
   GCSObject insertObject(@PathParam("bucket") String bucketName, @QueryParam("uploadType") UploadType uploadType,
            @PayloadParam("template") ObjectTemplate objectTemplate);

   /**
    * Stores a new object and metadata
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be stored
    * @param uploadType
    *           The type of upload request.
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters.
    * @param options
    *           Supply {@link DeleteBucketOptions} with optional query parameters.
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/upload/storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_WRITEONLY_SCOPE)
   @MapBinder(ObjectBinder.class)
   GCSObject insertObject(@PathParam("bucket") String bucketName, @QueryParam("uploadType") UploadType uploadType,
            @PayloadParam("template") ObjectTemplate objectTemplate, InsertBucketOptions options); // change to
                                                                                                   // InsertObjectOptions

   /**
    * Deletes an object and its metadata. Deletions are permanent if versioning is not enabled for the bucket, or if the
    * generation parameter is used.
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be deleted resides
    * @param objectName
    *           Name of the object
    */
   @Named("Object:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_WRITEONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   void deleteObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName);

   /**
    * Deletes an object and its metadata. Deletions are permanent if versioning is not enabled for the bucket, or if the
    * generation parameter is used.
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be deleted resides
    * @param objectName
    *           Name of the object
    * @param options
    *           Supply {@link DeletObjectOptions} with optional query parameters
    */
   @Named("Object:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_WRITEONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   void delete(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            DeleteBucketOptions options);

   /**
    * Retrieves a list of objects matching the criteria.
    * 
    * @param bucketName
    *           Name of the bucket in which to look for objects.
    * 
    * @return a {@link ListPage<Object>}
    */
   @Named("Object:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ListPage<GCSObject> listObject(@PathParam("bucket") String bucketName);

   /**
    * Retrieves a list of objects matching the criteria.
    * 
    * @param bucketName
    *           Name of the bucket in which to look for objects.
    * 
    * @return a {@link ListPage<GCSObject>}
    */
   @Named("Object:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ListPage<GCSObject> listObject(@PathParam("bucket") String bucketName, ListOptions options); // ListOptions not right

   /**
    * Updates an object
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters
    * 
    * @return If successful, this method returns the updated {@link Bucket} resource.
    */
   @Named("Object:update")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject updateObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @PayloadParam("template") ObjectTemplate objectTemplate);

   /**
    * Updates an object
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters
    * @param options
    *           Supply {@link UpdateObjectOptions} with optional query parameters
    * 
    * @return If successful,this method returns the updated {@link Bucket} resource in the response body
    */
   @Named("Bucket:update")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject updateObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @PayloadParam("template") ObjectTemplate objectTemplate, UpdateBucketOptions options); //Create and change to UpdateObjectOptions

   /**
    * Updates a bucket supporting patch semantics.
    * 
    * @param bucketName
    *           In the request body, supply a bucket resource with list of {@link BucketAccessControls} (acl[])
    * @param bucketTemplate
    *           In the request body, supply the relevant portions of a bucket resource
    * 
    * @return If successful, this method returns the updated {@link Bucket} resource in the response body
    */
   @Named("Bucket:patch")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   Bucket patchBucket(@PathParam("bucket") String bucketName,
            @BinderParam(BindToJsonPayload.class) BucketTemplate bucketTemplate);
   /**
    * Updates an object supporting patch semantics
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters
    * 
    * @return If successful, this method returns the updated {@link Bucket} resource.
    */
   @Named("Object:patch")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject patchObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @PayloadParam("template") ObjectTemplate objectTemplate);

   /**
    * Updates an object supporting patch semantics
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters
    * @param options
    *           Supply {@link UpdateObjectOptions} with optional query parameters
    * 
    * @return If successful,this method returns the updated {@link Bucket} resource in the response body
    */
   @Named("Bucket:update")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/b/{bucket}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject patchObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @PayloadParam("template") ObjectTemplate objectTemplate, UpdateBucketOptions options); //Create and change to UpdateObjectOptions
}
