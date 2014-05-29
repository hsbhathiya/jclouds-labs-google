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
package org.jclouds.googlecloudstorage.config;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Singleton;

import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.ListBucketAccessControls;
import org.jclouds.json.config.GsonModule.DateAdapter;
import org.jclouds.json.config.GsonModule.Iso8601DateAdapter;
import org.jclouds.oauth.v2.domain.ClaimSet;
import org.jclouds.oauth.v2.domain.Header;
import org.jclouds.oauth.v2.json.ClaimSetTypeAdapter;
import org.jclouds.oauth.v2.json.HeaderTypeAdapter;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author Bhathiya Supun
 */
public class GoogleCloudStorageParserModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(DateAdapter.class).to(Iso8601DateAdapter.class);
   }

   @Provides
   @Singleton
   public Map<Type, Object> provideCustomAdapterBindings() {
      return new ImmutableMap.Builder<Type, Object>()
            .put(Header.class, new HeaderTypeAdapter())
            .put(ClaimSet.class, new ClaimSetTypeAdapter())
            .build();
   }

}
