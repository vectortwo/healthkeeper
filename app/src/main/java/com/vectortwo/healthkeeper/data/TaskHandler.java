package com.vectortwo.healthkeeper.data;

/**
 * An interface for handling the results of events initiated by {@link TaskInitiator}
 *
 * @param <T> should match the output {@param <OUT>} type of the event that's being handled.
 */
public interface TaskHandler<T> {
    /**
     * Callback-method for handling the results of executed task.
     * @param r task results
     */
    void onPostExecute(T r);
}
