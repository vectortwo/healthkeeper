package com.vectortwo.healthkeeper.data;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Base class for building asynchronous requests based on observer pattern.
 *
 * @param <IN> input type of a request
 * @param <OUT> result type of a request
 */
public abstract class BaseInfo<IN, OUT> extends AsyncTask<IN, Integer, OUT> implements AsyncTaskInitiator {

    /**
     * Return string indicating an unsuccessful request.
     */
    public static final String NO_INFO = "";

    private ArrayList<AsyncTaskHandler<OUT>> handlers;

    public BaseInfo() {
        handlers = new ArrayList<>();
    }

    @Override
    public void addHandler(AsyncTaskHandler e) {
        handlers.add(e);
    }

    @Override
    protected void onPostExecute(OUT out) {
        for (AsyncTaskHandler<OUT> handler : handlers) {
            handler.onPostExecute(out);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

