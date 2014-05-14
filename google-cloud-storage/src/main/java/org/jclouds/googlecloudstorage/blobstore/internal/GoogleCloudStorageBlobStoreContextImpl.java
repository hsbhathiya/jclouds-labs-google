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
package org.jclouds.googlecloudstorage.blobstore.internal;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.Context;
import org.jclouds.blobstore.AsyncBlobStore;
import org.jclouds.blobstore.BlobRequestSigner;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.attr.ConsistencyModel;
import org.jclouds.blobstore.internal.BlobStoreContextImpl;
import org.jclouds.googlecloudstorage.blobstore.GoogleCloudStorageBlobStore;
import org.jclouds.googlecloudstorage.blobstore.GoogleCloudStorageBlobStoreContext;
import org.jclouds.googlecloudstorage.blobstore.GoogleCloudStorageAsyncBlobStore;
import org.jclouds.location.Provider;
import org.jclouds.rest.Utils;

import com.google.common.reflect.TypeToken;

/**
 * @author Bhathiya Supun
 */
@Singleton
public class GoogleCloudStorageBlobStoreContextImpl extends BlobStoreContextImpl implements GoogleCloudStorageBlobStoreContext {

   @Inject
   public GoogleCloudStorageBlobStoreContextImpl(@Provider Context backend, @Provider TypeToken<? extends Context> backendType,
            Utils utils, ConsistencyModel consistencyModel,
            @SuppressWarnings("deprecation") AsyncBlobStore ablobStore, BlobStore blobStore,
            BlobRequestSigner blobRequestSigner) {
      super(backend, backendType, utils, consistencyModel, ablobStore,
               blobStore, blobRequestSigner);
   }

   @Override
   public GoogleCloudStorageBlobStore getBlobStore() {
      return GoogleCloudStorageBlobStore.class.cast(super.getBlobStore());
   }

}
