package com.vectortwo.healthkeeper.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by ilya on 18/04/2017.
 */
public class UrlDownloader {
    protected static String downloadFrom(URL url) {
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
