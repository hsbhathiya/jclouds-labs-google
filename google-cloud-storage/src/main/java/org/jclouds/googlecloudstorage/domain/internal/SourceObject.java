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
package org.jclouds.googlecloudstorage.domain.internal;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public class SourceObject {
   private final String name;
   private final Long generation;
   private final ObjectPrecondition objectPreconditions;

   private SourceObject(String name, Long generation, ObjectPrecondition objectPreconditions) {
      this.name = checkNotNull(name, "name");
      this.generation = checkNotNull(generation, "generation");
      this.objectPreconditions = objectPreconditions;
   }

   public String getName() {
      return name;
   }

   public Long getGeneration() {
      return generation;
   }

   public ObjectPrecondition getObjectPreconditions() {
      return objectPreconditions;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(name, generation, objectPreconditions);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      SourceObject other = (SourceObject) obj;
      if (generation == null) {
         if (other.generation != null)
            return false;
      } else if (!generation.equals(other.generation))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (objectPreconditions == null) {
         if (other.objectPreconditions != null)
            return false;
      } else if (!objectPreconditions.equals(other.objectPreconditions))
         return false;
      return true;
   }

   protected Objects.ToStringHelper string() {
      return toStringHelper(this).add("name", name).add("generation", generation)
               .add("objectPreconditions", objectPreconditions);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {
      private String name;
      private Long generation;
      private ObjectPrecondition objectPreconditions;

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder generation(Long generation) {
         this.generation = generation;
         return this;
      }

      public Builder objectPreconditions(ObjectPrecondition objectPreconditions) {
         this.objectPreconditions = objectPreconditions;
         return this;
      }

      public SourceObject build() {
         return new SourceObject(this.name, this.generation, this.objectPreconditions);
      }

      public Builder fromSourceObject(SourceObject in) {
         return this.name(in.name).generation(in.getGeneration());

      }

   }
}
