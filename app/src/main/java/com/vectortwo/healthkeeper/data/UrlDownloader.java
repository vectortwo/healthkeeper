package com.vectortwo.healthkeeper.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Base class for dealing with HTTP requests
 */
public class UrlDownloader {

    /**
     * Downloads content located at {@param url}.
     * Runs on current thread.
     *
     * @param url to download from
     * @return downloaded content in String
     */
    protected static String downloadFrom(URL url) {
        StringBuilder res = new StringBuilder("");

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
