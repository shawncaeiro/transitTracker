package com.shawncaeiro.transit.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shawncaeiro on 1/4/17.
 */
public class PredictionResponse {
    public List<BusTimes> data;

    public PredictionResponse(String routeName, String stopName, List<String> arrivalTimes) {
        this.data = new ArrayList<>();
        this.data.add(new BusTimes(routeName, stopName, arrivalTimes));
    }

    public static class BusTimes {
        public String routeName;
        public String stopName;
        public List<String> arrivalTimes;

        public BusTimes(String routeName, String stopName, List<String> arrivalTimes) {
            this.routeName = routeName;
            this.stopName = stopName;
            this.arrivalTimes = arrivalTimes;
        }
    }
}
