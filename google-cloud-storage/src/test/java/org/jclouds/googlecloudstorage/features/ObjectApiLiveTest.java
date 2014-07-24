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

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.AbsentOn403Or404Or500;
import org.jclouds.googlecloudstorage.domain.ComposeObjectTemplate;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.DestinationPredefinedAcl;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.Projection;
import org.jclouds.googlecloudstorage.domain.GCSObject;
import org.jclouds.googlecloudstorage.domain.DomainResourceRefferences.UploadType;
import org.jclouds.googlecloudstorage.domain.ListPage;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectTemplate;
import org.jclouds.googlecloudstorage.domain.Resource.Kind;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageApiLiveTest;
import org.jclouds.googlecloudstorage.options.ComposeObjectOptions;
import org.jclouds.googlecloudstorage.options.CopyObjectOptions;
import org.jclouds.googlecloudstorage.options.DeleteObjectOptions;
import org.jclouds.googlecloudstorage.options.GetObjectOptions;
import org.jclouds.googlecloudstorage.options.InsertObjectOptions;
import org.jclouds.googlecloudstorage.options.ListObjectOptions;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Sets;
import com.google.common.base.Optional;

public class ObjectApiLiveTest extends BaseGoogleCloudStorageApiLiveTest {

   private static final String BUCKET_NAME = "jcloudobjectoperations";// + (int) (Math.random() * 10000);
   private static final String DESTINATION_BUCKET_NAME = "jcloudobjectdestination";
   private static final String UPLOAD_OBJECT_NAME = "objectOperation.txt";
   private static final String DESTINATION_OBJECT_NAME = "copyofObjectOperation.txt";
   private static final String COMPOSED_OBJECT = "ComposedObject1.txt";
   private static final String COMPOSED_OBJECT2 = "ComposedObject2.json";

   private Long metageneration;
   private Long generation;

   private ObjectApi api() {
      return api.getObjectApi();
   }

   @Test(groups = "live")
   public void testSimpleUpload() throws IOException {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      /*
       * File file = new File( getClass().getResource("/" + UPLOAD_OBJECT_NAME).getFile()); FileInputStream fis= new
       * FileInputStream(file);
       */
      String data = "Payload data";

      InsertObjectOptions options = new InsertObjectOptions().name(UPLOAD_OBJECT_NAME);

      ObjectTemplate template = new ObjectTemplate();
      template.setPayload(data);
      template.contentType("text/plain").addAcl(oacl)
               .size(template.getPayload().getContentMetadata().getContentLength());

      assertNotNull(template);
      assertNotNull(template.getPayload());

      GCSObject gcsObject = api().insertObject(BUCKET_NAME, UploadType.MEDIA, template, options);

      assertNotNull(gcsObject);
      assertEquals(gcsObject.getBucket(), BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME);

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

      GCSObject gcsObject = api().copyObject(DESTINATION_BUCKET_NAME, DESTINATION_OBJECT_NAME, BUCKET_NAME,
               UPLOAD_OBJECT_NAME);

      assertNotNull(gcsObject);
      assertEquals(gcsObject.getBucket(), DESTINATION_BUCKET_NAME);
      assertEquals(gcsObject.getName(), DESTINATION_OBJECT_NAME);
      assertEquals(gcsObject.getContentType(), "text/plain");

   }

