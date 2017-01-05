package com.shawncaeiro.transit;

import com.shawncaeiro.transit.resources.BusTrackerResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TransitTrackerApplication extends Application<TransitTrackerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TransitTrackerApplication().run(args);
    }

    @Override
    public String getName() {
        return "TransitTracker";
    }

    @Override
    public void initialize(final Bootstrap<TransitTrackerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final TransitTrackerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
        BusTrackerResource busTrackerResource = new BusTrackerResource(configuration.getApiKey());
        environment.jersey().register(busTrackerResource);
    }

}
