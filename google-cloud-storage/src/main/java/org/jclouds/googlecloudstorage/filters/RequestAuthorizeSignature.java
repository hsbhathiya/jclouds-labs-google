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
package org.jclouds.googlecloudstorage.filters;

import javax.inject.Singleton;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.oauth.v2.filters.OAuthAuthenticator;
import org.jclouds.rest.RequestSigner;
import org.jclouds.rest.annotations.RequestFilters;


/**
 * @author Bhathiya Supun
 * 
 */
@Singleton
@RequestFilters(OAuthAuthenticator.class)
public class RequestAuthorizeSignature implements HttpRequestFilter , RequestSigner {
	
	
	
	@Override
	@RequestFilters(OAuthAuthenticator.class)
	public HttpRequest filter(HttpRequest request) throws HttpException {
		return request;
	}
		
	@Override
	public String createStringToSign(HttpRequest input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sign(String toSign) {
		// TODO Auto-generated method stub
		return null;
	}
}
