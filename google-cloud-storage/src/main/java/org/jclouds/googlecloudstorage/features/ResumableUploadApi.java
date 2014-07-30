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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.googlecloudstorage.domain.ComposeObjectTemplate;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.ObjectTemplate;
import org.jclouds.googlecloudstorage.domain.ResumableUploadResponse;
import org.jclouds.googlecloudstorage.domain.WatchAllTemplate;
import org.jclouds.googlecloudstorage.handlers.ComposeObjectBinder;
import org.jclouds.googlecloudstorage.handlers.MultipartUploadBinder;
import org.jclouds.googlecloudstorage.handlers.ResumableUploadBinder;
import org.jclouds.googlecloudstorage.handlers.SimpleUploadBinder;
import org.jclouds.googlecloudstorage.handlers.SimpleUploadBinder2;
import org.jclouds.googlecloudstorage.options.ComposeObjectOptions;
import org.jclouds.googlecloudstorage.options.CopyObjectOptions;
import org.jclouds.googlecloudstorage.options.DeleteObjectOptions;
import org.jclouds.googlecloudstorage.options.GetObjectOptions;
import org.jclouds.googlecloudstorage.options.InsertObjectOptions;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.jclouds.googlecloudstorage.options.UpdateObjectOptions;
import org.jclouds.http.HttpResponse;
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
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.SkipEncoding;
import org.jclouds.rest.annotations.QueryParams;
import org.jclouds.rest.binders.BindToJsonPayload;

/**
 * Provides Resumable Upload support via Rest API
 * 
 * @see <a href = " https://developers.google.com/storage/docs/json_api/v1/objects"/>
 * @see <a href = " https://developers.google.com/storage/docs/json_api/v1/how-tos/upload#resumable"/>
 */

@SkipEncoding({ '/', '=' })
@RequestFilters(OAuthAuthenticator.class)
public interface ResumableUploadApi {

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
   @QueryParams(keys = "uploadType", values = "resumable")
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/upload/storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(ResumableUploadBinder.class)
   HttpResponse initResumableUpload(@PathParam("bucket") String bucketName,
            @HeaderParam("X-Upload-Content-Type") String contentType,
            @HeaderParam("X-Upload-Content-Length") String contentLength,
            @PayloadParam("template") ObjectTemplate metadata);

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
   @Named("Object:Upload")
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @QueryParams(keys = "uploadType", values = "resumable")
   @Path("/upload/storage/v1/b/{bucket}/o")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @MapBinder(SimpleUploadBinder.class)
   HttpResponse upload(@PathParam("bucket") String bucketName ,@QueryParam("upload_id") String uploadId,
            @HeaderParam("Content-Type") String contentType, @HeaderParam("Content-Length") String contentLength,
            @PayloadParam("payload") Payload payload);

}
