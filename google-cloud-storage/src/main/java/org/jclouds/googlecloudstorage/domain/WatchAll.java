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

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.DeliveryType;

import com.google.common.base.Objects;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * This supports object change notification settings
 *
 * @see <a href = "https://developers.google.com/storage/docs/json_api/v1/objects/watchAll"/>
 */
public class WatchAll {

   private final String kind;
   private final String id;
   private final String resourceId;
   private final URI resourceUri;
   private final String token;
   private final Long expiration;
   private final DeliveryType type;
   private final URI address;
   private final Boolean payload;
   private final Multimap <String,String> params;
   

   private WatchAll(String id, String resourceId, URI resourceUri, DeliveryType type, URI address, String token,
            Long expiration, Boolean payload , Multimap<String, String> params ) {

      this.kind = "api#channel";
      this.id = checkNotNull(id, "id");
      this.resourceId = checkNotNull(resourceId, "resourceId");
      this.resourceUri = checkNotNull(resourceUri, "resourceUri");
      this.type = checkNotNull(type, "DeliveryType");
      this.address = checkNotNull(address, "address");
      this.token = token;
      this.payload = payload;
      this.expiration = expiration;
      this.params = params; 
   }

   public String getKind() {
      return kind;
   }

   public String getId() {
      return id;
   }

   public Multimap<String, String> getParams() {
      return params;
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

   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((address == null) ? 0 : address.hashCode());
      result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((kind == null) ? 0 : kind.hashCode());
      result = prime * result + ((payload == null) ? 0 : payload.hashCode());
      result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
      result = prime * result + ((resourceUri == null) ? 0 : resourceUri.hashCode());
      result = prime * result + ((token == null) ? 0 : token.hashCode());
      result = prime * result + ((type == null) ? 0 : type.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      WatchAll other = (WatchAll) obj;
      if (address == null) {
         if (other.address != null)
            return false;
      } else if (!address.equals(other.address))
         return false;
      if (expiration == null) {
         if (other.expiration != null)
            return false;
      } else if (!expiration.equals(other.expiration))
         return false;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (kind == null) {
         if (other.kind != null)
            return false;
      } else if (!kind.equals(other.kind))
         return false;
      if (payload == null) {
         if (other.payload != null)
            return false;
      } else if (!payload.equals(other.payload))
         return false;
      if (resourceId == null) {
         if (other.resourceId != null)
            return false;
      } else if (!resourceId.equals(other.resourceId))
         return false;
      if (resourceUri == null) {
         if (other.resourceUri != null)
            return false;
      } else if (!resourceUri.equals(other.resourceUri))
         return false;
      if (token == null) {
         if (other.token != null)
            return false;
      } else if (!token.equals(other.token))
         return false;
      if (type != other.type)
         return false;
      return true;
   }

   protected Objects.ToStringHelper string() {
      return Objects.toStringHelper(this).omitNullValues().add("kind", kind).add("id", id).add("", resourceId)
               .add("resourceUri", resourceUri).add("type", type).add("address", address).add("token", token)
               .add("payload", payload);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromWatchAll(this);
   }

   public static final class Builder {

      private String id;
      private String resourceId;
      private URI resourceUri;
      private String token;
      private Long expiration;
      private DeliveryType type;
      private URI address;
      private Boolean payload;
      private Multimap<String, String> params = LinkedHashMultimap.create();

      public Builder id(String id) {
         this.id = id;
         return this;
      }

      public Builder resourceId(String resourceId) {
         this.resourceId = resourceId;
         return this;
      }

      public Builder token(String token) {
         this.token = token;
         return this;
      }

      public Builder resourceUri(URI resourceUri) {
         this.resourceUri = resourceUri;
         return this;
      }

      public Builder expiration(Long expiration) {
         this.expiration = expiration;
         return this;
      }

      public Builder address(URI address) {
         this.address = address;
         return this;
      }

      public Builder type(DeliveryType type) {
         this.type = type;
         return this;
      }

      public Builder payload(Boolean payload) {
         this.payload = payload;
         return this;
      }     

      public Builder params(Multimap<String, String> params) {
         this.params = params;
         return this;
      }
      
      public Builder addParam(String key , String value) {
         this.params.put(key, value);
         return this;
      }
      

      public WatchAll build() {
         return new WatchAll(id, resourceId, resourceUri, type, address, token, expiration, payload, params);
      }

      public Builder fromWatchAll(WatchAll in) {
         return this.id(in.getId()).resourceId(in.getResourceId()).resourceUri(in.getResourceUri())
                  .address(in.getAddress()).expiration(in.getExpiration()).payload(in.getPayload()).type(in.getType())
                  .token(in.getToken());
      }
   }

}
