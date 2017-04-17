package com.vectortwo.healthkeeper.data;

import android.os.AsyncTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Downloads information about a specific drug from the Internet.
 */
public class DrugInfo {

    /**
     * Return string indicating an unsuccessful request.
     */
    public static final String NO_INFO = "";

    /**
     * An interface for handling the results of events initiated by {@link EventInitiator}
     *
     * @param <T> should match the output {@param <OUT>} type of the event that's being handled.
     */
    public interface EventHandler<T> {
        /**
         * Callback-method for handling the results of executed task.
         * @param r downloaded results
         */
        void onPostExecute(T r);
    }

    private interface EventInitiator {
        void addHandler(EventHandler e);
    }

    /**
     * Base class for building asynchronous requests for getting drug related information.
     * Implements observer pattern.
     *
     * @param <IN> input type of a request
     * @param <OUT> result type of a request
     */
    private abstract class BaseInfo<IN, OUT> extends AsyncTask<IN, Integer, OUT> implements EventInitiator {
        ArrayList<EventHandler<OUT>> handlers;

        public BaseInfo() {
            handlers = new ArrayList<>();
        }

        @Override
        public void addHandler(EventHandler e) {
            handlers.add(e);
        }

        @Override
        protected void onPostExecute(OUT out) {
            for (EventHandler<OUT> handler : handlers) {
                handler.onPostExecute(out);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    /**
     * Downloads spelling suggestion for a drug.
     * Example usage:
     *      DrugInfo info = new DrugInfo();
     *      DrugInfo.SpellingSuggestions op = info.new SpellingSuggestions();
     *      op.addHandler(new DrugInfo.EventHandler<ArrayList<String>>() {
     *      @Override
     *      public void onPostExecute(ArrayList<String> r) {
     *          // manipulate results here
     *      }
     *      });
     */
    public class SpellingSuggestions extends BaseInfo<String, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            if (params == null || params.length > 1) {
                throw new IllegalArgumentException("Exactly one argument is allowed!");
            }
            return getSpellingSuggestions(params[0]);
        }
    }

    /**
     * Downloads description for a drug.
     * Example usage:
     *      DrugInfo info = new DrugInfo();
     *      DrugInfo.Description op = info.new Description();
     *      op.addHandler(new DrugInfo.EventHandler<String>() {
     *      @Override
     *      public void onPostExecute(String r) {
     *          // manipulate results here
     *      }
     *      });
     */
    public class Description extends BaseInfo<String, String> {
        @Override
        protected String doInBackground(String... params) {
            if (params == null || params.length > 1) {
                throw new IllegalArgumentException("Exactly one argument is allowed!");
            }
            return getDescription(params[0]);
        }
    }

    /**
     * Downloads warnings for a drug.
     * Example usage:
     *      DrugInfo info = new DrugInfo();
     *      DrugInfo.Warnings op = info.new Warnings();
     *      op.addHandler(new DrugInfo.EventHandler<String>() {
     *      @Override
     *      public void onPostExecute(String r) {
     *          // manipulate results here
     *      }
     *      });
     */
    public class Warnings extends BaseInfo<String, String> {
        @Override
        protected String doInBackground(String... params) {
            if (params == null || params.length > 1) {
                throw new IllegalArgumentException("Exactly one argument is allowed!");
            }
            return getWarnings(params[0]);
        }
    }

    private static ArrayList<String> getSpellingSuggestions(String input) {
        input = input.trim().replaceAll("\\s", "%20");

        ArrayList<String> res = new ArrayList<>();
        URL url = null;

        try {
            url = new URL("https://rxnav.nlm.nih.gov/REST/spellingsuggestions.json?name=" + "\"" + input + "\"");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String allInfoStr = downloadFrom(url);

        try {
            JSONObject allInfoJSON = (JSONObject) new JSONParser().parse(allInfoStr);
            JSONObject suggestionJSON = (JSONObject) ((JSONObject) allInfoJSON.get("suggestionGroup")).get("suggestionList");
            if (suggestionJSON == null) {
                return res;
            }

            JSONArray suggestions = (JSONArray) suggestionJSON.get("suggestion");
            if (suggestions != null) {
                for (Object obj : suggestions) {
                    res.add((String) obj);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static String getDescription(String drugName) {
        JSONObject resultsJSON = drugResultsJSON(drugName);
        if (resultsJSON == null) {
            return NO_INFO;
        }
        JSONArray descJSON = (JSONArray) resultsJSON.get("indications_and_usage");
        if (descJSON == null) {
            return NO_INFO;
        }
        String desc = (String) descJSON.get(0);

        int start = 0, end = desc.length();
        Matcher matcher;

        Pattern extraneousPattern = Pattern.compile("USAGE");
        matcher = extraneousPattern.matcher(desc);
        if (matcher.find()) {
            start = matcher.end();
        }

        Pattern sentencePattern = Pattern.compile("\\.\\s");
        matcher = sentencePattern.matcher(desc);
        if (matcher.find()) {
            end = matcher.start() + 1;
        }

        desc = desc.substring(start, end);
        desc = desc.replaceAll("•", " ");

        return desc;
    }

    private static String getWarnings(String drugName) {
        JSONObject resultsJSON = drugResultsJSON(drugName);
        if (resultsJSON == null) {
            return NO_INFO;
        }
        JSONArray warningsJSON = (JSONArray) resultsJSON.get("contraindications");
        if (warningsJSON == null) {
            return NO_INFO;
        }
        String warnings = (String) warningsJSON.get(0);

        Pattern extraneousPattern = Pattern.compile("CONTRAINDICATIONS");
        Matcher matcher = extraneousPattern.matcher(warnings);
        if (matcher.find()) {
            warnings = warnings.substring(matcher.end());
        }

        return warnings;
    }

    private static JSONObject drugResultsJSON(String drugName) {
        drugName = drugName.trim().replaceAll("\\s", "%20");

        URL url;
        JSONObject resultsJSON = null;

        try {
            url = new URL("https://api.fda.gov/drug/label.json?search=brand_name:" + "\"" + drugName + "\"");
            String allInfoStr = downloadFrom(url);
            if (allInfoStr == null) {
                return null;
            }

            JSONObject allInfoJSON = (JSONObject) new JSONParser().parse(allInfoStr);
            JSONArray results = (JSONArray) allInfoJSON.get("results");
            if (results == null) {
                return null;
            }
            resultsJSON = (JSONObject) results.get(0);
        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }
        return resultsJSON;
    }

    private static String downloadFrom(URL url) {
        StringBuilder res = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            res = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                res.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (res == null ? null : res.toString());
    }
}