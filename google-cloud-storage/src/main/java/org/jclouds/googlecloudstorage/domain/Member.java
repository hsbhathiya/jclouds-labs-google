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

/**
 * In Google Cloud Storage each Bucket is associated with a unique project.Project consist of set of users or
 * "Members".Members are identified by their email address associated with google account
 * <p/>
 * 
 * @author Bhathiya Supun
 */
public class Member {
   private String emailAddress;
   
   public Member(String emailAddress) {
      this.emailAddress = emailAddress;
   }
   
   public String getEmailAddress() {
      return emailAddress;
   }
   
   public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Member other = (Member) obj;
      if (emailAddress == null) {
         if (other.emailAddress != null)
            return false;
      } else if (!emailAddress.equals(other.emailAddress))
         return false;
      return true;
   }
   
}
