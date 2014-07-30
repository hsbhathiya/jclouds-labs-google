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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.googlecloudstorage.binders.ComposeObjectBinder;
import org.jclouds.googlecloudstorage.binders.MultipartUploadBinder;
import org.jclouds.googlecloudstorage.binders.SimpleUploadBinder;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.WatchAllTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ComposeObjectTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ObjectTemplate;
import org.jclouds.googlecloudstorage.options.ComposeObjectOptions;
import org.jclouds.googlecloudstorage.options.CopyObjectOptions;
import org.jclouds.googlecloudstorage.options.DeleteObjectOptions;
import org.jclouds.googlecloudstorage.options.GetObjectOptions;
import org.jclouds.googlecloudstorage.options.InsertObjectOptions;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.jclouds.googlecloudstorage.options.UpdateObjectOptions;
import org.jclouds.io.Payload;
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
import org.jclouds.rest.annotations.QueryParams;
import org.jclouds.rest.binders.BindToJsonPayload;

/*Multipart Upload 
 * 
 * Separate watchAllTemaplete and Watch All, add params to watchAll 
 */

/**
 * Provides access to Object entities via their REST API.
 * 
 * @see <a href = " https://developers.google.com/storage/docs/json_api/v1/objects"/>
 */

@SkipEncoding({ '/', '=' })
@RequestFilters(OAuthAuthenticator.class)
public interface ObjectApi {

