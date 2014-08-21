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
package org.jclouds.googlecloudstorage.utils;

import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class GoogleCloudStorageUtils {
   
   private static final Pattern BUCKET_NAME_PATTERN = Pattern.compile("^[a-z0-9][-_.a-z0-9]+");
   private static final Pattern IP_PATTERN = Pattern.compile("b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).)"
         + "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)b");

   public static String validateBucketName(String bucketName) {
      checkNotNull(bucketName, "bucketName");
      checkArgument(
            BUCKET_NAME_PATTERN.matcher(bucketName).matches(),
            "bucketName name must start with a number or letter and  can only contain lowercase letters, numbers, periods (.), underscores (_), and dashes (-)");
     if( !bucketName.contains(".")){
        checkArgument(bucketName.length() > 2 && bucketName.length() < 63,
                 "bucketName name must be between 3 and 63 characters long");        
     }else{
        checkArgument(bucketName.length()< 222,
                 "bucketName name contatining dots must be less than 222 characters long");      

       String [] components = bucketName.split(".");
       for(String comp : components){
          checkArgument(comp.length() < 63,
                   "each component of bucketName name must be less than 63 characters long"); 
          
       }
     }
      checkArgument(!IP_PATTERN.matcher(bucketName).matches(), "bucketName name cannot be ip address style");
      return bucketName;
   }

}
