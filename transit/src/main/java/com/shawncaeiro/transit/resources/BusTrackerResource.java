package com.shawncaeiro.transit.resources;

import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.shawncaeiro.transit.api.Event;

@Path("tracker")
@Produces(MediaType.APPLICATION_JSON)
public class BusTrackerResource {

    @GET
    @Path("route/{routeId}/stop/{stopId}")
    public String allEvents(@PathParam("routeId") String routeId, @PathParam("stopId") String stopId) throws ClientProtocolException, IOException, JSONException {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	String url = String.format("http://www.ctabustracker.com/bustime/api/v2/getpredictions?key=API_KEY&rt=%s&stpid=%s&format=json",
    			routeId, stopId);
    	HttpGet httpGet = new HttpGet(url);
    	CloseableHttpResponse response1 = httpclient.execute(httpGet);
    	String retSrc = "";
    	// The underlying HTTP connection is still held by the response object
    	// to allow the response content to be streamed directly from the network socket.
    	// In order to ensure correct deallocation of system resources
    	// the user MUST call CloseableHttpResponse#close() from a finally clause.
    	// Please note that if response content is not fully consumed the underlying
    	// connection cannot be safely re-used and will be shut down and discarded
    	// by the connection manager. 
    	try {
    	    System.out.println(response1.getStatusLine());
    	    HttpEntity entity1 = response1.getEntity();
        	retSrc = EntityUtils.toString(entity1); 
        	JSONObject myObject = new JSONObject(retSrc);
        	
    	    // do something useful with the response body
    	    // and ensure it is fully consumed
    	    EntityUtils.consume(entity1);
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
    	    response1.close();
    	}	
    	return retSrc;

//    	
//    	Map<String, String> map = new HashMap<String, String>();
//    	map.put("dog", "type of animal");
//    	System.out.println(map.get("dog"));
//
//        return map;
    }
    
    public static void main(final String[] args) throws JSONException {
    	BusTrackerResource bus = new BusTrackerResource();
    	try {
			bus.allEvents("77", "4996");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
