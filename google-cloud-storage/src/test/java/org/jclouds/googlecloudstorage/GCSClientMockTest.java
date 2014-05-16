package org.jclouds.googlecloudstorage;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;
import static org.jclouds.Constants.PROPERTY_SO_TIMEOUT;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.concurrent.config.ExecutorServiceModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

public class GCSClientMockTest {
	
	 private static final String BUCKET_NAME = "myBucketName" + (int)100* Math.random() ;
	 private static final String PROJECT_ID = "1082289308625";
	 
	 
	 private static final Set<Module> modules = ImmutableSet.<Module> of(new ExecutorServiceModule(sameThreadExecutor() , sameThreadExecutor()));
	 
	 static GoogleCloudStorageClient getGCSClient(URL server) {
	      Properties overrides = new Properties();
	      // prevent expect-100 bug http://code.google.com/p/mockwebserver/issues/detail?id=6
	      overrides.setProperty(PROPERTY_SO_TIMEOUT, "0");
	      overrides.setProperty(PROPERTY_MAX_RETRIES, "1");
	      return ContextBuilder.newBuilder("google-cloud-storage").credentials("accessKey", "secretKey").endpoint(server.toString())
	            .modules(modules).overrides(overrides).buildApi(GoogleCloudStorageClient.class);
	 }
	 
	 
	   public void testBucketInsert() throws IOException, InterruptedException {
		      MockResponse mr = new MockResponse();
		      mr.setResponseCode(200);		  
		      MockWebServer server = new MockWebServer();
		      server.enqueue(mr);
		      try {
		         server.play();
		         GoogleCloudStorageClient client = getGCSClient(server.getUrl("/"));
		         boolean response = client.BucketInsert(BUCKET_NAME ,PROJECT_ID );
		         assertEquals(response, true);
		         RecordedRequest request = server.takeRequest();
		         assertEquals(request.getRequestLine(), "POST /b?project=" + PROJECT_ID );
		         assertEquals(request.getHeader("Content-Type"), "application/json");
		      } finally {
		         server.shutdown();
		      }
		   }
	 

}
