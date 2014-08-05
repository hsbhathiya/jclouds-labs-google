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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.DeliveryType;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.DestinationPredefinedAcl;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Projection;
import org.jclouds.googlecloudstorage.domain.Bucket;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectChangeNotification;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.domain.templates.BucketTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ComposeObjectTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ObjectChangeNotificationTemplate;
import org.jclouds.googlecloudstorage.domain.templates.ObjectTemplate;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.googlecloudstorage.options.ComposeObjectOptions;
import org.jclouds.googlecloudstorage.options.CopyObjectOptions;
import org.jclouds.googlecloudstorage.options.DeleteObjectOptions;
import org.jclouds.googlecloudstorage.options.GetObjectOptions;
import org.jclouds.googlecloudstorage.options.InsertObjectOptions;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.jclouds.googlecloudstorage.options.UpdateObjectOptions;
import org.jclouds.googlecloudstorage.reference.Crc32c;
import org.jclouds.http.internal.PayloadEnclosingImpl;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.ByteSourcePayload;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Sets;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class ObjectApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   private static final String BUCKET_NAME = "jcloudsobjectoperations" + UUID.randomUUID();
   private static final String BUCKET_NAME2 = "jcloudobjectdestination" + UUID.randomUUID();
   private static final String UPLOAD_OBJECT_NAME = "objectOperation.txt";
   private static final String UPLOAD_OBJECT_NAME2 = "jcloudslogo.jpg";
   private static final String COPIED_OBJECT_NAME = "copyofObjectOperation.txt";
   private static final String COMPOSED_OBJECT = "ComposedObject1.txt";
   private static final String COMPOSED_OBJECT2 = "ComposedObject2.json";
   private static final String BUCKET_NAMEX = "jcloudobjectdestination";

   private Long RANDOM_LONG = 100L;

   private Long metageneration;
   private Long generation;
   private HashCode hc;

   private ObjectApi api() {
      return api.getObjectApi();
   }

   // Create the buckets
   @BeforeClass
   private void createBucket() {
      BucketTemplate template = new BucketTemplate().name(BUCKET_NAME);
      Bucket bucket = api.getBucketApi().createBucket(PROJECT_NUMBER, template);
      assertNotNull(bucket);

      BucketTemplate template2 = new BucketTemplate().name(BUCKET_NAME2);
      Bucket bucket2 = api.getBucketApi().createBucket(PROJECT_NUMBER, template2);
      assertNotNull(bucket2);
   }

   // Enable ObjectChangeNotifiactions for the buckets
