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

package org.jclouds.googlecloudstorage.features;

import static org.jclouds.googlecloudstorage.reference.GoogleCloudStorageConstants.STORAGE_FULLCONTROL_SCOPE;
import static org.jclouds.io.Payloads.newByteSourcePayload;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;
import static org.jclouds.Constants.PROPERTY_SO_TIMEOUT;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.jclouds.ContextBuilder;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.io.Payload;
import org.jclouds.oauth.v2.OAuthConstants;
import org.jclouds.oauth.v2.config.OAuthProperties;
import org.jclouds.util.Strings2;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSource;
import com.google.common.net.HttpHeaders;
import com.google.inject.Module;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import com.squareup.okhttp.mockwebserver.RecordedRequest;

import javax.ws.rs.core.MediaType;

import org.jclouds.googlecloudstorage.GoogleCloudStorageApi;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;

public class ObjectApiMockTest {
    SimpleDateFormatDateService dates = new SimpleDateFormatDateService();

   private static final Pattern urlTokenPattern = Pattern.compile(":\\s*\"\\s*URL");

   static final Payload PAYLOAD = newByteSourcePayload(ByteSource.wrap("googlecloudstorage".getBytes()));

   protected ListPage<GCSObject> parsedObjectsForUrl(String baseUri) {
      baseUri += "v1/MossoCloudFS_5bcf396e-39dd-45ff-93a1-712b9aba90a9/myContainer";
      
      
      ImmutableList<GCSObject> items =  ImmutableList.of(
               GCSObject.builder().name("test_obj_1").selfLink(URI.create(baseUri + "/test_obj_1"))
                        .etag("4281c348eaf83e70ddce0e07221c3d28")
                        .updated(dates.iso8601DateParse("2009-02-03T05:26:32.612278")).build(),
               GCSObject.builder().name("test_obj_2").selfLink(URI.create(baseUri + "/test_obj_2"))
                        .contentType(MediaType.APPLICATION_OCTET_STREAM_TYPE.toString())
                        .etag("b039efe731ad111bc1b0ef221c3849d0")
                        .updated(dates.iso8601DateParse("2009-02-03T05:26:32.612278")).build(),
               GCSObject.builder().name("test obj 3").selfLink(URI.create(baseUri + "/test%20obj%203"))
                        .etag("0b2e80bd0744d9ebb20484149a57c82e")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM_TYPE.toString())
                        .updated(dates.iso8601DateParse("2014-05-20T05:26:32.612278")).build());
      
      Set<String> prefixes =  ImmutableSet.<String>of();
      
      ListPage<GCSObject> list = ListPage.<GCSObject>builder().items(items).prefixes(prefixes).build();
      
      return list;
  
   }
   
   protected static final String TOKEN = "1/8xbJqaOZXSUZbHLl5EOtu1pxz3fmmetKx9W8CV4t79M";
   private MockWebServer server;
   private GoogleCloudStorageApi api;
   private static final Set<Module> modules = ImmutableSet.<Module> of(new ExecutorServiceModule(newDirectExecutorService(), newDirectExecutorService()));

   private static GoogleCloudStorageApi getGoogleCloudStorageApi(URL server){
      Properties overrides = new Properties();
      overrides.put(OAuthProperties.SIGNATURE_OR_MAC_ALGORITHM, OAuthConstants.NO_ALGORITHM);
      overrides.setProperty(PROPERTY_SO_TIMEOUT, "0");
      overrides.setProperty(PROPERTY_MAX_RETRIES, "1");
      return ContextBuilder.newBuilder("google-cloud-storage").credentials("accessKey", "secretKey").endpoint(server.toString())
            .modules(modules).overrides(overrides).buildApi(GoogleCloudStorageApi.class);
      
   }
   
   /*public GoogleCloudStorageApi api(String uri, String provider, Properties overrides) {
      if (!overrides.containsKey(PROPERTY_MAX_RETRIES)) {
         overrides.setProperty(PROPERTY_MAX_RETRIES, "1");
      }
       overrides.put(OAuthProperties.SIGNATURE_OR_MAC_ALGORITHM, OAuthConstants.NO_ALGORITHM);
      
      return ContextBuilder.newBuilder(provider)               
            .credentials("skip", "skip")
            .endpoint(uri)
            .overrides(overrides)
            .modules(modules)
            .buildApi(GoogleCloudStorageApi.class);
   }

   public GoogleCloudStorageApi api(String uri, String provider) {
      return api(uri, provider, new Properties());
   }*/

   
   @BeforeMethod
   private void initServer() throws IOException {
      server = new MockWebServer();
      server.play();
      api = getGoogleCloudStorageApi(server.getUrl("/"));
   }

   @AfterMethod
   private void shutdownServer() throws IOException {
      server.shutdown();
   }
   
   private static MockResponse buildBaseResponse(int responseCode) {
      MockResponse mr = new MockResponse();
      mr.setResponseCode(responseCode);
      mr.addHeader(GlacierHeaders.REQUEST_ID, REQUEST_ID);
      mr.addHeader(HttpHeaders.DATE, DATE);
      return mr;
   }
   
   
   
   @Test(singleThreaded = true, groups = {"mock"})
   public void testList() throws Exception {
      MockWebServer  server = new MockWebServer();
     // server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n" + "  \"access_token\" : \"" + TOKEN + "\",\n"
    //           + "  \"token_type\" : \"Bearer\",\n" + "  \"expires_in\" : 3600\n" + "}"));
    //  server.enqueue(addCommonHeaders(new MockResponse().setBody(stringFromResource("/"))));
       
      ListPage<GCSObject> objects =  api.getObjectApi().listObjects("mycontainer");

      assertEquals(objects, parsedObjectsForUrl(server.getUrl("/").toString()));
      assertEquals(objects.iterator().next().getBucket(), "mycontainer");

      assertEquals(server.getRequestCount(), 2);
      assertAuthentication(server);
      assertRequest(server.takeRequest(), "GET",
               "https://www.googleapis.com/storage/v1/b/mycontainer/o");
   }
   
   
   public String stringFromResource(String resourceName) {
      try {
         return Strings2.toStringAndClose(getClass().getResourceAsStream(resourceName));
      } catch (IOException e) {
         throw Throwables.propagate(e);
      }
   }

   public MockResponse addCommonHeaders(MockResponse mockResponse) {
      mockResponse.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
      return mockResponse;
   }
   
   public void assertRequest(RecordedRequest request, String method, String path) {
      assertEquals(request.getMethod(), method);
      assertEquals(request.getPath(), path);
   }
   
   public void assertAuthentication(MockWebServer server) {
      assertTrue(server.getRequestCount() >= 1);
      try {
         assertEquals(server.takeRequest().getRequestLine(), "POST /tokens HTTP/1.1");
      } catch (InterruptedException e) {
         Throwables.propagate(e);
      }
   }

}