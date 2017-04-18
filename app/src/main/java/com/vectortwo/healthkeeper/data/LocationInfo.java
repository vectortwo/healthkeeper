package com.vectortwo.healthkeeper.data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.*;
import java.util.Collections;
import java.util.List;

/**
 *  Downloads location specific information from the Internet
 */
public class LocationInfo extends UrlDownloader {
    private String ip;

    public LocationInfo() {
        this.ip = getIPAddress();
    }

    public class Details extends BaseInfo<Void, Details> {
        private String city;
        private String country;

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        @Override
        protected Details doInBackground(Void... voids) {
            if (ip.isEmpty()) {
                return this;
            }
            JSONObject resultsJSON = locationResultsJSON(ip);
            if (resultsJSON != null) {
                this.city = getCity(resultsJSON);
                this.country = getCountry(resultsJSON);
            }
            return this;
        }

        private String getCity(JSONObject resultsJSON) {
            String res = (String) resultsJSON.get("city");
            return res == null ? NO_INFO : res;
        }

        private String getCountry(JSONObject resultsJSON) {
            String res = (String) resultsJSON.get("country_name");
            return res == null ? NO_INFO : res;
        }
    }

    /**
     * Get IP address from first non-localhost interface.
     *
     * @return  ipv4 or ipv6 address or empty string
     */
    private static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (isIPv4) {
                            return sAddr;
                        } else {
                            int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                            return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static JSONObject locationResultsJSON(String ip) {
        JSONObject resultJSON = null;
        try {
            URL url = new URL("http://freegeoip.net/json/" + ip);
            String allInfoStr = downloadFrom(url);
            if (allInfoStr == null) {
                return null;
            }
            resultJSON = (JSONObject) new JSONParser().parse(allInfoStr);
        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }
        return resultJSON;
    }
}
