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

import com.github.dockerjava.api.model.Network;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import io.patriot_framework.network_simulator.docker.TestClass;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Docker network test.
 */
class DockerNetworkTest extends TestClass {

    /**
     * Create network with subnet test.
     */
    @Test
    void createNetworkWithSubnet() {
        DockerManager dockerManager = new DockerManager();
        DockerNetwork dockerNetwork = (DockerNetwork) dockerManager.createNetwork("test_network", "175.16.0.0/16");


        List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(dockerNetwork.getName()).exec();

        assertEquals(false, networks.isEmpty());

        dockerManager.destroyNetwork(dockerNetwork);

    }
}