   /**
    * Retrieve an object or their metadata
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
   @Path("storage/v1/b/{bucket}/o/{object}")
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
    * @param options
    *           Supply {@link GetObjectOptions} with optional query parameters
    * 
    * @return a {@link GCSObject} resource
    */
   @Named("Object:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   GCSObject getObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            GetObjectOptions options);

   /**
    * Stores a new object
    * 
    * @see https://developers.google.com/storage/docs/json_api/v1/how-tos/upload#simple
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be stored
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters.
    * @param options
    *           Supply {@link InsertObjectOptions} with optional query parameters. 'name' is mandatory.
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:simpleUpload")
   @POST
   @QueryParams(keys = "uploadType", values = "media")
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/upload/storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(SimpleUploadBinder.class)
   GCSObject simpleUpload(@PathParam("bucket") String bucketName, @HeaderParam("Content-Type") String contentType,
            @HeaderParam("Content-Length") String contentLength, @PayloadParam("payload") Payload payload,
            InsertObjectOptions Options);

   // Not Functioning. Add ResponseParser ?
   /**
    * Stores a new object with metadata
    * 
    * @see https://developers.google.com/storage/docs/json_api/v1/how-tos/upload#simple
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be stored
    * @param uploadType
    *           The type of upload request.
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters.
    * @param options
    *           Supply {@link InsertObjectOptions} with optional query parameters. 'name' is mandatory.
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   
   @Named("Object:multipartUpload")
   @POST
   @QueryParams(keys = "uploadType", values = "multipart")
   //@Consumes("multipart/related")
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/upload/storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(MultipartUploadBinder.class)
   GCSObject multipartUpload(@PathParam("bucket") String bucketName,
            @PayloadParam("template") ObjectTemplate objectTemplate, @PayloadParam("payload") Payload payload);

   /**
    * Deletes an object and its metadata. Deletions are permanent if versioning is not enabled.
    * 
    * @param bucketName
    *           Name of the bucket in which the object to be deleted resides
    * @param objectName
    *           Name of the object
    */
   @Named("Object:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/{object}")
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
   @Path("storage/v1/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_WRITEONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   void deleteObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            DeleteObjectOptions options);

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
   @Path("storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ListPage<GCSObject> listObjects(@PathParam("bucket") String bucketName);

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
   @Path("storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ListPage<GCSObject> listObjects(@PathParam("bucket") String bucketName, ListObjectOptions options);

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
    * @return If successful, this method returns the updated {@link GCSObject} resource.
    */
   @Named("Object:update")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject updateObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @BinderParam(BindToJsonPayload.class) ObjectTemplate objectTemplate);

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
    * @return The updated {@link GCSObject} .
    */
   @Named("Object:update")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject updateObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @BinderParam(BindToJsonPayload.class) ObjectTemplate objectTemplate, UpdateObjectOptions options);

   /**
    * Updates an object according to patch semantics
    * 
    * @param bucketName
    *           Name of the bucket in which the object resides
    * @param objectName
    *           Name of the object
    * @param objectTemplate
    *           Supply {@link ObjectTemplate} with optional query parameters
    * 
    * @return If successful, this method returns the updated {@link GCSObject} resource.
    */
   @Named("Object:patch")
   @PATCH
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject patchObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @BinderParam(BindToJsonPayload.class) ObjectTemplate objectTemplate);

   /**
    * Updates an object according to patch semantics
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
    * @return If successful,this method returns the updated {@link GCSObject} resource.
    */
   @Named("Object:patch")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/{object}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   GCSObject patchObject(@PathParam("bucket") String bucketName, @PathParam("object") String objectName,
            @BinderParam(BindToJsonPayload.class) ObjectTemplate objectTemplate, UpdateObjectOptions options);

   /**
    * Concatenates a list of existing objects into a new object in the same bucket.
    * 
    * @param destinationBucket
    *           Name of the bucket in which the object to be stored
    * @param destinationObject
    *           The type of upload request.
    * @param composeObjectTemplate
    *           Supply a {@link ComposeObjectTemplate}
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:compose")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{destinationBucket}/o/{destinationObject}/compose")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(ComposeObjectBinder.class)
   GCSObject composeObjects(@PathParam("destinationBucket") String destinationBucket,
            @PathParam("destinationObject") String destinationObject,
            @PayloadParam("template") ComposeObjectTemplate composeObjectTemplate);

   /**
    * Concatenates a list of existing objects into a new object in the same bucket.
    * 
    * @param destinationBucket
    *           Name of the bucket in which the object to be stored
    * @param destinationObject
    *           The type of upload request.
    * @param composeObjectTemplate
    *           Supply a {@link ComposeObjectTemplate}
    * @param options
    *           Supply {@link ComposeObjectOptions} with optional query parameters
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:compose")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{destinationBucket}/o/{destinationObject}/compose")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(ComposeObjectBinder.class)
   GCSObject composeObjects(@PathParam("destinationBucket") String destinationBucket,
            @PathParam("destinationObject") String destinationObject,
            @PayloadParam("template") ComposeObjectTemplate composeObjectTemplate, ComposeObjectOptions options);

   /**
    * Copies an object to a specified location. Optionally overrides metadata.
    * 
    * @param destinationBucket
    *           Name of the bucket in which to store the new object
    * @param destinationObject
    *           Name of the new object.
    * @param sourceBucket
    *           Name of the bucket in which to find the source object
    * @param sourceObject
    *           Name of the source object
    * @param objectTemplate
    *           Supply an {@link ObjectTemplate}
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:copy")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/storage/v1/b/{sourceBucket}/o/{sourceObject}/copyTo/b/{destinationBucket}/o/{destinationObject}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   GCSObject copyObject(@PathParam("destinationBucket") String destinationBucket,
            @PathParam("destinationObject") String destinationObject, @PathParam("sourceBucket") String sourceBucket,
            @PathParam("sourceObject") String sourceObject);

   /**
    * Copies an object to a specified location. Optionally overrides metadata.
    * 
    * @param destinationBucket
    *           Name of the bucket in which to store the new object
    * @param destinationObject
    *           Name of the new object.
    * @param sourceBucket
    *           Name of the bucket in which to find the source object
    * @param sourceObject
    *           Name of the source object
    * @param objectTemplate
    *           Supply an {@link ObjectTemplate}
    * @param options
    *           Supply {@link CopyObjectOptions} with optional query parameters
    * 
    * @return If successful, this method returns a {@link GCSObject} resource.
    */
   @Named("Object:copy")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/storage/v1/b/{sourceBucket}/o/{sourceObject}/copyTo/b/{destinationBucket}/o/{destinationObject}")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   GCSObject copyObject(@PathParam("destinationBucket") String destinationBucket,
            @PathParam("destinationObject") String destinationObject, @PathParam("sourceBucket") String sourceBucket,
            @PathParam("sourceObject") String sourceObject, CopyObjectOptions options);

   // Need Changes

   /**
    * Retrieves a list of objects matching the criteria.
    * 
    * @param bucketName
    *           Name of the bucket in which to look for objects.
    * @param watchAlltemplate
    *           Supply an {@link WatchAllTemplate}
    * @param options
    *           Supply {@link ListObjectOptions} with options
    * 
    * @return a {@link WatchAllTemplate}
    */
   @Named("Object:list")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   WatchAllTemplate watchAllObjects(@PathParam("bucket") String bucketName,
            @PayloadParam("template") WatchAllTemplate watchAlltemplate, ListObjectOptions options);
}
