package com.redhat.patriot.network_simulator.example.network;

import com.redhat.patriot.network_simulator.example.manager.Manager;

/**
 * The interface Network.
 */
public interface Network {
    /**
     * Gets id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Exists boolean.
     *
     * @param dockerManager the docker manager
     * @return the boolean
     */
    boolean exists(Manager dockerManager);
}
