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

import java.util.Set;

import javax.inject.Singleton;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.internal.BaseAsyncBlobStore;
import org.jclouds.blobstore.options.CreateContainerOptions;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.blobstore.util.BlobUtils;
import org.jclouds.domain.Location;

import com.google.common.base.Supplier;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * 
 * @author Bhathiya Supun
 * @deprecated will be removed in jclouds 1.7, as async interfaces are no longer
 *             supported. Please use {@link GoogleCloudStorageBlobStore}
 */
@Deprecated
@Singleton
public class GoogleCloudStorageAsyncBlobStore extends BaseAsyncBlobStore {

	protected GoogleCloudStorageAsyncBlobStore(BlobStoreContext context,
			BlobUtils blobUtils, ListeningExecutorService userExecutor,
			Supplier<Location> defaultLocation,
			Supplier<Set<? extends Location>> locations) {
		super(context, blobUtils, userExecutor, defaultLocation, locations);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ListenableFuture<PageSet<? extends StorageMetadata>> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<Boolean> containerExists(String container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<Boolean> createContainerInLocation(
			Location location, String container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<Boolean> createContainerInLocation(
			Location location, String container, CreateContainerOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<PageSet<? extends StorageMetadata>> list(
			String container, ListContainerOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<Boolean> blobExists(String container, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<String> putBlob(String container, Blob blob) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<String> putBlob(String container, Blob blob,
			PutOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<BlobMetadata> blobMetadata(String container,
			String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<Blob> getBlob(String container, String key,
			org.jclouds.blobstore.options.GetOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenableFuture<Void> removeBlob(String container, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean deleteAndVerifyContainerGone(String container) {
		// TODO Auto-generated method stub
		return false;
	}

  
}
