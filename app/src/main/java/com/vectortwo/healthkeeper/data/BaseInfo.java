package com.vectortwo.healthkeeper.data;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Base class for building asynchronous requests based on observer pattern.
 *
 * @param <IN> input type of a request
 * @param <OUT> result type of a request
 */
public abstract class BaseInfo<IN, OUT> extends AsyncTask<IN, Integer, OUT> implements TaskInitiator {

    /**
     * Return string indicating an unsuccessful request.
     */
    public static final String NO_INFO = "";

    private ArrayList<TaskHandler<OUT>> handlers;

    public BaseInfo() {
        handlers = new ArrayList<>();
    }

    @Override
    public void addHandler(TaskHandler e) {
        handlers.add(e);
    }

    @Override
    protected void onPostExecute(OUT out) {
        for (TaskHandler<OUT> handler : handlers) {
            handler.onPostExecute(out);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

