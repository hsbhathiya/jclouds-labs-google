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
package org.jclouds.googlecloudstorage.blobstore.strategy.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Provider;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobBuilder;
import org.jclouds.blobstore.reference.BlobStoreConstants;
import org.jclouds.googlecloudstorage.GoogleCloudStorageApi;
import org.jclouds.googlecloudstorage.blobstore.functions.BlobMetadataToObjectTemplate;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.templates.ComposeObjectTemplate;
import org.jclouds.io.Payload;
import org.jclouds.io.PayloadSlicer;
import org.jclouds.logging.Logger;

import com.google.inject.Inject;

public class SequentialMultipartUploadStrategy implements MultipartUploadStrategy {

   @Resource
   @Named(BlobStoreConstants.BLOBSTORE_LOGGER)
   private Logger logger = Logger.NULL;

   private final GoogleCloudStorageApi api;
   private final Provider<BlobBuilder> blobBuilders;
   private final BlobMetadataToObjectTemplate blob2ObjectTemplate;
   private final MultipartUploadSlicingAlgorithm algorithm;
   private final PayloadSlicer slicer;
   private final MultipartNamingStrategy namingStrategy;

   @Inject
   public SequentialMultipartUploadStrategy(GoogleCloudStorageApi api, Provider<BlobBuilder> blobBuilders,
         BlobMetadataToObjectTemplate blob2ObjectTemplate, MultipartUploadSlicingAlgorithm algorithm, PayloadSlicer slicer,
         MultipartNamingStrategy namingStrategy) {
      this.api = checkNotNull(api, "api");
      this.blobBuilders = checkNotNull(blobBuilders, "blobBuilders");
      this.blob2ObjectTemplate = checkNotNull(blob2ObjectTemplate, "blob2Object");
      this.algorithm = checkNotNull(algorithm, "algorithm");
      this.slicer = checkNotNull(slicer, "slicer");
      this.namingStrategy = checkNotNull(namingStrategy, "namingStrategy");
   }

   @Override
   public String execute(String container, Blob blob) {
      ComposeObjectTemplate template = new ComposeObjectTemplate();
      String key = blob.getMetadata().getName();
      Payload payload = blob.getPayload();
      Long length = payload.getContentMetadata().getContentLength();
      if(length == null){
         length = blob.getMetadata().getContentMetadata().getContentLength();
         payload.getContentMetadata().setContentLength(length);
      }
      checkNotNull(length,
            "please invoke payload.getContentMetadata().setContentLength(length) prior to multipart upload");
      long chunkSize = algorithm.calculateChunkSize(length);
      int partCount = algorithm.getParts();
      if (partCount > 0) {
         for (Payload part : slicer.slice(payload, chunkSize)) {
            int partNum = algorithm.getNextPart();
            String partName = namingStrategy.getPartName(key, partNum, partCount);
            Blob blobPart = blobBuilders.get()
                                        .name(partName)
                                        .payload(part)
                                        .contentDisposition(partName)
                                        .contentLength(chunkSize)
                                        .contentType(blob.getMetadata().getContentMetadata().getContentType())
                                        .build();
            GCSObject object=  api.getObjectApi().multipartUpload(container, blob2ObjectTemplate.apply(blobPart.getMetadata()), blobPart.getPayload());
            template = template.addsourceObject(object);          
         }
         return api.getObjectApi().composeObjects(container, key, template).getEtag();
      } else {
         return api.getObjectApi().multipartUpload(container, blob2ObjectTemplate.apply(blob.getMetadata()), blob.getPayload()).getEtag();
      }
   }
}