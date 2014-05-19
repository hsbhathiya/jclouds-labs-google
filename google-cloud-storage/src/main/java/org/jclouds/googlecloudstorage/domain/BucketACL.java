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
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getLast;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.Date;
import java.util.Set;

import org.jclouds.googlecomputeengine.domain.Instance;
import org.jclouds.googlecomputeengine.domain.Instance.Builder;
import org.jclouds.javax.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;

/**
 * Represents a Bucket Access Control Resource.
 * 
 * @author Bhathiya Supun
 * @see <a href=
 *      "https://developers.google.com/storage/docs/json_api/v1/bucketAccessControls"
 *      />
 */
public class BucketACL extends Resource {

   public enum Role {
      READER, WRITER, OWNER
   }

   protected String bucket;
   protected String entity;
   protected Role role;
   protected String email;
   protected String domain;
   protected String entityId;
   protected ProjectTeam projectTeam;

   protected BucketACL(String id, URI selfLink, String name, String etag, String bucket, String entity,
         String entityId, Role role, String email, String domain, ProjectTeam projectTeam) {
      super(Kind.BUCKETACCESSCONTROL, id, selfLink, name, etag);
      this.bucket = checkNotNull(bucket, "bucket");
      this.entity = checkNotNull(entity, "entity");
      this.entityId = checkNotNull(entityId, "entityId");
      this.role = checkNotNull(role, "role");
      this.email = email;
      this.domain = domain;
      this.projectTeam = checkNotNull(projectTeam, "projectTeam");

   }
   

   public String getBucket() {
      return bucket;
   }


   public String getEntity() {
      return entity;
   }


   public Role getRole() {
      return role;
   }


   public String getEmail() {
      return email;
   }


   public String getDomain() {
      return domain;
   }


   public String getEntityId() {
      return entityId;
   }


   public ProjectTeam getProjectTeam() {
      return projectTeam;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      BucketACL that = BucketACL.class.cast(obj);
      return equal(this.kind, that.kind) && equal(this.bucket, that.bucket) && equal(this.entity, that.entity);
   }

   protected Objects.ToStringHelper string() {
      return super.string().omitNullValues().add("bucket", bucket).add("entity", entity).add("entityId", entityId)
            .add("role", role).add("email", email).add("domain", domain);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromBucketACL(this);
   }

   public static final class Builder extends Resource.Builder<Builder> {

      protected String bucket;
      protected String entity;
      protected String entityId;
      protected Role role;
      protected String email;
      protected String domain;
      protected ProjectTeam projectTeam;

      public Builder bucket(String bucket) {
         this.bucket = bucket;
         return this;
      }

      public Builder entity(String entity) {
         this.entity = entity;
         return this;
      }

      public Builder entityId(String entityId) {
         this.entityId = entityId;
         return this;
      }

      public Builder role(Role role) {
         this.role = role;
         return this;
      }

      public Builder email(String email) {
         this.email = email;
         return this;
      }

      public Builder domain(String domain) {
         this.domain = domain;
         return this;
      }

      public Builder projectTeam(ProjectTeam projectTeam) {
         this.projectTeam = projectTeam;
         return this;
      }

      public BucketACL build() {
         return new BucketACL(super.id, super.selfLink, super.name, super.etag, bucket, entity, entityId, role, email,
               domain, projectTeam);

      }
      
      public Builder fromBucketACL(BucketACL bACL) {
         return super.fromResource(bACL)
               .bucket(bACL.getBucket())
               .entity(bACL.getEntity())
               .entityId(bACL.getEntityId())
               .role(bACL.getRole())
               .email(bACL.getEmail())
               .domain(bACL.getDomain())
               .projectTeam(bACL.getProjectTeam());
               
                 
      }

      @Override
      protected Builder self() {
         return this;
      }


   }

   public static class ProjectTeam {

      public enum Team {
         owner, editor, viewer;
      }

      private final String projectId;
      private final Team team;

      @ConstructorProperties({ "projectId", "team" })
      public ProjectTeam(String projectId, Team team) {
         this.projectId = checkNotNull(projectId);
         this.team = checkNotNull(team);
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(projectId, team);
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null || getClass() != obj.getClass())
            return false;
         ProjectTeam that = ProjectTeam.class.cast(obj);
         return equal(this.projectId, that.projectId) && equal(this.team, that.team);
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).add("projectId", projectId).add("team", team);
      }

      @Override
      public String toString() {
         return string().toString();
      }
   }

}