/*
 * Copyright 2019 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.network_simulator.docker.network;

import com.github.dockerjava.api.DockerClient;
import io.patriot_framework.network_simulator.docker.manager.Manager;

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
