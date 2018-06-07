package com.redhat.patriot.network_simulator.example.manager;

import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;

/**
 * The interface Manager.
 */
public interface Manager {
    /**
     * Create container container.
     *
     * @param name the name
     * @param tag  the tag
     * @return the container
     */
    Container createContainer(String name, String tag);

    /**
     * Create network network.
     *
     * @param name   the name
     * @param subnet the subnet
     * @return the network
     */
    Network createNetwork(String name, String subnet);

    /**
     * List containers list.
     *
     * @return the list
     */
    List<Container> listContainers();

    /**
     * List networks list.
     *
     * @return the list
     */
    List<Network> listNetworks();

    /**
     * Connect container to network.
     *
     * @param container the container
     * @param network   the network
     */
    void connectContainerToNetwork(Container container,Network network);

    /**
     * Destroy container.
     *
     * @param container the container
     */
    void destroyContainer(Container container);

    /**
     * Destroy network.
     *
     * @param network the network
     */
    void destroyNetwork(Network network);

    /**
     * Run command.
     *
     * @param container the container
     * @param command   the command
     */
    void runCommand(Container container, String command);

    /**
     * Start container.
     *
     * @param container the container
     */
    void startContainer(Container container);

}
