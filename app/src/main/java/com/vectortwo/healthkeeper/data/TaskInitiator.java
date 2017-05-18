package com.vectortwo.healthkeeper.data;

/**
 *  An interface for implementing the server-side of the Observer Pattern.
 */
public interface TaskInitiator {
    void addHandler(TaskHandler e);
}
