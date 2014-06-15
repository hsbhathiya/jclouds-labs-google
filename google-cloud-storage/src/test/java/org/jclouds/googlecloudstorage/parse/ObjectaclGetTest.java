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
package org.jclouds.googlecloudstorage.parse;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.jclouds.googlecloudstorage.domain.ObjectAccessControls;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls.ProjectTeam;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls.ProjectTeam.Team;
import org.jclouds.googlecloudstorage.domain.ObjectAccessControls.Role;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageParseTest;

/**
 * @author Bhathiya Supun
 */
public class ObjectaclGetTest extends BaseGoogleCloudStorageParseTest<ObjectAccessControls> {

   @Override
   public String resource() {
      return "/objectacl_get.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public ObjectAccessControls expected() {
      return ObjectAccessControls
               .builder()
               .bucket("jcloudtestbucket")
               .object("foo.txt")
               .generation(Long.valueOf("1394121608485000"))
               .entity("project-owners-1082289308625")
               .role(Role.OWNER)
               .etag("CIix/dmj/rwCEAE=")
               .projectTeam(new ProjectTeam("1082289308625", Team.owners))
               .selfLink(
                        URI.create("https://www.googleapis.com/storage/v1/b/jcloudtestbucket/o/foo.txt/acl/project-owners-1082289308625"))
               .id("jcloudtestbucket/foo.txt/1394121608485000/project-owners-1082289308625").build();
   }

   /*
    * {
    * 
    * "kind": "storage#objectAccessControl", "id":
    * "jcloudtestbucket/foo.txt/1394121608485000/project-owners-1082289308625", "selfLink":
    * "https://www.googleapis.com/storage/v1/b/jcloudtestbucket/o/foo.txt/acl/project-owners-1082289308625", "bucket":
    * "jcloudtestbucket", "object": "foo.txt", "generation": "1394121608485000", "entity":
    * "project-owners-1082289308625", "role": "OWNER", "projectTeam": { "projectNumber": "1082289308625", "team":
    * "owners" }, "etag": "CIix/dmj/rwCEAE=" }
    */
}
