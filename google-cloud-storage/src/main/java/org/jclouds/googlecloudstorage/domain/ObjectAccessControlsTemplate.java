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

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.net.URI;

import org.jclouds.googlecloudstorage.domain.BucketsTemplate.Builder;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls.Role;

import com.google.common.base.Objects;

/**
 * Represents a Object Access Control Resource.
 * 
 * @author Bhathiya Supun
 * @see <a href= "https://developers.google.com/storage/docs/json_api/v1/objectAccessControls"/>
 */
public class ObjectAccessControlsTemplate {

   protected String entity;
   protected Role role;

   public ObjectAccessControlsTemplate role(Role role) {
      this.role = role;
      return this;
   }

   public ObjectAccessControlsTemplate entity(String entity) {
      this.entity = entity;
      return this;
   }

   public String getEntity() {
      return entity;
   }

   public Role getRole() {
      return role;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static ObjectAccessControlsTemplate fromObjectAccessControlsTemplate(
            ObjectAccessControlsTemplate objectAccessControlsTemplate) {
      return Builder.fromObjectAccessControlsTemplate(objectAccessControlsTemplate);
   }

   public static class Builder {

      public static ObjectAccessControlsTemplate fromObjectAccessControlsTemplate(ObjectAccessControlsTemplate in) {
         return new ObjectAccessControlsTemplate().role(in.getRole()).entity(in.getEntity());
      }

   }
}