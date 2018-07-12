package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;

/**
 * The interface Container.
 */
public interface Container {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets ip address.
     *
     * @param networkName the network name
     * @return the ip address
     */
    String getIpAddress(String networkName);

    /**
     * Gets id.
     *
     * @return the id
     */
    String getId();

    /**
     * Is alive boolean.
     *
     * @return the boolean
     */
    boolean isAlive();

    /**
     * Exists boolean.
     *
     * @return the boolean
     */
    boolean exists();

    /**
     * Connect to network.
     *
     * @param networks the networks
     */
    void connectToNetwork(List<Network> networks);

    /**
     * Destroy container.
     */
    void destroyContainer();

}
