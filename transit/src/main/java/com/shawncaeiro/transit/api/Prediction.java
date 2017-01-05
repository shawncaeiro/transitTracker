package com.shawncaeiro.transit.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by shawncaeiro on 1/4/17.
 */
public class Prediction {

    @JsonProperty("bustime-response")
    public BusTimeResponse busTimeResponse;

    @Override
    public String toString() {
        return "Prediction{" +
                "busTimeResponse=" + busTimeResponse +
                '}';
    }

    public static class BusTimeResponse {
        public Prd[] prd;

        @Override
        public String toString() {
            return "BusTimeResponse{" +
                    "prd=" + Arrays.toString(prd) +
                    '}';
        }

        public static class Prd {
            public String tmstmp;
            public String typ;
            public String stpnm;
            public String stpid;
            public String vid;
            public String dstp;
            public String rt;
            public String rtdd;
            public String rtdir;
            public String des;
            public String prdtm;
            public String tablockid;
            public String tatripid;
            public Boolean dly;
            public String prdctdn;
            public String zone;

            @Override
            public String toString() {
                return "Prd{" +
                        "tmstmp='" + tmstmp + '\'' +
                        ", typ='" + typ + '\'' +
                        ", stpnm='" + stpnm + '\'' +
                        ", stpid='" + stpid + '\'' +
                        ", vid='" + vid + '\'' +
                        ", dstp='" + dstp + '\'' +
                        ", rt='" + rt + '\'' +
                        ", rtdd='" + rtdd + '\'' +
                        ", rtdir='" + rtdir + '\'' +
                        ", des='" + des + '\'' +
                        ", prdtm='" + prdtm + '\'' +
                        ", tablockid='" + tablockid + '\'' +
                        ", tatripid='" + tatripid + '\'' +
                        ", dly=" + dly +
                        ", prdctdn='" + prdctdn + '\'' +
                        ", zone='" + zone + '\'' +
                        '}';
            }
        }
    }

}
