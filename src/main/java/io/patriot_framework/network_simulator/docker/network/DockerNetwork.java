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

import io.patriot_framework.network.simulator.api.model.network.Network;
import io.patriot_framework.network_simulator.docker.manager.Manager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Docker network.
 */
public class DockerNetwork extends Network {

    public DockerNetwork(String name, String id) {
        super.setName(name);
        super.setId(id);
    }


    public boolean exists(Manager dockerManager) {
        List<Network> networks = dockerManager.listNetworks().stream()
                .filter(Network -> Network.getId().equals(super.getId()))
                .collect(Collectors.toList());
        return !networks.isEmpty();
    }

    @Override
    public String getCreator() {
        return "Docker";
    }
}