/*   @Test(groups = "live")
   public void testWatchAllObjects() {

      String address = "";
      String id = "";
      String resourceId = "";
      String resourceUri = "";
      String token = "";
      Long expiration = new Long(100);

      ObjectChangeNotificationTemplate template = new ObjectChangeNotificationTemplate().id(id)
               .address(URI.create(address)).resourceId(resourceId).resourceUri(URI.create(resourceUri))
               .type(DeliveryType.WEB_HOOK).expiration(expiration).payload(true).token(token);

      ListObjectOptions options = new ListObjectOptions().maxResults(2);

      ObjectChangeNotification notify = api.getObjectChangeNNotificationApi().watchAllObjects(BUCKET_NAME, template,
               options);

      assertNotNull(notify);
      assertEquals(template.getPayload(), Boolean.TRUE);
      assertEquals(template.getToken(), token);
      assertEquals(template.getId(), id);
      assertEquals(template.getExpiration(), expiration);
      assertEquals(template.getResourceUri().toString(), resourceUri);
   }

   @Test(groups = "live")
   public void testWatchAllObjectsWithOptions() {

      String address = "";
      String id = "";
      String resourceId = "";
      String resourceUri = "";
      String token = "";
      Long expiration = new Long(100);

      ObjectChangeNotificationTemplate template = new ObjectChangeNotificationTemplate().id(id)
               .address(URI.create(address)).resourceId(resourceId).resourceUri(URI.create(resourceUri))
               .type(DeliveryType.WEB_HOOK).expiration(expiration).payload(true).token(token);

      ListObjectOptions options = new ListObjectOptions().maxResults(2);

      ObjectChangeNotification notify = api.getObjectChangeNNotificationApi().watchAllObjects(BUCKET_NAME, template,
               options);

      assertNotNull(notify);
      assertEquals(template.getPayload(), Boolean.TRUE);
      assertEquals(template.getToken(), token);
      assertEquals(template.getId(), id);
      assertEquals(template.getExpiration(), expiration);
      assertEquals(template.getResourceUri().toString(), resourceUri);
   }*/

   // Object Operations
   @Test(groups = "live")
   public void testSimpleUpload() throws IOException {
      String data = "Payload data";

      PayloadEnclosingImpl payload = new PayloadEnclosingImpl();
      payload.setPayload(data);

      InsertObjectOptions options = new InsertObjectOptions().name(UPLOAD_OBJECT_NAME);

      GCSObject gcsObject = api().simpleUpload(BUCKET_NAME, "text/plain",
               payload.getPayload().getContentMetadata().getContentLength() + "", payload.getPayload(), options);

      assertNotNull(gcsObject);
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME);

   }

   @Test(groups = "live", dependsOnMethods = "testSimpleUpload")
   public void testSimpleJpegUpload() throws IOException {
      ByteSource byteSource = Files.asByteSource(new File(Resources.getResource(getClass(), "/" + UPLOAD_OBJECT_NAME2)
               .getPath()));

      ByteSourcePayload payload = Payloads.newByteSourcePayload(byteSource);
      InsertObjectOptions options = new InsertObjectOptions().name(UPLOAD_OBJECT_NAME2);

      GCSObject gcsObject = api().simpleUpload(BUCKET_NAME, "image/jpeg", byteSource.read().length + "", payload,
               options);

      assertNotNull(gcsObject);
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME2);

      // This is a client side validation of md5
      // Md5 Hash
      HashFunction hf = Hashing.md5();
      hc = hf.newHasher().putBytes(byteSource.read()).hash();
      String md5 = BaseEncoding.base64().encode(hc.asBytes());

      assertEquals(gcsObject.getMd5Hash(), md5); // Assertion works

      // crc32c validation
      Crc32c crc32c = new Crc32c();
      crc32c.update(byteSource.read(), 0, byteSource.read().length);
      byte[] bArray = crc32c.getValueAsBytes(); // Longs.toByteArray(value)
      String encodedCrc = new String(BaseEncoding.base64().encode(bArray));       assertEquals(gcsObject.getCrc32c(), encodedCrc); //Assertion fails
   }

   @Test(groups = "live", dependsOnMethods = "testSimpleUpload")
   public void testMultipartJpegUpload() throws IOException {
      ByteSource byteSource = Files.asByteSource(new File(Resources.getResource(getClass(), "/" + UPLOAD_OBJECT_NAME2)
               .getPath()));

      ByteSourcePayload payload = Payloads.newByteSourcePayload(byteSource);
      
      ObjectTemplate template = new ObjectTemplate();

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAMEX).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      // This would trigger server side validation of md5     
      HashFunction hf = Hashing.md5();
      hc = hf.newHasher().putBytes(byteSource.read()).hash();
      String md5 = BaseEncoding.base64().encode(hc.asBytes());

      template.contentType("image/jpeg").addAcl(oacl).size(Long.valueOf(byteSource.read().length + ""))
               .name(UPLOAD_OBJECT_NAME2).contentLanguage("en").contentDisposition("attachment").md5Hash(hc)
               .customMetadata("cutomMetaKey", "cutomMetaValue");

      GCSObject gcsObject = api().multipartUpload(BUCKET_NAMEX, template, payload);

      assertNotNull(gcsObject);
      assertEquals(gcsObject.getBucket(), BUCKET_NAMEX);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME2);
      assertNotNull(gcsObject.getAllMetadata());
      assertEquals(gcsObject.getAllMetadata().get("cutomMetaKey"), "cutomMetaValue");

      assertEquals(gcsObject.getMd5Hash(), md5); // Assertion works
   }

   @Test(groups = "live", dependsOnMethods = "testSimpleUpload")
   public void testGetObject() {
      GCSObject gcsObject = api().getObject(BUCKET_NAME, UPLOAD_OBJECT_NAME);

      assertNotNull(gcsObject);

      metageneration = gcsObject.getMetageneration();
      generation = gcsObject.getGeneration();

      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME);
      assertEquals(gcsObject.getContentType(), "text/plain");
   }

   @Test(groups = "live", dependsOnMethods = "testGetObject")
   public void testGetObjectWithOptions() {
      GetObjectOptions options = new GetObjectOptions().ifGenerationMatch(generation)
               .ifMetagenerationMatch(metageneration).ifGenerationNotMatch(generation + 1).projection(Projection.FULL);

      GCSObject gcsObject = api().getObject(BUCKET_NAME, UPLOAD_OBJECT_NAME, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME);
      assertEquals(gcsObject.getContentType(), "text/plain");

   }

   @Test(groups = "live", dependsOnMethods = "testGetObject")
   public void testCopyObject() {
      GCSObject gcsObject = api().copyObject(BUCKET_NAME2, COPIED_OBJECT_NAME, BUCKET_NAME, UPLOAD_OBJECT_NAME);

      assertNotNull(gcsObject);
      assertEquals(gcsObject.getBucket(), BUCKET_NAME2);
      assertEquals(gcsObject.getName(), COPIED_OBJECT_NAME);
      assertEquals(gcsObject.getContentType(), "text/plain");

   }

   @Test(groups = "live", dependsOnMethods = "testCopyObject")
   public void testCopyObjectWithOptions() {
      CopyObjectOptions options = new CopyObjectOptions().ifSourceGenerationMatch(generation)
               .ifSourceMetagenerationMatch(metageneration).projection(Projection.FULL);

      GCSObject gcsObject = api()
               .copyObject(BUCKET_NAME2, UPLOAD_OBJECT_NAME, BUCKET_NAME, UPLOAD_OBJECT_NAME, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME2);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME);
      assertEquals(gcsObject.getContentType(), "text/plain");
   }

   @Test(groups = "live", dependsOnMethods = "testCopyObjectWithOptions")
   public void testComposeObject() {
      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ObjectTemplate destination = new ObjectTemplate().contentType("text/plain").addAcl(oacl);
      Set<GCSObject> sourceList = Sets.newHashSet();
      sourceList.add(api().getObject(BUCKET_NAME2, UPLOAD_OBJECT_NAME));
      sourceList.add(api().getObject(BUCKET_NAME2, COPIED_OBJECT_NAME));

      ComposeObjectTemplate requestTemplate = new ComposeObjectTemplate().sourceObjects(sourceList).destination(
               destination);

      GCSObject gcsObject = api().composeObjects(BUCKET_NAME2, COMPOSED_OBJECT, requestTemplate);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME2);
      assertEquals(gcsObject.getName(), COMPOSED_OBJECT);
      assertEquals(gcsObject.getContentType(), "text/plain");

   }

   @Test(groups = "live", dependsOnMethods = "testComposeObject")
   public void testComposeObjectWithOptions() {
      ObjectTemplate destination = new ObjectTemplate().contentType(MediaType.APPLICATION_JSON);
      Set<GCSObject> sourceList = Sets.newHashSet();
      sourceList.add(api().getObject(BUCKET_NAME2, UPLOAD_OBJECT_NAME));
      sourceList.add(api().getObject(BUCKET_NAME2, COPIED_OBJECT_NAME));

      ComposeObjectTemplate requestTemplate = new ComposeObjectTemplate().sourceObjects(sourceList).destination(
               destination);

      ComposeObjectOptions options = new ComposeObjectOptions().destinationPredefinedAcl(
               DestinationPredefinedAcl.BUCKET_OWNER_READ).ifMetagenerationNotMatch(RANDOM_LONG);

      GCSObject gcsObject = api().composeObjects(BUCKET_NAME2, COMPOSED_OBJECT2, requestTemplate, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME2);
      assertEquals(gcsObject.getName(), COMPOSED_OBJECT2);
      assertEquals(gcsObject.getContentType(), MediaType.APPLICATION_JSON);

   }

   @Test(groups = "live", dependsOnMethods = "testComposeObjectWithOptions")
   public void listObjects() {
      ListPage<GCSObject> list = api().listObjects(BUCKET_NAME);

      assertNotNull(list);
      assertEquals(list.get(0) instanceof GCSObject, true);
      assertEquals(list.getKind(), Kind.OBJECTS);
   }

   @Test(groups = "live", dependsOnMethods = "testComposeObjectWithOptions")
   public void testListObjectsWithOptions() {
      ListObjectOptions options = new ListObjectOptions().maxResults(1);
      ListPage<GCSObject> list = api().listObjects(BUCKET_NAME, options);

      while (list.nextMarker().isPresent()) {

         assertNotNull(list);
         assertEquals(list.get(0) instanceof GCSObject, true);
         assertEquals(list.size(), 1);
         assertEquals(list.getKind(), Kind.OBJECTS);

         options = new ListObjectOptions().maxResults(1).pageToken(list.getNextPageToken());
         list = api().listObjects(BUCKET_NAME, options);
      }
   }

   @Test(groups = "live", dependsOnMethods = "testComposeObjectWithOptions")
   public void testUpdateObject() {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ObjectTemplate template = new ObjectTemplate().addAcl(oacl).contentType("image/jpeg");
      GCSObject gcsObject = api().updateObject(BUCKET_NAME, UPLOAD_OBJECT_NAME2, template);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME2);
      assertEquals(gcsObject.getContentType(), "image/jpeg");
   }

   @Test(groups = "live", dependsOnMethods = "testUpdateObject")
   public void testUpdateObjectWithOptions() {

      String METADATA_KEY = "key1";
      String METADATA_VALUE = "value1";

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      UpdateObjectOptions options = new UpdateObjectOptions().ifMetagenerationNotMatch(RANDOM_LONG)
               .ifGenerationNotMatch(RANDOM_LONG);

      ObjectTemplate template = new ObjectTemplate().addAcl(oacl).contentType("image/jpeg")
               .contentDisposition("attachment").customMetadata(METADATA_KEY, METADATA_VALUE);
      GCSObject gcsObject = api().updateObject(BUCKET_NAME, UPLOAD_OBJECT_NAME2, template, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME2);
      assertEquals(gcsObject.getContentType(), "image/jpeg");
      assertNotNull(gcsObject.getAllMetadata());
      assertNotNull(gcsObject.getAllMetadata().get(METADATA_KEY), METADATA_VALUE);

   }

   @Test(groups = "live", dependsOnMethods = "testUpdateObjectWithOptions")
   public void testPatchObject() {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.READER).build();

      ObjectTemplate template = new ObjectTemplate().addAcl(oacl).contentType("image/jpeg");
      GCSObject gcsObject = api().patchObject(BUCKET_NAME, UPLOAD_OBJECT_NAME2, template);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME2);
      assertEquals(gcsObject.getContentType(), "image/jpeg");
   }

   @Test(groups = "live", dependsOnMethods = "testPatchObject")
   public void testPatchObjectsWithOptions() {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      UpdateObjectOptions options = new UpdateObjectOptions().ifMetagenerationNotMatch(RANDOM_LONG)
               .ifGenerationNotMatch(RANDOM_LONG);

      ObjectTemplate template = new ObjectTemplate().addAcl(oacl).contentType("image/jpeg")
               .contentDisposition("attachment");
      GCSObject gcsObject = api().patchObject(BUCKET_NAME, UPLOAD_OBJECT_NAME2, template, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME2);
      assertEquals(gcsObject.getContentType(), "image/jpeg");
      assertEquals(gcsObject.getContentDisposition(), "attachment");

   }

   @Test(groups = "live", dependsOnMethods = "testPatchObjectsWithOptions")
   public void testDeleteObject() {
      api().deleteObject(BUCKET_NAME2, UPLOAD_OBJECT_NAME);
      api().deleteObject(BUCKET_NAME2, COMPOSED_OBJECT2);
      api().deleteObject(BUCKET_NAME2, COMPOSED_OBJECT);
      api().deleteObject(BUCKET_NAME2, COPIED_OBJECT_NAME);
      api().deleteObject(BUCKET_NAME, UPLOAD_OBJECT_NAME2);

   }

   @Test(groups = "live", dependsOnMethods = "testPatchObjectsWithOptions")
   public void testDeleteObjectWithOptions() {
      DeleteObjectOptions options = new DeleteObjectOptions().ifGenerationMatch(generation).ifMetagenerationMatch(
               metageneration);
      api().deleteObject(BUCKET_NAME, UPLOAD_OBJECT_NAME, options);

   }

   // Stop the channel
/*   @Test(groups = "live", dependsOnMethods = "testDeleteObjectWithOptions")
   public void testStopChannel() {

      String address = "";
      String id = "";
      String resourceId = "";
      String resourceUri = "";
      String token = "";
      Long expiration = new Long(100);

      ObjectChangeNotificationTemplate template = new ObjectChangeNotificationTemplate().id(id)
               .address(URI.create(address)).resourceId(resourceId).resourceUri(URI.create(resourceUri))
               .type(DeliveryType.WEB_HOOK).expiration(expiration).payload(true).token(token);

      api.getObjectChangeNNotificationApi().stop(template);

   }*/

   @AfterClass
   private void deleteBucket() {
      api.getBucketApi().deleteBucket(BUCKET_NAME);
      api.getBucketApi().deleteBucket(BUCKET_NAME2);
   }

}
