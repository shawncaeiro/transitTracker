package com.shawncaeiro.transit.resources;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shawncaeiro.transit.api.Prediction;
import com.shawncaeiro.transit.api.PredictionResponse;
import org.json.JSONException;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Path("tracker")
@Produces(MediaType.APPLICATION_JSON)
public class BusTrackerResource {

	String apiKey;
	String urlPatternStopAndRoute = "http://www.ctabustracker.com/bustime/api/v2/getpredictions?key=%s&rt=%s&stpid=%s&format=json";
	String urlPatternStopOnly = "http://www.ctabustracker.com/bustime/api/v2/getpredictions?key=%s&stpid=%s&format=json";


	public BusTrackerResource(String apiKey) {
		this.apiKey = apiKey;
	}

    @GET
    @Path("route/{routeId}/stop/{stopId}")
    public Response allEvents(@PathParam("routeId") String routeId,
							@PathParam("stopId") String stopId) {

		String url = String.format(urlPatternStopAndRoute,
				apiKey, routeId, stopId);

    	CloseableHttpClient httpclient = HttpClients.createDefault();

    	CloseableHttpResponse response = null;
		Prediction prediction = null;
    	try {
			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
    	    System.out.println(response.getStatusLine());
        	ObjectMapper objectMapper = new ObjectMapper();
			prediction = objectMapper.readValue(response.getEntity().getContent(), Prediction.class);

    	} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (prediction.busTimeResponse.error != null && prediction.busTimeResponse.error.length > 0) {
    		return Response.noContent().build();
		}

		List<String> arrivalTimes = new ArrayList<>();
		for (Prediction.BusTimeResponse.Prd prd : prediction.busTimeResponse.prd) {
			arrivalTimes.add(prd.prdctdn);
		}
		PredictionResponse res = new PredictionResponse(
				prediction.busTimeResponse.prd[0].rtdd,
				prediction.busTimeResponse.prd[0].stpnm,
				arrivalTimes);

		return Response.ok().entity(res).build();
    }

	@GET
	@Path("stop/{stopId}")
	public Response allEvents(@PathParam("stopId") String stopId) {

		String url = String.format(urlPatternStopOnly,
				apiKey, stopId);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		Prediction prediction = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
			System.out.println(response.getStatusLine());
			ObjectMapper objectMapper = new ObjectMapper();
			prediction = objectMapper.readValue(response.getEntity().getContent(), Prediction.class);

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<PredictionResponse> arrivals = new ArrayList<>();
		List<String> arrivalTimes = new ArrayList<>();
		for (Prediction.BusTimeResponse.Prd prd : prediction.busTimeResponse.prd) {
			arrivalTimes.add(prd.prdctdn);
		}
		PredictionResponse res = new PredictionResponse(
				prediction.busTimeResponse.prd[0].rtdd,
				prediction.busTimeResponse.prd[0].stpnm,
				arrivalTimes);

		return Response.ok().entity(res).build();
	}

    public static void main(final String[] args) throws JSONException {
    	BusTrackerResource bus = new BusTrackerResource("APIKEY");
		bus.allEvents("77", "4996");
    }
	
}