   @Test(groups = "live", dependsOnMethods = "testCopyObject")
   public void testCopyObjectWithOptions() {

      CopyObjectOptions options = new CopyObjectOptions().ifSourceGenerationMatch(generation)
               .ifSourceMetagenerationMatch(metageneration).projection(Projection.FULL);

      GCSObject gcsObject = api().copyObject(DESTINATION_BUCKET_NAME, UPLOAD_OBJECT_NAME, BUCKET_NAME,
               UPLOAD_OBJECT_NAME, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), DESTINATION_BUCKET_NAME);
      assertEquals(gcsObject.getName(), UPLOAD_OBJECT_NAME);
      assertEquals(gcsObject.getContentType(), "text/plain");

   }

   @Test(groups = "live", dependsOnMethods = "testCopyObjectWithOptions")
   public void testComposeObject() {

      ObjectAccessControls oacl = ObjectAccessControls.builder().bucket(BUCKET_NAME).entity("allUsers")
               .role(ObjectRole.OWNER).build();

      ObjectTemplate destination = new ObjectTemplate().contentType("text/plain").addAcl(oacl);
      Set<GCSObject> sourceList = Sets.newHashSet();
      sourceList.add(api().getObject(DESTINATION_BUCKET_NAME, UPLOAD_OBJECT_NAME));
      sourceList.add(api().getObject(DESTINATION_BUCKET_NAME, DESTINATION_OBJECT_NAME));

      ComposeObjectTemplate requestTemplate = new ComposeObjectTemplate().sourceObjects(sourceList).destination(
               destination);

      GCSObject gcsObject = api().composeObjects(DESTINATION_BUCKET_NAME, COMPOSED_OBJECT, requestTemplate);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), DESTINATION_BUCKET_NAME);
      assertEquals(gcsObject.getName(), COMPOSED_OBJECT);
      assertEquals(gcsObject.getContentType(), "text/plain");

   }

   @Test(groups = "live", dependsOnMethods = "testCopyObjectWithOptions")
   public void testComposeObjectWithOptions() {

      ObjectTemplate destination = new ObjectTemplate().contentType(MediaType.APPLICATION_JSON);
      Set<GCSObject> sourceList = Sets.newHashSet();
      sourceList.add(api().getObject(DESTINATION_BUCKET_NAME, UPLOAD_OBJECT_NAME));
      sourceList.add(api().getObject(DESTINATION_BUCKET_NAME, DESTINATION_OBJECT_NAME));

      ComposeObjectTemplate requestTemplate = new ComposeObjectTemplate().sourceObjects(sourceList).destination(
               destination);

      ComposeObjectOptions options = new ComposeObjectOptions().destinationPredefinedAcl(
               DestinationPredefinedAcl.BUCKET_OWNER_READ).ifMetagenerationNotMatch(100L);

      GCSObject gcsObject = api().composeObjects(DESTINATION_BUCKET_NAME, COMPOSED_OBJECT2, requestTemplate, options);

      assertNotNull(gcsObject);
      assertNotNull(gcsObject.getAcl());
      assertEquals(gcsObject.getBucket(), DESTINATION_BUCKET_NAME);
      assertEquals(gcsObject.getName(), COMPOSED_OBJECT2);
      assertEquals(gcsObject.getContentType(), MediaType.APPLICATION_JSON);

   }

   @Test(groups = "live", dependsOnMethods = "listObjects")
   public void deleteObject() {

      api().deleteObject(DESTINATION_BUCKET_NAME, UPLOAD_OBJECT_NAME);
      api().deleteObject(DESTINATION_BUCKET_NAME, COMPOSED_OBJECT2);
      api().deleteObject(DESTINATION_BUCKET_NAME, COMPOSED_OBJECT);

   }

   @Test(groups = "live", dependsOnMethods = "listObjects")
   public void deleteObjectWithOptions() {
      DeleteObjectOptions options = new DeleteObjectOptions().ifGenerationMatch(generation).ifMetagenerationMatch(
               metageneration);

      api().deleteObject(BUCKET_NAME, UPLOAD_OBJECT_NAME, options);
   }
   
   @Test(groups = "live", dependsOnMethods = "testComposeObjectWithOptions")
   public void listObjects() {
      
        ListPage<GCSObject> list = api().listObjects(BUCKET_NAME);
        
        assertNotNull(list);
        assertEquals(list.get(0) instanceof GCSObject , true ); 
        assertEquals(list.getKind(), Kind.OBJECTS);
      
   }
   
   @Test(groups = "live", dependsOnMethods = "testComposeObjectWithOptions")
   public void listObjectsWithOptions() {

        ListObjectOptions options = new ListObjectOptions().maxResults(1);
        ListPage<GCSObject> list = api().listObjects(BUCKET_NAME,options);
        
        while(list.nextMarker().isPresent()){
                 
           assertNotNull(list);
           assertEquals(list.get(0) instanceof GCSObject , true ); 
           assertEquals(list.size(),1);
           assertEquals(list.getKind(), Kind.OBJECTS);
           
           options = new ListObjectOptions().maxResults(1).pageToken(list.getNextPageToken());
           list = api().listObjects(BUCKET_NAME,options);
           
        }
      
   }
   
   


}
