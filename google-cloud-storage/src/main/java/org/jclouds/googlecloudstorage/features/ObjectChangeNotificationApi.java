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

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.googlecloudstorage.domain.ObjectChangeNotification;
import org.jclouds.googlecloudstorage.domain.templates.ObjectChangeNotificationTemplate;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.oauth.v2.config.OAuthScopes;
import org.jclouds.oauth.v2.filters.OAuthAuthenticator;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SkipEncoding;

/**
 * Provides Object change notification support via Rest API
 * 
 * @see <a href = " https://developers.google.com/storage/docs/json_api/v1/channels/stop"/>
 */

@SkipEncoding({ '/', '=' })
@RequestFilters(OAuthAuthenticator.class)
public interface ObjectChangeNotificationApi {

   /**
    * Watch for changes on all objects in a bucket.
    *
    * @param bucketName
    *           Name of the bucket which is observed.
    * @param template
    *           Supply an {@link ObjectChangeNotification}
    *
    * @return a {@link ObjectChangeNotification}
    */
   @Named("ObjectNotificatio:WatchAll")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/watch")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ObjectChangeNotification watchAllObjects(@PathParam("bucket") String bucketName,
            @PayloadParam("template") ObjectChangeNotificationTemplate template);

   /**
    * Watch for changes on all objects in a bucket.
    *
    * @param bucketName
    *           Name of the bucket which is observed.
    * @param template
    *           Supply an {@link ObjectChangeNotification}
    * @param options
    *           Supply {@link ListObjectOptions} with options
    *
    * @return a {@link ObjectChangeNotification}
    */
   @Named("ObjectNotificatio:WatchAll")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/watch")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   ObjectChangeNotification watchAllObjects(@PathParam("bucket") String bucketName,
            @PayloadParam("template") ObjectChangeNotificationTemplate template, ListObjectOptions options);

   /**
    * Watch for changes on all objects in a bucket.
    *
    * @param template
    *           Supply an {@link ObjectChangeNotification}
    */
   @Named("ObjectNotificatio:WatchAll")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("storage/v1/b/{bucket}/o/watch")
   @OAuthScopes(STORAGE_FULLCONTROL_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   void stop(@PayloadParam("template") ObjectChangeNotificationTemplate template);
}
