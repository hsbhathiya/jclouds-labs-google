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

import java.util.HashMap;
import java.util.Map;

/**
 * All data in Google Cloud Storage belongs inside a project. A project consists
 * of a set of users, a set of APIs, and billing, authentication, and monitoring
 * settings for those APIs. User can have one project or multiple projects.
 * <p/>
 * 
 * @author Bhathiya Supun
 */
public class Project {
   
   private final String projectName;  
   private final String projectId;
   private final String projectNumber;
   private final Map< Member ,Permission> projectPermission = new HashMap<Member, Project.Permission>();
      
   public Project(String projectName,String projectId, String projectNumber, Member createdBy) {
    
      this.projectName = projectName;
      this.projectId = projectId;
      this.projectNumber = projectNumber;
      projectPermission.put(createdBy, Permission.IS_owner);      
   }   
 
   
   
   public String getProjectName() {
      return projectName;
   }



   public String getProjectId() {
      return projectId;
   }



   public String getProjectNumber() {
      return projectNumber;
   }



   private enum Permission{
         IS_owner,
         Can_edit,
         Can_view
   }

}



