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

package io.patriot_framework.network_simulator.docker.cleanup;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class designed for cleaning docker after job, means delete containers, networks and sometimes images.
 */
public class Cleaner {
    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
    private static final Logger LOGGER = LoggerFactory.getLogger(Cleaner.class);


    /**
     * Clear instances of docker container from list (stop -&gt; delete) and networks (delete).
     *
     * @param networks   the networks
     * @param containers the containers
     */
    public void cleanUp(List<String> networks, List<String> containers) {

        List<Container> outputCont = dockerClient.listContainersCmd().withShowAll(true).
                withNameFilter(containers).exec();

        if (!outputCont.isEmpty()) {
            for (Container container: outputCont) {

                if (!container.getStatus().contains("Exited") && !container.getStatus().contains("Created")) {
                    dockerClient.killContainerCmd(container.getId()).exec();
                }
                dockerClient.removeContainerCmd(container.getId()).exec();
            }
        }


        LOGGER.info("Trying to clear networks");
        if (!networks.isEmpty()) {
            for (String networkName : networks) {
                List<Network> network = dockerClient.listNetworksCmd().withNameFilter(networkName).exec();
                LOGGER.info("Network " + networkName + " is being cleared");
                dockerClient.removeNetworkCmd(networkName).exec();

            }
        }
    }
}
