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
import java.util.Date;
import java.util.Set;

import org.jclouds.javax.annotation.Nullable;

import com.google.common.base.CaseFormat;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

public class Buckets extends Resource {

   public enum Location {
      ASIA, EU, US, ASIA_EAST1, US_CENTRAL1, US_CENTRAL2, US_EAST1, US_EAST2, US_EAST3, US_WEST1;

      public String value() {
         return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
      }
   }

   public enum StorageClass {
      STANDARD, DURABLE_REDUCED_AVAILABILITY;
   }

   protected final String name;
   protected final Long projectNumber;
   protected final Date timeCreated;
   protected final Long metageneration;
   protected final Set<BucketAccessControls> acl;
   protected final Set<BucketAccessControls> defaultObjectAcl;
   protected final Owner owner;
   protected final Location location;
   protected final Website website;
   protected final Logging logging;
   protected final Versioning versioning;
   protected final Set<Cors> cors;
   protected final LifeCycle lifeCycle;
   protected final StorageClass storageClass;

   
   protected Buckets(String id, URI selfLink, String name, String etag, Long projectNumber, Date timeCreated,
            Long metageneration, Set<BucketAccessControls> acl, Set<BucketAccessControls> defaultObjectAcl,
            Owner owner, Location location, Website website, Logging logging, Versioning versioning, Set<Cors> cors,
            LifeCycle lifeCycle, StorageClass storageClass) {

      super(Kind.bucket, id, selfLink, etag);
      this.projectNumber = checkNotNull(projectNumber, "projectNumber");
      this.timeCreated = checkNotNull(timeCreated, "timeCreated");
      this.metageneration = checkNotNull(metageneration, "metageneration");
      this.acl = acl.isEmpty() ? null : acl;
      this.defaultObjectAcl = acl.isEmpty() ? null : defaultObjectAcl;
      this.owner = checkNotNull(owner, "Owner");
      this.location = checkNotNull(location, "location");
      this.website = website;
      this.logging = logging;
      this.versioning = versioning;
      this.cors = acl.isEmpty() ? null : cors;
      this.lifeCycle = lifeCycle;
      this.storageClass = storageClass;
      this.name = checkNotNull(name, "name");
   }

   public Long getProjectNumber() {
      return projectNumber;
   }

   public String getName() {
      return name;
   }

   public Date getTimeCreated() {
      return timeCreated;
   }

   public Long getMetageneration() {
      return metageneration;
   }

   public Set<BucketAccessControls> getAcl() {
      return acl;
   }

   public Set<BucketAccessControls> getDefaultObjectAcl() {
      return defaultObjectAcl;
   }

   public Owner getOwner() {
      return owner;
   }

   public Location getLocation() {
      return location;
   }

   public Website getWebsite() {
      return website;
   }

   public Logging getLogging() {
      return logging;
   }

   public Versioning getVersioning() {
      return versioning;
   }

   public Set<Cors> getCors() {
      return cors;
   }

   public LifeCycle getLifeCycle() {
      return lifeCycle;
   }

   public StorageClass getStorageClass() {
      return storageClass;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      Buckets that = Buckets.class.cast(obj);
      return equal(this.kind, that.kind) && equal(this.name, that.name)
               && equal(this.projectNumber, that.projectNumber);

   }

   protected Objects.ToStringHelper string() {
      return super.string().omitNullValues().add("name", name).add("timeCreated", timeCreated)
               .add("projectNumber", projectNumber).add("metageneration", metageneration).add("acl", acl)
               .add("defaultObjectAcl", defaultObjectAcl).add("owner", owner).add("location", location)
               .add("website", website).add("logging", logging).add("versioning", versioning).add("cors", cors)
               .add("lifeCycle", lifeCycle).add("storageClass", storageClass);

   }

   @Override
   public String toString() {
      return string().toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromBucket(this);
   }

   public static final class Builder extends Resource.Builder<Builder> {

