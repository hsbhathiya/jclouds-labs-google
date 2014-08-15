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
package org.jclouds.googlecloudstorage.blobstore;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.internal.BlobBuilderImpl;
import org.jclouds.blobstore.domain.internal.BlobImpl;
import org.jclouds.blobstore.domain.internal.PageSetImpl;
import org.jclouds.blobstore.internal.BaseBlobStore;
import org.jclouds.blobstore.options.CreateContainerOptions;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.blobstore.strategy.internal.FetchBlobMetadata;
import org.jclouds.blobstore.util.BlobUtils;
import org.jclouds.collect.Memoized;
import org.jclouds.domain.Location;
import org.jclouds.googlecloudstorage.GoogleCloudStorageApi;
import org.jclouds.googlecloudstorage.blobstore.functions.BlobMetadataToObjectTemplate;
import org.jclouds.googlecloudstorage.blobstore.functions.BucketToStorageMetadata;
import org.jclouds.googlecloudstorage.blobstore.functions.ObjectListToStorageMetadata;
import org.jclouds.googlecloudstorage.blobstore.functions.ObjectToBlobMetadata;
import org.jclouds.googlecloudstorage.domain.Bucket;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Role;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.templates.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ObjectTemplate;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.jclouds.http.HttpResponseException;
import org.jclouds.http.internal.PayloadEnclosingImpl;
import org.jclouds.io.ContentMetadata;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.ByteSourcePayload;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashCode;
import com.google.common.io.ByteSource;
import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.google.inject.Provider;

@Singleton
public class GCSBlobStore extends BaseBlobStore {
   private final String PROJECT_ID = System.getProperty("test.google-cloud-storage.project-number");

   GoogleCloudStorageApi api;
   BucketToStorageMetadata bucketToStorageMetadata;
   ObjectToBlobMetadata objectToBlobMetadata;
   ObjectListToStorageMetadata objectListToStorageMetadata;
   Provider<FetchBlobMetadata> fetchBlobMetadataProvider;
   BlobMetadataToObjectTemplate blobMetadataToObjectTemplate;

   @Inject
   protected GCSBlobStore(BlobStoreContext context, BlobUtils blobUtils, Supplier<Location> defaultLocation,
            @Memoized Supplier<Set<? extends Location>> locations, GoogleCloudStorageApi api,
            BucketToStorageMetadata bucketToStorageMetadata, ObjectToBlobMetadata objectToBlobMetadata,
            ObjectListToStorageMetadata objectListToStorageMetadata,
            Provider<FetchBlobMetadata> fetchBlobMetadataProvider,
            BlobMetadataToObjectTemplate blobMetadataToObjectTemplate) {
      super(context, blobUtils, defaultLocation, locations);
      this.api = api;
      this.bucketToStorageMetadata = bucketToStorageMetadata;
      this.objectToBlobMetadata = objectToBlobMetadata;
      this.objectListToStorageMetadata = objectListToStorageMetadata;
      this.fetchBlobMetadataProvider = checkNotNull(fetchBlobMetadataProvider, "fetchBlobMetadataProvider");
      this.blobMetadataToObjectTemplate = blobMetadataToObjectTemplate;
   }

   @Override
   public PageSet<? extends StorageMetadata> list() {
      return new Function<ListPage<Bucket>, org.jclouds.blobstore.domain.PageSet<? extends StorageMetadata>>() {
         public org.jclouds.blobstore.domain.PageSet<? extends StorageMetadata> apply(ListPage<Bucket> from) {
            return new PageSetImpl<StorageMetadata>(Iterables.transform(from, bucketToStorageMetadata), null);
         }
      }.apply(api.getBucketApi().listBucket(PROJECT_ID));
   }

   @Override
   public boolean containerExists(String container) {
      return api.getBucketApi().getBucket(container) != null;
   }

   @Override
   public boolean createContainerInLocation(Location location, String container) {

      if (containerExists(container)) {
         return true;
      }

      BucketTemplate template = new BucketTemplate().name(container);
      if (location != null) {
         org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Location gcsLocation = org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Location
                  .fromValue(location.getId());
         template = template.location(gcsLocation);
      }
      return api.getBucketApi().createBucket(PROJECT_ID, template) != null;
   }

