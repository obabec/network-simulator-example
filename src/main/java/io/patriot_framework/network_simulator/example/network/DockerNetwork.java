package io.patriot_framework.network_simulator.example.network;

import com.github.dockerjava.api.DockerClient;
import io.patriot_framework.network_simulator.example.manager.Manager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Docker network.
 */
public class DockerNetwork implements Network {
    private DockerClient dockerClient;
    private String name;
    private String id;

    /**
     * Instantiates a new Docker network.
     *
     * @param name the name
     */
    public DockerNetwork(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Docker network.
     *
     * @param name the name
     * @param id   the id
     */
    public DockerNetwork(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Instantiates a new Docker network.
     *
     * @param dockerClient the docker client
     * @param name         the name
     * @param id           the id
     */
    public DockerNetwork(DockerClient dockerClient, String name, String id) {

        this.dockerClient = dockerClient;
        this.name = name;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean exists(Manager dockerManager) {
        List<Network> networks = dockerManager.listNetworks().stream()
                                    .filter(Network -> Network.getId().equals(this.id))
                                    .collect(Collectors.toList());
        if (networks.isEmpty()) {
            return false;
        }
        return true;
    }

}
