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
package org.jclouds.googlecloudstorage.blobstore.functions;

import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.GOOGLE_PROVIDER_LOCATION;

import org.jclouds.blobstore.domain.MutableStorageMetadata;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.domain.internal.MutableStorageMetadataImpl;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.googlecloudstorage.domain.Bucket;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BucketToStorageMetadata implements Function<Bucket, StorageMetadata> {
   private Supplier<Location> defaultLocation;

   @Inject
   BucketToStorageMetadata(Supplier<Location> defaultLocation) {
      this.defaultLocation = defaultLocation;
   }

   public StorageMetadata apply(Bucket from) {
      MutableStorageMetadata to = new MutableStorageMetadataImpl();
      to.setName(from.getName());
      if (from.getLocation() != null) {
         to.setLocation(new LocationBuilder().scope(LocationScope.REGION).id(from.getLocation().value())
                  .description(from.getLocation().value()).parent(GOOGLE_PROVIDER_LOCATION).build());
      } else {
         to.setLocation(defaultLocation.get());
      }
      to.setType(StorageType.CONTAINER);
      return to;
   }
}