      private String name;
      private Long projectNumber;
      private Date timeCreated;
      private Long metageneration;
      private ImmutableSet.Builder<BucketAccessControls> acl = ImmutableSet.builder();
      private ImmutableSet.Builder<BucketAccessControls> defaultObjectAcl = ImmutableSet.builder();
      private Owner owner;
      private Location location;
      private Website website;
      private Logging logging;
      private Versioning versioning;
      private ImmutableSet.Builder<Cors> cors = ImmutableSet.builder();
      private LifeCycle lifeCycle;
      private StorageClass storageClass;

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder projectNumber(Long projectNumber) {
         this.projectNumber = projectNumber;
         return this;

      }

      public Builder timeCreated(Date timeCreated) {
         this.timeCreated = timeCreated;
         return this;
      }

      public Builder metageneration(Long metageneration) {
         this.metageneration = metageneration;
         return this;
      }

      public Builder owner(Owner owner) {
         this.owner = owner;
         return this;
      }

      public Builder location(Location location) {
         this.location = location;
         return this;
      }

      public Builder website(Website website) {
         this.website = website;
         return this;
      }

      public Builder logging(Logging logging) {
         this.logging = logging;
         return this;
      }

      public Builder versioning(Versioning versioning) {
         this.versioning = versioning;
         return this;
      }

      public Builder lifeCycle(LifeCycle lifeCycle) {
         this.lifeCycle = lifeCycle;
         return this;
      }

      public Builder storageClass(StorageClass storageClass) {
         this.storageClass = storageClass;
         return this;
      }

      public Builder addAcl(BucketAccessControls bucketAccessControls) {
         this.acl.add(bucketAccessControls);
         return this;
      }

      public Builder acl(Set<BucketAccessControls> acl) {
         this.acl.addAll(acl);
         return this;
      }

      public Builder addDefaultObjectAcl(BucketAccessControls bucketAccessControls) {
         this.defaultObjectAcl.add(bucketAccessControls);
         return this;
      }

      public Builder defaultObjectAcl(Set<BucketAccessControls> defaultObjectAcl) {
         this.defaultObjectAcl.addAll(defaultObjectAcl);
         return this;
      }

      public Builder addCORS(Cors cors) {
         this.cors.add(cors);
         return this;
      }

      public Builder cors(Set<Cors> cors) {
         this.cors.addAll(cors);
         return this;
      }

      @Override
      protected Builder self() {
         return this;
      }

      public Buckets build() {
         return new Buckets(super.id, super.selfLink, name, super.etag, projectNumber, timeCreated, metageneration,
                  acl.build(), defaultObjectAcl.build(), owner, location, website, logging, versioning, cors.build(),
                  lifeCycle, storageClass);
      }

      public Builder fromBucket(Buckets in) {
         return super.fromResource(in).name(in.getName()).projectNumber(in.getProjectNumber())
                  .timeCreated(in.getTimeCreated()).metageneration(in.getMetageneration()).acl(in.getAcl())
                  .defaultObjectAcl(in.getDefaultObjectAcl()).owner(in.getOwner()).location(in.getLocation())
                  .website(in.getWebsite()).logging(in.getLogging()).versioning(in.getVersioning()).cors(in.getCors())
                  .lifeCycle(in.getLifeCycle()).storageClass(in.getStorageClass());
      }

   }

   public static class Owner {

      private final String entity;
      private final String entityId;

      public Owner(String entity, String entityId) {
         this.entity = entity;
         this.entityId = entityId;
      }

      public String getEntity() {
         return entity;
      }

