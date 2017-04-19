package com.vectortwo.healthkeeper.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Downloads information about a specified drug from the Internet.
 */
public class DrugInfo extends UrlDownloader {

    private String drugTitle;

    public DrugInfo(String drugTitle) {
        this.drugTitle = drugTitle;
    }

    public String getDrugTitle() {
        return drugTitle;
    }

    public void setDrugTitle(String drugTitle) {
        this.drugTitle = drugTitle;
    }

    /**
     * Downloads spelling suggestion for a drug.
     * Example usage:
     *      DrugInfo info = new DrugInfo(drugTitle);
     *      DrugInfo.SpellingSuggestions op = info.new SpellingSuggestions();
     *      op.addHandler(new DrugInfo.EventHandler<ArrayList<String>>() {
     *      @Override
     *      public void onPostExecute(ArrayList<String> r) {
     *          // manipulate results here
     *      }
     *      });
     */
    public class SpellingSuggestions extends BaseInfo<Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return getSpellingSuggestions(drugTitle);
        }

        private ArrayList<String> getSpellingSuggestions(String input) {
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

    }

    /**
     * Downloads detail information about a drug (e.g. description, contradictions)
     * Example usage:
     *      DrugInfo info = new DrugInfo(drugTitle);
     *      DrugInfo.Details op = info.new Details();
     *      op.addHandler(new DrugInfo.EventHandler<Details>() {
     *      @Override
     *      public void onPostExecute(Details r) {
     *           description = r.getDescription();
     *      }
     *      });
     */
    public class Details extends BaseInfo<Void, Details> {
        private String description = NO_INFO;
        private String warnings = NO_INFO;

        public String getDescription() {
            return description;
        }

        public String getWarnings() {
            return warnings;
        }

        @Override
        protected Details doInBackground(Void... params) {
            JSONObject resultsJSON = drugResultsJSON(drugTitle);
            if (resultsJSON != null) {
                this.description = getDescription(resultsJSON);
                this.warnings = getWarnings(resultsJSON);
            }
            return this;
        }

        private String getDescription(JSONObject resultsJSON) {
            if (resultsJSON == null) {
                return BaseInfo.NO_INFO;
            }
            JSONArray descJSON = (JSONArray) resultsJSON.get("indications_and_usage");
            if (descJSON == null) {
                return BaseInfo.NO_INFO;
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

        private String getWarnings(JSONObject resultsJSON) {
            if (resultsJSON == null) {
                return BaseInfo.NO_INFO;
            }
            JSONArray warningsJSON = (JSONArray) resultsJSON.get("contraindications");
            if (warningsJSON == null) {
                return BaseInfo.NO_INFO;
            }
            String warnings = (String) warningsJSON.get(0);

            Pattern extraneousPattern = Pattern.compile("CONTRAINDICATIONS");
            Matcher matcher = extraneousPattern.matcher(warnings);
            if (matcher.find()) {
                warnings = warnings.substring(matcher.end());
            }

            return warnings;
        }

        private JSONObject drugResultsJSON(String drugName) {
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
    }

}