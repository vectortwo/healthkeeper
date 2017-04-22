package com.vectortwo.healthkeeper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.*;

/**
 *  Downloads location specific information from the Internet
 */
public class LocationInfo extends UrlDownloader {
    private String ip = "";
    private volatile int gotIP = -1;

    public LocationInfo() {
        PublicIP ipGetter = new PublicIP();
        ipGetter.addHandler(new TaskHandler<String>() {
            @Override
            public void onPostExecute(String r) {
                ip = r;
            }
        });
        ipGetter.execute();
    }

    private class PublicIP extends BaseInfo<Void, String> {

        @NonNull
        @Override
        protected String doInBackground(Void... voids) {
            String ip = "";
            try {
                URL url = new URL("https://api.ipify.org/?format=json");
                String allInfoStr = downloadFrom(url);
                if (!allInfoStr.isEmpty()) {
                    JSONObject allInfoJSON = (JSONObject) new JSONParser().parse(allInfoStr);
                    String fetchedIP = (String) allInfoJSON.get("ip");
                    if (fetchedIP != null) {
                        ip = fetchedIP;
                    }
                }
            } catch (MalformedURLException | ParseException e) {
                e.printStackTrace();
            }
            gotIP = ip.isEmpty() ? 0 : 1;
            return ip;
        }
    }

    public class Details extends BaseInfo<Void, Details> {
        private String city = NO_INFO;
        private String country = NO_INFO;

        @NonNull
        public String getCity() {
            return city;
        }

        @NonNull
        public String getCountry() {
            return country;
        }

        @Override
        protected Details doInBackground(Void... voids) {
            while (gotIP < 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            JSONObject resultsJSON = locationResultsJSON(ip);
            if (resultsJSON != null) {
                this.city = getCity(resultsJSON);
                this.country = getCountry(resultsJSON);
            }
            return this;
        }

        @NonNull
        private String getCity(@NonNull JSONObject resultsJSON) {
            String res = (String) resultsJSON.get("city");
            return res == null ? NO_INFO : res;
        }

        @NonNull
        private String getCountry(@NonNull JSONObject resultsJSON) {
            String res = (String) resultsJSON.get("country_name");
            return res == null ? NO_INFO : res;
        }
    }

    @Nullable
    private static JSONObject locationResultsJSON(@NonNull String ip) {
        JSONObject resultJSON = null;
        try {
            URL url = new URL("http://freegeoip.net/json/" + ip);
            String allInfoStr = downloadFrom(url);
            resultJSON = (JSONObject) new JSONParser().parse(allInfoStr);
        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }
        return resultJSON;
    }
}
