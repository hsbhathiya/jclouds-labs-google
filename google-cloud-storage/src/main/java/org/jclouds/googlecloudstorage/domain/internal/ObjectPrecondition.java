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

import com.google.common.base.Objects;

/**
 * This is an internal Object used in ComposeObjectTemplate/SourceObject.
 */

public class ObjectPrecondition {
   private final Long ifGenerationMatch;

   private ObjectPrecondition(Long ifGenerationMatch) {
      this.ifGenerationMatch = ifGenerationMatch;
   }

   public Long getIfGenerationMatch() {
      return ifGenerationMatch;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(ifGenerationMatch);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ObjectPrecondition other = (ObjectPrecondition) obj;
      if (ifGenerationMatch == null) {
         if (other.ifGenerationMatch != null)
            return false;
      } else if (!ifGenerationMatch.equals(other.ifGenerationMatch))
         return false;
      return true;
   }

   protected Objects.ToStringHelper string() {
      return toStringHelper(this).add("ifGenerationMatch", ifGenerationMatch);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {
      private Long ifGenerationMatch;

      public Builder ifGenerationMatch(Long ifGenerationMatch) {
         this.ifGenerationMatch = ifGenerationMatch;
         return this;
      }

      public ObjectPrecondition build() {
         return new ObjectPrecondition(this.ifGenerationMatch);
      }

      public Builder fromObjectPrecondition(ObjectPrecondition in) {
         return this.ifGenerationMatch(in.getIfGenerationMatch());
      }

   }
}
