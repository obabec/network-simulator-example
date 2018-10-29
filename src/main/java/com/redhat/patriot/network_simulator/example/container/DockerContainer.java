package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.manager.Manager;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Instance of DockerContainer representing informations which are required for work with container. Class is providing
 * basic work with container. This type of work is later executed in DockerManager.
 */
public class DockerContainer implements Container {
    private String name;
    private String id;
    private DockerManager dockerManager;

    public String getName() {
        return name;
    }

    @Override
    public String getIpAddress(Network network) {
        return dockerManager.findIpAddress(this, network);
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public DockerContainer(String id) {
        this.id = id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Instantiates a new Docker container.
     *
     * @param name          the name
     * @param id            the id
     * @param dockerManager the docker manager
     */
    public DockerContainer(String name, String id, DockerManager dockerManager) {
        this.name = name;
        this.id = id;
        this.dockerManager = dockerManager;
    }

    /**
     * Instantiates a new Docker container.
     *
     * @param name the name
     * @param id   the id
     */
    public DockerContainer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Gets manager.
     *
     * @return the manager
     */
    public Manager getManager() {
        return dockerManager;
    }

    /**
     * Sets manager.
     *
     * @param manager the manager
     */
    public void setManager(Manager manager) {
        this.dockerManager = (DockerManager) manager;
    }

    /**
     * Method returning if container is up or down.
     * @return
     */
    @Override
    public boolean isAlive() {
        return false;
    }

    /**
     * Method returning if is container alive.
     * @return
     */
    @Override
    public boolean exists() {
        List<Container> aliveCont = dockerManager.listContainers().stream()
                .filter(DockerContainer -> DockerContainer.getId().equals(this.id))
                .collect(Collectors.toList());

        if (aliveCont.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Method providing connection with networks.
     * @param networks
     */
    @Override
    public void connectToNetwork(List<Network> networks) {
        for (Network network : networks) {
            dockerManager.connectContainerToNetwork(this, network);
        }
    }

    /**
     * Method provides soft delete of container (stop + delete).
     */
    @Override
    public void destroyContainer() {
        dockerManager.destroyContainer(this);
    }

    public String getGatewayNetworkIp() {
        return dockerManager.getDefaultGwNetworkIp(this);
    }

    public Integer getGatewayNetworkMask() {
        return dockerManager.getDefaultGwNetworkMask(this);
    }

}
