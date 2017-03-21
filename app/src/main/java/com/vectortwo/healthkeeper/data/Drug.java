package com.vectortwo.healthkeeper.data;

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
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ilya on 07/03/2017.
 */
public class Drug {
    public String title;

    public float dosage;
    public int timesAday;
    public int timesAweek;

    public Date startDate;
    public Date endDate;

    // TODO: preparse input string
    public static ArrayList<String> getSpellingSuggestions(String from) {
        ArrayList<String> res = new ArrayList<>();
        URL url = null;

        try {
            url = new URL("https://rxnav.nlm.nih.gov/REST/spellingsuggestions.json?name=" + "\"" + from + "\"");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String jsonRaw = downloadFrom(url);

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonRaw);
            JSONObject suggestionObj = (JSONObject) ((JSONObject) jsonObject.get("suggestionGroup")).get("suggestionList");
            JSONArray suggestions = (JSONArray) suggestionObj.get("suggestion");

            Iterator<String> it = suggestions.iterator();
            while (it.hasNext()) {
                res.add(it.next());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    // TODO: replace ' ' if exists with '+' in url
    private static String downloadFrom(URL url) {
        StringBuilder res = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;

            while ((line = in.readLine()) != null) {
                res.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }
}
