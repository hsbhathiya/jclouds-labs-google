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

package org.jclouds.googlecloudstorage.domain.templates;

import java.net.URI;
import java.util.Map;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.DeliveryType;
import com.google.common.collect.ImmutableMap;

public class ObjectChangeNotificationTemplate {
   private String id;
   private String resourceId;
   private URI resourceUri;
   private String token;
   private Long expiration;
   private DeliveryType type;
   private URI address;
   private Boolean payload;
   private ImmutableMap.Builder<String, String> params = ImmutableMap.builder();

   public ObjectChangeNotificationTemplate id(String id) {
      this.id = id;
      return this;
   }

   public ObjectChangeNotificationTemplate resourceId(String resourceId) {
      this.resourceId = resourceId;
      return this;
   }

   public ObjectChangeNotificationTemplate token(String token) {
      this.token = token;
      return this;
   }

   public ObjectChangeNotificationTemplate resourceUri(URI resourceUri) {
      this.resourceUri = resourceUri;
      return this;
   }

   public ObjectChangeNotificationTemplate expiration(Long expiration) {
      this.expiration = expiration;
      return this;
   }

   public ObjectChangeNotificationTemplate address(URI address) {
      this.address = address;
      return this;
   }

   public ObjectChangeNotificationTemplate type(DeliveryType type) {
      this.type = type;
      return this;
   }

   public ObjectChangeNotificationTemplate payload(Boolean payload) {
      this.payload = payload;
      return this;
   }

   public ObjectChangeNotificationTemplate addParams(String key, String value) {
      this.params.put(key, value);
      return this;
   }

   public ObjectChangeNotificationTemplate addParams(Map<String, String> params) {
      this.params.putAll(params);
      return this;
   }

   public ObjectChangeNotificationTemplate addParam(String key, String value) {
      this.params.put(key, value);
      return this;
   }

   public String getId() {
      return id;
   }

   public String getResourceId() {
      return resourceId;
   }

   public URI getResourceUri() {
      return resourceUri;
   }

   public String getToken() {
      return token;
   }

   public Long getExpiration() {
      return expiration;
   }

   public DeliveryType getType() {
      return type;
   }

   public URI getAddress() {
      return address;
   }

   public Boolean getPayload() {
      return payload;
   }

   public ImmutableMap.Builder<String, String> getParams() {
      return params;
   }

   public static ObjectChangeNotificationTemplate fromWatchAllTemplate(ObjectChangeNotificationTemplate watchAllTemplate) {
      return Builder.fromWatchAllTemplate(watchAllTemplate);
   }

   public static class Builder {

      public static ObjectChangeNotificationTemplate fromWatchAllTemplate(ObjectChangeNotificationTemplate in) {
         return new ObjectChangeNotificationTemplate().id(in.getId()).resourceId(in.getResourceId()).resourceUri(in.getResourceUri())
                  .address(in.getAddress()).expiration(in.getExpiration()).payload(in.getPayload()).type(in.getType())
                  .token(in.getToken());
      }
   }

}
