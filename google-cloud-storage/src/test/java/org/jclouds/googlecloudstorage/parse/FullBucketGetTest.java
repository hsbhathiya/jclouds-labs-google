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

import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.ProjectTeam;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.ProjectTeam.Team;
import org.jclouds.googlecloudstorage.domain.BucketAccessControls.Role;
import org.jclouds.googlecloudstorage.domain.Buckets;
import org.jclouds.googlecloudstorage.domain.Buckets.Location;
import org.jclouds.googlecloudstorage.domain.Buckets.Owner;
import org.jclouds.googlecloudstorage.domain.Buckets.StorageClass;
import org.jclouds.googlecloudstorage.domain.DefaultObjectAccessControls;
import org.jclouds.googlecloudstorage.features.ApiResourceRefferences.ObjectRole;
import org.jclouds.googlecloudstorage.internal.BaseGoogleCloudStorageParseTest;

public class FullBucketGetTest extends BaseGoogleCloudStorageParseTest<Buckets> {

   private final BucketAccessControls acl_1 = BucketAccessControls
            .builder()
            .id("jcloudtestbucket3500/project-owners-1082289308625")
            .selfLink(
                     URI.create("https://www.googleapis.com/storage/v1/b/jcloudtestbucket3500/acl/project-owners-1082289308625"))
            .bucket("jcloudtestbucket3500").entity("project-owners-1082289308625").role(Role.OWNER)
            .projectTeam(new ProjectTeam("1082289308625", Team.owners)).etag("CAo=").build();
   private final DefaultObjectAccessControls defObjectAcl = DefaultObjectAccessControls.builder()
            .entity("project-owners-1082289308625").role(ObjectRole.OWNER).etag("CAo=").build();

   @Override
   public String resource() {
      return "/full_bucket_get.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public Buckets expected() {
      return Buckets.builder().id("jcloudtestbucket3500")
               .selfLink(URI.create("https://www.googleapis.com/storage/v1/b/jcloudtestbucket3500"))
               .name("jcloudtestbucket3500").projectNumber(Long.valueOf("1082289308625"))
               .timeCreated(new SimpleDateFormatDateService().iso8601DateParse("2014-06-19T14:03:22.345Z"))
               .metageneration(Long.valueOf(10)).owner(Owner.builder().entity("project-owners-1082289308625").build())
               .location(Location.US).storageClass(StorageClass.STANDARD).etag("CAo=").addAcl(acl_1)
               .addDefaultObjectAcl(defObjectAcl).build();
   }

}
// expected [Buckets{kind=storage#bucket, id=jcloudtestbucket3500,
// selfLink=https://www.googleapis.com/storage/v1/b/jcloudtestbucket3500, etag=CAo=, name=jcloudtestbucket3500,
// timeCreated=Thu Jun 19 19:33:22 IST 2014, projectNumber=1082289308625, metageneration=10,
// acl=[BucketAccessControls{kind=storage#bucketAccessControl, id=jcloudtestbucket3500/project-owners-1082289308625,
// selfLink=https://www.googleapis.com/storage/v1/b/jcloudtestbucket3500/acl/project-owners-1082289308625, etag=CAo=,
// bucket=jcloudtestbucket3500, entity=project-owners-1082289308625, role=OWNER}],
// defaultObjectAcl=[DefaultObjectAccessControls{kind=storage#objectAccessControl, etag=CAo=,
// entity=project-owners-1082289308625, role=OWNER}], owner=Owner{entiy=project-owners-1082289308625}, location=US,
// cors=[], storageClass=STANDARD}]
// butfound [Buckets{kind=storage#bucket, id=jcloudtestbucket3500,
// selfLink=https://www.googleapis.com/storage/v1/b/jcloudtestbucket3500, etag=CAo=, name=jcloudtestbucket3500,
// timeCreated=Thu Jun 19 19:33:22 IST 2014, projectNumber=1082289308625, metageneration=10,
// acl=[BucketAccessControls{kind=storage#bucketAccessControl, id=jcloudtestbucket3500/project-owners-1082289308625,
// selfLink=https://www.googleapis.com/storage/v1/b/jcloudtestbucket3500/acl/project-owners-1082289308625, etag=CAo=,
// bucket=jcloudtestbucket3500, entity=project-owners-1082289308625, role=OWNER}],
// defaultObjectAcl=[DefaultObjectAccessControls{kind=storage#objectAccessControl, etag=CAo=,
// entity=project-owners-1082289308625, role=OWNER}], owner=Owner{entiy=project-owners-1082289308625}, location=US,
// storageClass=STANDARD}]

