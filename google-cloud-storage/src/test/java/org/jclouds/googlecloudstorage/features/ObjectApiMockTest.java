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

import static org.jclouds.io.Payloads.newByteSourcePayload;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;
import static org.jclouds.Constants.PROPERTY_SO_TIMEOUT;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

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
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.StorageClass;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.domain.internal.Owner;

public class ObjectApiMockTest {
   SimpleDateFormatDateService dates = new SimpleDateFormatDateService();

   static final Payload PAYLOAD = newByteSourcePayload(ByteSource.wrap("googlecloudstorage".getBytes()));

   private static final String BUCKET_NAME = "mycontainer";

   protected static final String TOKEN = "1/8xbJqaOZXSUZbHLl5EOtu1pxz3fmmetKx9W8CV4t79M";
   private MockWebServer server;
   private GoogleCloudStorageApi api;
   private static final Set<Module> modules = ImmutableSet.<Module> of(new ExecutorServiceModule(
            newDirectExecutorService(), newDirectExecutorService()));

   private static GoogleCloudStorageApi getGoogleCloudStorageApi(URL server) {
      Properties overrides = new Properties();
      overrides.put(OAuthProperties.SIGNATURE_OR_MAC_ALGORITHM, OAuthConstants.NO_ALGORITHM);
      overrides.setProperty(PROPERTY_SO_TIMEOUT, "0");
      overrides.setProperty(PROPERTY_MAX_RETRIES, "1");
      return ContextBuilder.newBuilder("google-cloud-storage").credentials("accessKey", "secretKey")
               .endpoint(server.toString()).modules(modules).overrides(overrides).buildApi(GoogleCloudStorageApi.class);

   }

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
      mr.addHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=0, must-revalidate, no-transform");
      mr.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
      mr.addHeader(HttpHeaders.CONTENT_LENGTH, "1123");
      mr.addHeader(HttpHeaders.DATE, "Thu, 21 Aug 2014 14:06:23 GMT");
      mr.addHeader(HttpHeaders.EXPIRES, "Thu, 21 Aug 2014 14:06:23 GMT");
      mr.addHeader(HttpHeaders.SERVER, "GSE");
      return mr;
   }

   private static MockWebServer enqueAuthResponse(MockWebServer server) {
      server.enqueue(new MockResponse().setResponseCode(200).setBody(
               "{\n" + "  \"access_token\" : \"" + TOKEN + "\",\n" + "  \"token_type\" : \"Bearer\",\n"
                        + "  \"expires_in\" : 3600\n" + "}"));
      return server;
   }

   // Test Object list

   protected ListPage<GCSObject> parsedObjectsForUrl(String baseUri) {

      ImmutableList<GCSObject> items = ImmutableList
               .of(GCSObject
                        .builder()
                        .id("bhashbucket/const.txt/1408527552052000")
                        .selfLink(URI.create("https://www.googleapis.com/storage/v1/b/bhashbucket/o/const.txt"))
                        .name("const.txt")
                        .bucket("bhashbucket")
                        .generation(1408527552052000L)
                        .metageneration(1L)
                        .contentType(MediaType.TEXT_PLAIN)
                        .updated(dates.iso8601DateParse("2014-08-20T09:39:12.052Z"))
                        .storageClass(StorageClass.STANDARD)
                        .size(13670754L)
                        .md5Hash("h04Bp0U0hYaYs0hbDgHw9g==")
                        .mediaLink(
                                 URI.create("https://www.googleapis.com/download/storage/v1/b/bhashbucket/o/const.txt?generation=1408527552052000&alt=media"))
                        .owner(Owner.builder()
                                 .entity("user-00b4903a979105b8b299d9412b18493c177a8b1150e4c30382f473470371729f")
                                 .entityId("00b4903a979105b8b299d9412b18493c177a8b1150e4c30382f473470371729f").build())
                        .crc32c("9zLoRA==").etag("CKD2lYPGocACEAE=").build(),

                        GCSObject.builder()

                                 .id("bhashbucket/file/1408100153379000")
                                 .name("file")
                                 .selfLink(URI.create("https://www.googleapis.com/storage/v1/b/bhashbucket/o/file"))
                                 .bucket("bhashbucket")
                                 .generation(1408100153379000L)
                                 .metageneration(5L)
                                 .contentType(MediaType.APPLICATION_XML)
                                 .updated(dates.iso8601DateParse("2014-08-15T10:55:53.379Z"))
                                 .storageClass(StorageClass.STANDARD)
                                 .size(13670754L)
                                 .contentEncoding("en")
                                 .contentDisposition("attachment")
                                 .contentLanguage("en")
                                 .md5Hash("2tqh0M02BdGIyym3n9JAFA==")
                                 .mediaLink(
                                          URI.create("https://www.googleapis.com/download/storage/v1/b/bhashbucket/o/file?generation=1408100153379000&alt=media"))
                                 .owner(Owner
                                          .builder()
                                          .entity("user-00b4903a979105b8b299d9412b18493c177a8b1150e4c30382f473470371729f")
                                          .entityId("00b4903a979105b8b299d9412b18493c177a8b1150e4c30382f473470371729f")
                                          .build()).crc32c("h1k2+Q==").etag("CLiRzeuNlcACEAU=").build());

      Set<String> prefixes = ImmutableSet.<String> of();

      ListPage<GCSObject> list = ListPage.<GCSObject> builder().items(items).prefixes(prefixes).kind(Kind.OBJECTS)
               .build();

      return list;
   }

   @Test(singleThreaded = true, groups = { "mock" })
   public void testList() throws Exception {

      MockWebServer server = new MockWebServer();
      enqueAuthResponse(server);
      MockResponse response = buildBaseResponse(200);
      response.setBody(stringFromResource("/list_object.json"));
      server.enqueue(response);

      ListPage<GCSObject> objects = api.getObjectApi().listObjects(BUCKET_NAME);

      assertEquals(objects, parsedObjectsForUrl(server.getUrl("/").toString()));
      assertEquals(objects.iterator().next().getBucket(), BUCKET_NAME);

      assertEquals(server.getRequestCount(), 2);
      assertAuthentication(server);
      assertRequest(server.takeRequest(), "GET", server.getUrl("/").toString() + "/storage/v1/b/" + BUCKET_NAME + "/o");
   }

   //
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