      public String getEntityId() {
         return entityId;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(entity, entityId);
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null || getClass() != obj.getClass())
            return false;
         Owner that = Owner.class.cast(obj);
         return equal(this.entity, that.entity);
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).omitNullValues().add("entiy", entity).add("entityId", entityId);
      }

      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public static class Builder {
         private String entity;
         private String entityId;

         public Builder entity(String entity) {
            this.entity = entity;
            return this;
         }

         public Builder entityId(String entityId) {
            this.entityId = entityId;
            return this;
         }

         public Owner build() {
            return new Owner(this.entity, this.entityId);
         }

         public Builder fromOwner(Owner in) {
            return this.entity(in.getEntity()).entityId(in.getEntityId());
         }

      }

   }

   public static class Website {
      private final String mainPageSuffix;
      private final String notFoundPage;

      public Website(String mainPageSuffix, String notFoundPage) {

         this.mainPageSuffix = mainPageSuffix;
         this.notFoundPage = notFoundPage;
      }

      public String getMainPageSuffix() {
         return mainPageSuffix;
      }

      public String getNotFoundPage() {
         return notFoundPage;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(mainPageSuffix, notFoundPage);
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null || getClass() != obj.getClass())
            return false;
         Website that = Website.class.cast(obj);
         return equal(this.mainPageSuffix, that.mainPageSuffix);
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).add("mainPageSuffix", mainPageSuffix).add("notFoundPage", notFoundPage);
      }

      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public static class Builder {
         private String mainPageSuffix;
         private String notFoundPage;

         public Builder mainPageSuffix(String mainPageSuffix) {
            this.mainPageSuffix = mainPageSuffix;
            return this;
         }

         public Builder notFoundPage(String notFoundPage) {
            this.notFoundPage = notFoundPage;
            return this;
         }

         public Website build() {
            return new Website(this.mainPageSuffix, this.notFoundPage);
         }

         public Builder fromWebsite(Website in) {
            return this.mainPageSuffix(in.getMainPageSuffix()).notFoundPage(in.getNotFoundPage());
         }

      }
   }

   public static class Logging {
      private final String logBucket;
      private final String logObjectPrefix;

      public Logging(String logBucket, String logObjectPrefix) {

         this.logBucket = logBucket;
         this.logObjectPrefix = logObjectPrefix;
      }

      public String getLogBucket() {
         return logBucket;
      }

      public String getLogObjectPrefix() {
         return logObjectPrefix;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(logBucket, logObjectPrefix);
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null || getClass() != obj.getClass())
            return false;
         Logging that = Logging.class.cast(obj);
         return equal(this.logBucket, that.logBucket) && equal(this.logObjectPrefix, that.logObjectPrefix);
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).add("logBucket", logBucket).add("logObjectPrefix", logObjectPrefix);
      }

      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public static class Builder {
         private String logBucket;
         private String logObjectPrefix;

         public Builder logBucket(String logBucket) {
            this.logBucket = logBucket;
            return this;
         }

         public Builder logObjectPrefix(String logObjectPrefix) {
            this.logObjectPrefix = logObjectPrefix;
            return this;
         }

         public Logging build() {
            return new Logging(this.logBucket, this.logObjectPrefix);
         }

         public Builder fromLogging(Logging in) {
            return this.logBucket(in.getLogBucket()).logObjectPrefix(in.getLogObjectPrefix());
         }

      }

   }

   public static class Cors {
      private final Set<String> origin;
      private final Set<String> method;
      private final Set<String> responseHeader;
      private final Integer maxAgeSeconds;

      public Cors(@Nullable Set<String> origin, @Nullable Set<String> method, @Nullable Set<String> responseHeader,
               Integer maxAgeSeconds) {

         this.origin = origin == null ? ImmutableSet.<String> of() : origin;
         this.method = method == null ? ImmutableSet.<String> of() : method;
         this.responseHeader = responseHeader == null ? ImmutableSet.<String> of() : responseHeader;
         this.maxAgeSeconds = maxAgeSeconds;
      }

      public Set<String> getOrigin() {
         return origin;
      }

      public Set<String> getMethod() {
         return method;
      }

      public Set<String> getResponseHeader() {
         return responseHeader;
      }

      public Integer getMaxAgeSeconds() {
         return maxAgeSeconds;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(origin, method, responseHeader, maxAgeSeconds);
      }

      /* TODO -Check equals */
      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null || getClass() != obj.getClass())
            return false;
         Cors that = Cors.class.cast(obj);
         return equal(this.origin, that.origin) && equal(this.method, that.method)
                  && equal(this.responseHeader, that.responseHeader) && equal(this.maxAgeSeconds, that.maxAgeSeconds);
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).add("origin", origin).add("method", method).add("responseHeader", responseHeader)
                  .add("maxAgeSeconds", maxAgeSeconds);
      }

      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public static final class Builder {

         private ImmutableSet.Builder<String> origin = ImmutableSet.builder();
         private ImmutableSet.Builder<String> method = ImmutableSet.builder();
         private ImmutableSet.Builder<String> reponseHeader = ImmutableSet.builder();
         private Integer maxAgeSeconds;

         public Builder addOrigin(String origin) {
            this.origin.add(origin);
            return this;
         }

         public Builder origin(Set<String> origin) {
            this.origin.addAll(origin);
            return this;
         }

         public Builder addMethod(String method) {
            this.method.add(method);
            return this;
         }

         public Builder method(Set<String> method) {
            this.method.addAll(method);
            return this;
         }

         public Builder addResponseHeader(String responseHeader) {
            this.reponseHeader.add(responseHeader);
            return this;
         }

         public Builder responseHeader(Set<String> responseHeader) {
            this.reponseHeader.addAll(responseHeader);
            return this;
         }

         public Builder maxAgeSeconds(Integer maxAgeSeconds) {
            this.maxAgeSeconds = maxAgeSeconds;
            return this;
         }

         public Cors build() {
            return new Cors(this.origin.build(), this.method.build(), this.reponseHeader.build(), this.maxAgeSeconds);
         }

         public Builder fromCors(Cors c) {
            return this.maxAgeSeconds(c.getMaxAgeSeconds()).origin(c.getOrigin()).method(c.getMethod())
                     .responseHeader(c.getResponseHeader());
         }

      }

   }

   public static class Versioning {
      private final Boolean enabled;

      public Versioning(Boolean enabled) {
         this.enabled = enabled;
      }

      public Boolean isEnabled() {
         return enabled;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(enabled);
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         Versioning other = (Versioning) obj;
         if (enabled == null) {
            if (other.enabled != null)
               return false;
         } else if (!enabled.equals(other.enabled))
            return false;
         return true;
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).add("enabled", enabled);
      }

      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public static class Builder {

         private Boolean enabled;

         public Builder enalbled(Boolean enabled) {
            this.enabled = enabled;
            return this;
         }

         public Versioning build() {
            return new Versioning(this.enabled);
         }

         public Builder fromVersioning(Versioning in) {
            return this.enalbled(in.isEnabled());
         }

      }

   }

   public static class LifeCycle {

      private final Set<Rule> rule;

      public LifeCycle(Set<Rule> rule) {
         this.rule = rule == null ? ImmutableSet.<Rule> of() : rule;
      }

      public Set<Rule> getRule() {
         return rule;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(rule);
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         LifeCycle other = (LifeCycle) obj;
         if (rule == null) {
            if (other.rule != null)
               return false;
         } else if (!rule.equals(other.rule))
            return false;
         return true;
      }

      protected Objects.ToStringHelper string() {
         return toStringHelper(this).add("rule", rule);
      }

      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public static class Builder {
         ImmutableSet.Builder<Rule> rule = ImmutableSet.builder();

         public Builder addRule(Rule rule) {
            this.rule.add(rule);
            return this;
         }

         public Builder rule(Set<Rule> rule) {
            this.rule.addAll(rule);
            return this;
         }

         public LifeCycle build() {
            return new LifeCycle(this.rule.build());
         }

         public Builder fromLifeCycle(LifeCycle in) {
            return this.rule(in.getRule());
         }

      }

      public static class Rule {
         private final Action action;
         private final Condition condition;

         public Rule(Action action, Condition condition) {
            this.action = action;
            this.condition = condition;
         }

         public Action getAction() {
            return action;
         }

         public Condition getCondition() {
            return condition;
         }

         @Override
         public int hashCode() {
            return Objects.hashCode(action, condition);
         }

         @Override
         public boolean equals(Object obj) {
            if (this == obj)
               return true;
            if (obj == null)
               return false;
            if (getClass() != obj.getClass())
               return false;
            Rule other = (Rule) obj;
            if (action == null) {
               if (other.action != null)
                  return false;
            } else if (!action.equals(other.action))
               return false;
            if (condition == null) {
               if (other.condition != null)
                  return false;
            } else if (!condition.equals(other.condition))
               return false;
            return true;
         }

         protected Objects.ToStringHelper string() {
            return toStringHelper(this).add("condition", condition).add("action", action);
         }

         @Override
         public String toString() {
            return string().toString();
         }

         public static Builder builder() {
            return new Builder();
         }

         public static class Builder {
            private Action action;
            private Condition condition;

            public Builder action(Action action) {
               this.action = action;
               return this;
            }

            public Builder condtion(Condition condition) {
               this.condition = condition;
               return this;
            }

            public Rule build() {
               return new Rule(this.action, this.condition);
            }

            public Builder fromRule(Rule in) {
               return this.action(in.getAction()).condtion(in.getCondition());

            }

         }

         public static class Action {
            private final String type;

            public Action(String type) {
               this.type = type;
            }

            public String getType() {
               return type;
            }

            @Override
            public int hashCode() {
               return Objects.hashCode(type);
            }

            @Override
            public boolean equals(Object obj) {
               if (this == obj)
                  return true;
               if (obj == null)
                  return false;
               if (getClass() != obj.getClass())
                  return false;
               Action other = (Action) obj;
               if (type == null) {
                  if (other.type != null)
                     return false;
               } else if (!type.equals(other.type))
                  return false;
               return true;
            }

            protected Objects.ToStringHelper string() {
               return toStringHelper(this).add("type", type);
            }

            @Override
            public String toString() {
               return string().toString();
            }

            public static Builder builder() {
               return new Builder();
            }

            public static class Builder {
               private String type;

               public Builder type(String type) {
                  this.type = type;
                  return this;
               }

               public Action build() {
                  return new Action(this.type);
               }

               public Builder fromAction(Action in) {
                  return this.type(in.getType());

               }

            }

         }

         public static final class Condition {
            private final Integer age;
            private final Date createdBefore;
            private final Boolean isLive;
            private final Integer numNewerVersions;

            public Condition(Integer age, Date createdBefore, Boolean isLive, Integer numNewerVersions) {
               this.age = age;
               this.createdBefore = createdBefore;
               this.isLive = isLive;
               this.numNewerVersions = numNewerVersions;
            }

            public Integer getAge() {
               return age;
            }

            public Date getCreatedBefore() {
               return createdBefore;
            }

            public Boolean getIsLive() {
               return isLive;
            }

            public Integer getNumNewerVersions() {
               return numNewerVersions;
            }

            @Override
            public int hashCode() {
               return Objects.hashCode(age, createdBefore, isLive, numNewerVersions);
            }

            @Override
            public boolean equals(Object obj) {
               if (this == obj)
                  return true;
               if (obj == null)
                  return false;
               if (getClass() != obj.getClass())
                  return false;
               Condition other = (Condition) obj;
               if (age == null) {
                  if (other.age != null)
                     return false;
               } else if (!age.equals(other.age))
                  return false;
               if (createdBefore == null) {
                  if (other.createdBefore != null)
                     return false;
               } else if (!createdBefore.equals(other.createdBefore))
                  return false;
               if (isLive == null) {
                  if (other.isLive != null)
                     return false;
               } else if (!isLive.equals(other.isLive))
                  return false;
               if (numNewerVersions == null) {
                  if (other.numNewerVersions != null)
                     return false;
               } else if (!numNewerVersions.equals(other.numNewerVersions))
                  return false;
               return true;
            }

            protected Objects.ToStringHelper string() {
               return toStringHelper(this).add("age", age).add("createdBefore", createdBefore).add("isLive", isLive)
                        .add("numNewerVersions", numNewerVersions);
            }

            @Override
            public String toString() {
               return string().toString();
            }

            public static Builder builder() {
               return new Builder();
            }

            public static final class Builder {

               private Integer age;
               private Date createdBefore;
               private Boolean isLive;
               private Integer numNewerVersions;

               public Builder age(Integer age) {
                  this.age = age;
                  return this;
               }

               public Builder createdBefore(Date createdBefore) {
                  this.createdBefore = createdBefore;
                  return this;
               }

               public Builder isLive(Boolean isLive) {
                  this.isLive = isLive;
                  return this;
               }

               public Builder numNewerVersions(Integer numNewerVersions) {
                  this.numNewerVersions = numNewerVersions;
                  return this;
               }

               public Condition build() {
                  return new Condition(this.age, this.createdBefore, this.isLive, this.numNewerVersions);
               }

               public Builder fromCondition(Condition in) {
                  return this.age(in.getAge()).createdBefore(in.getCreatedBefore()).isLive(in.getIsLive())
                           .numNewerVersions(in.getNumNewerVersions());
               }

            }

         }
      }
   }
}
