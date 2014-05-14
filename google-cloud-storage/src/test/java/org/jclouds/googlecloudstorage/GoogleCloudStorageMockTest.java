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
package org.jclouds.googlecloudstorage;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;
import static org.jclouds.Constants.PROPERTY_SO_TIMEOUT;


import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.testng.annotations.Test;


import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * 
 * @author Bhathiya Supun
 */
@Test(singleThreaded = true)
public class GoogleCloudStorageMockTest {

   private static final Set<Module> modules = ImmutableSet.<Module> of(
         new ExecutorServiceModule(sameThreadExecutor(), sameThreadExecutor()));

   static GoogleCloudStorageApi  getGoogleCloudStorageApi(URL server) {
      Properties overrides = new Properties();
      
      overrides.setProperty(PROPERTY_SO_TIMEOUT, "0");
      overrides.setProperty(PROPERTY_MAX_RETRIES, "1");
      return ContextBuilder.newBuilder("google-cloud-storage")
                           .credentials("accessKey", "secretKey")
                           .endpoint(server.toString())
                           .modules(modules)
                           .overrides(overrides)
                           .buildApi(GoogleCloudStorageApi.class);
   }



 
}
