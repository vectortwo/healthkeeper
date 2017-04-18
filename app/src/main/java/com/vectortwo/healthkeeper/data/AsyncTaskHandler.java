package com.vectortwo.healthkeeper.data;

/**
 * An interface for handling the results of events initiated by {@link AsyncTaskInitiator}
 *
 * @param <T> should match the output {@param <OUT>} type of the event that's being handled.
 */
public interface AsyncTaskHandler<T> {
    /**
     * Callback-method for handling the results of executed task.
     * @param r downloaded results
     */
    void onPostExecute(T r);
}