   @Override
   public boolean createContainerInLocation(Location location, String container, CreateContainerOptions options) {

      if (containerExists(container)) {
         return true;
      }
      org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Location gcsLocation = org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Location
               .fromValue(location.getId());
      BucketTemplate template = new BucketTemplate().name(container).location(gcsLocation);
      if (options.isPublicRead()) {
         BucketAccessControls acl = BucketAccessControls.builder().bucket(container).entity("allUsers")
                  .role(Role.READER).build();
         template.addAcl(acl);
      }
      return api.getBucketApi().createBucket(PROJECT_ID, template) != null;
   }

   @Override
   public PageSet<? extends StorageMetadata> list(String container, ListContainerOptions options) {

      if (options != null && options.getMaxResults() != null && options.getMarker() != null) {
         ListObjectOptions listOptions = new ListObjectOptions().maxResults(options.getMaxResults()).pageToken(
                  options.getMarker());
         ListPage<GCSObject> gcsList = api.getObjectApi().listObjects(container, listOptions);
         PageSet<? extends StorageMetadata> list = objectListToStorageMetadata.apply(gcsList);
         return options.isDetailed() ? fetchBlobMetadataProvider.get().setContainerName(container).apply(list) : list;
      } else {
         ListPage<GCSObject> gcsList = api.getObjectApi().listObjects(container);
         PageSet<? extends StorageMetadata> list = objectListToStorageMetadata.apply(gcsList);
         return list;
      }
   }

   /**
    * Checks whether an accessible object is available.Google cloud storage does not support directly support
    * BucketExist or ObjectExist operations
    */
   @Override
   public boolean blobExists(String container, String name) {
      return api.getObjectApi().getObject(container, name) != null;
   }

   /**
    * This supports multipart/related upload which has exactly 2 parts, media-part and metadata-part
    */
   @Override
   public String putBlob(String container, Blob blob) {
      checkNotNull(blob.getPayload().getContentMetadata().getContentLength());
      
     ObjectTemplate template = blobMetadataToObjectTemplate.apply(blob.getMetadata());
      
    /*  ContentMetadata metadata  = blob.getPayload().getContentMetadata();
      BlobMetadata from = blob.getMetadata();
      String name = from.getName();
      Map<String, String>  userMeta = from.getUserMetadata();      
           
      String contentDisposition = metadata.getContentDisposition();
      String contentEncoding = metadata.getContentEncoding();
      String contentLanguage = metadata.getContentLanguage();
      String contentType = metadata.getContentType();
      Long contentLength = metadata.getContentLength();
            
      HashCode md5 = metadata.getContentMD5AsHashCode();
      
      ObjectTemplate template = new ObjectTemplate().contentType(contentType).size(contentLength)
               .contentEncoding(contentEncoding).contentLanguage(contentLanguage)
               .contentDisposition(contentDisposition).name(name).customMetadata(userMeta);

      if(md5!= null){
         template = template.md5Hash(md5);
      }*/
      //ObjectTemplate template= new ObjectTemplate().contentType(blob.getPayload().getContentMetadata().getContentType());
      return api.getObjectApi().multipartUpload(container, template, blob.getPayload()).getEtag();
   }

   @Override
   public String putBlob(String container, Blob blob, PutOptions options) {
      if (!options.multipart().isMultipart()) {
         return putBlob(container, blob);
      }
      return null;
   }

   @Override
   public BlobMetadata blobMetadata(String container, String name) {
      return objectToBlobMetadata.apply(api.getObjectApi().getObject(container, name));
   }

   @Override
   public Blob getBlob(String container, String name, org.jclouds.blobstore.options.GetOptions options) {
      PayloadEnclosingImpl impl = api.getObjectApi().download(container, name);
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      GCSObject gcsObject = api.getObjectApi().getObject(container, name);
      Blob blob = new BlobImpl(objectToBlobMetadata.apply(gcsObject));
      blob.setPayload(impl.getPayload());
      return blob;
   }

   @Override
   public void removeBlob(String container, String name) {
      api.getObjectApi().deleteObject(container, name);
   }

   @Override
   protected boolean deleteAndVerifyContainerGone(String container) {
      api.getBucketApi().deleteBucket(container);
      return !containerExists(container);

   }

}
