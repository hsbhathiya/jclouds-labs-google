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

package org.jclouds.googlecloudstorage.domain;

import java.util.Set;

import org.jclouds.googlecloudstorage.domain.internal.Metadata;
import org.jclouds.http.internal.PayloadEnclosingImpl;

import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.net.MediaType;

public class ObjectTemplate extends PayloadEnclosingImpl {

   protected String name;
   protected Long size;
   protected String cacheControl;
   protected String contentDisposition;
   protected String contentEncoding;
   protected String contentLanguage;
   protected String contentType;
   protected String crc32c;
   protected String md5Hash;
   protected Metadata metadata;
   protected Set<ObjectAccessControls> acl = Sets.newHashSet();

   public ObjectTemplate name(String name) {
      this.name = name;
      return this;
   }
   
   public ObjectTemplate size(Long size) {
      this.size = size;
      return this;
   }

   public ObjectTemplate cacheControl(String cacheControl) {
      this.cacheControl = cacheControl;
      return this;
   }

   public ObjectTemplate contentDisposition(String contentDisposition) {
      this.contentDisposition = contentDisposition;
      return this;
   }

   public ObjectTemplate contentEncoding(String contentEncoding) {
      this.contentEncoding = contentEncoding;
      return this;
   }

   public ObjectTemplate contentLanguage(String contentLanguage) {
      this.contentLanguage = contentLanguage;
      return this;
   }
   
   public ObjectTemplate contentType(MediaType contentType) {
      this.contentType = contentType.toString();
      return this;
   }

   public ObjectTemplate contentType(String contentType) {
      this.contentType = contentType;
      return this;
   }

   public ObjectTemplate metadata(Metadata metadata) {
      this.metadata = metadata;
      return this;
   }

   public ObjectTemplate crc32c(String crc32c) {
      this.crc32c = crc32c;
      return this;
   }
   
   public ObjectTemplate md5Hash(HashCode md5Hash) {
      this.md5Hash = md5Hash.toString();
      return this;
   }

   public ObjectTemplate md5Hash(String md5Hash) {
      this.md5Hash = md5Hash;
      return this;
   }

   public ObjectTemplate addAcl(ObjectAccessControls acl) {
      this.acl.add(acl);
      return this;
   }

   public ObjectTemplate acl(Set<ObjectAccessControls> acl) {
      this.acl.addAll(acl);
      return this;
   }

   public String getCacheControl() {
      return cacheControl;
   }

   public String getContentDisposition() {
      return contentDisposition;
   }

   public String getContentEncoding() {
      return contentEncoding;
   }

   public String getContentLanguage() {
      return contentLanguage;
   }

   public String getContentType() {
      return contentType;
   }

   public String getCrc32c() {
      return crc32c;
   }

   public String getMd5Hash() {
      return md5Hash;
   }

   public Metadata getMetadata() {
      return metadata;
   }

   public String getName() {
      return name;
   }

   public Long getSize() {
      return size;
   }
   public Set<ObjectAccessControls> getAcl() {
      return acl;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static ObjectTemplate fromObjectTemplate(ObjectTemplate objectTemplate) {
      return Builder.fromObjectTemplate(objectTemplate);
   }

   
   public static class Builder {

      public static ObjectTemplate fromObjectTemplate(ObjectTemplate in) {
         return new ObjectTemplate().name(in.getName()).size(in.getSize()).acl(in.getAcl()).cacheControl(in.getCacheControl())
                  .contentDisposition(in.getContentDisposition()).contentEncoding(in.getContentEncoding())
                  .contentLanguage(in.getContentLanguage()).contentType(in.getContentType()).md5Hash(in.getMd5Hash())
                  .metadata(in.getMetadata()).crc32c(in.getCrc32c());

      }
   }
}
