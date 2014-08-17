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

import java.util.Iterator;
import java.util.Set;

import javax.inject.Singleton;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.internal.BlobBuilderImpl;
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
import org.jclouds.googlecloudstorage.blobstore.functions.BlobStoreListContainerOptionsToListObjectOptions;
import org.jclouds.googlecloudstorage.blobstore.functions.BucketToStorageMetadata;
import org.jclouds.googlecloudstorage.blobstore.functions.ObjectListToStorageMetadata;
import org.jclouds.googlecloudstorage.blobstore.functions.ObjectToBlobMetadata;
import org.jclouds.googlecloudstorage.domain.Bucket;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.templates.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.templates.DefaultObjectAccessControlsTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ObjectTemplate;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.jclouds.http.HttpResponseException;
import org.jclouds.http.internal.PayloadEnclosingImpl;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashCode;
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
   BlobStoreListContainerOptionsToListObjectOptions listContainerOptionsToListObjectOptions;

   @Inject
   protected GCSBlobStore(BlobStoreContext context, BlobUtils blobUtils, Supplier<Location> defaultLocation,
            @Memoized Supplier<Set<? extends Location>> locations, GoogleCloudStorageApi api,
            BucketToStorageMetadata bucketToStorageMetadata, ObjectToBlobMetadata objectToBlobMetadata,
            ObjectListToStorageMetadata objectListToStorageMetadata,
            Provider<FetchBlobMetadata> fetchBlobMetadataProvider,
            BlobMetadataToObjectTemplate blobMetadataToObjectTemplate,
            BlobStoreListContainerOptionsToListObjectOptions listContainerOptionsToListObjectOptions) {
      super(context, blobUtils, defaultLocation, locations);
      this.api = api;
      this.bucketToStorageMetadata = bucketToStorageMetadata;
      this.objectToBlobMetadata = objectToBlobMetadata;
      this.objectListToStorageMetadata = objectListToStorageMetadata;
      this.fetchBlobMetadataProvider = checkNotNull(fetchBlobMetadataProvider, "fetchBlobMetadataProvider");
      this.blobMetadataToObjectTemplate = blobMetadataToObjectTemplate;
      this.listContainerOptionsToListObjectOptions = listContainerOptionsToListObjectOptions;
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
         return false;
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
         return false;
      }
      BucketTemplate template = new BucketTemplate().name(container);
      if (location != null) {
         org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Location gcsLocation = org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Location
                  .fromValue(location.getId());
         template = template.location(gcsLocation);
      }
      Bucket bucket = api.getBucketApi().createBucket(PROJECT_ID, template);
      if (options.isPublicRead()) {
         try {
            DefaultObjectAccessControlsTemplate doAclTemplate = new DefaultObjectAccessControlsTemplate().entity(
                     "allUsers").role(ObjectRole.READER);
            api.getDefaultObjectAccessControlsApi().createDefaultObjectAccessControls(container, doAclTemplate);
         } catch (HttpResponseException e) {
            // If DefaultObjectAccessControls operation fail, Reverse create operation the operation.
            api.getBucketApi().deleteBucket(container);
            return false;
         }
      }

      return bucket != null;
   }
   /** Returns list of objects in root*/
   @Override
   public PageSet<? extends StorageMetadata> list(String container) {
    //  ListObjectOptions options = new ListObjectOptions().delimiter("/");
      ListPage<GCSObject> gcsList = api.getObjectApi().listObjects(container);
      PageSet<? extends StorageMetadata> list = objectListToStorageMetadata.apply(gcsList);
      return list;
      
   }

   @Override
   public PageSet<? extends StorageMetadata> list(String container, ListContainerOptions options) {
      

      if (options != null && options != ListContainerOptions.NONE ) {
       //  ListObjectOptions listOptions = new ListObjectOptions().maxResults(options.getMaxResults()).pageToken(
       //           options.getMarker());
         ListObjectOptions listOptions =  listContainerOptionsToListObjectOptions.apply(options);
         ListPage<GCSObject> gcsList = api.getObjectApi().listObjects(container, listOptions);
         PageSet<? extends StorageMetadata> list = objectListToStorageMetadata.apply(gcsList);
         return options.isDetailed() ? fetchBlobMetadataProvider.get().setContainerName(container).apply(list) : list;
      } else {

         return list(container);
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
      try {
         ObjectTemplate template = blobMetadataToObjectTemplate.apply(blob.getMetadata());
         if (blob.getMetadata().getContentMetadata().getContentMD5AsHashCode() != null) {
            checkNotNull(template.getMd5Hash());
         }
         return api.getObjectApi().multipartUpload(container, template, blob.getPayload()).getEtag();
      } catch (HttpResponseException e) {
         e.printStackTrace();
      }
      return null;
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
      Blob blob = new BlobBuilderImpl().payload(impl.getPayload()).payload(impl.getPayload())
               .contentType(gcsObject.getContentType()).contentDisposition(gcsObject.getContentDisposition())
               .contentEncoding(gcsObject.getContentEncoding()).contentLanguage(gcsObject.getContentLanguage())
               .contentLength(gcsObject.getSize()).contentMD5(HashCode.fromBytes(gcsObject.getMd5AsByteArray()))
               .name(gcsObject.getName()).userMetadata(gcsObject.getAllMetadata()).build();
      // Blob blob = new BlobImpl(objectToBlobMetadata.apply(gcsObject));
      blob.getMetadata().setContainer(container);
      blob.getMetadata().setLastModified(gcsObject.getUpdated());
      blob.getMetadata().setETag(gcsObject.getEtag());
      blob.getMetadata().setPublicUri(gcsObject.getMediaLink());
      blob.getMetadata().setUserMetadata(gcsObject.getAllMetadata());
      blob.getMetadata().setUri(gcsObject.getSelfLink());
      blob.getMetadata().setId(gcsObject.getId());
      // blob.setPayload(impl.getPayload());
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
   
  /* @Override
   public void clearContainer(String containerName) {
      // TODO Auto-generated method stub
      super.clearContainer(containerName);
      //Delete the folders
      ListPage<GCSObject> list =  api.getObjectApi().listObjects(containerName);
      Iterator<String> it =list.getPrefixes().iterator();
      while(it.hasNext()){
         String prefix = it.next();
         removeBlob(containerName, prefix);
         
      };
   }*/

}
