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

package io.patriot_framework.network_simulator.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import io.patriot_framework.network.simulator.api.model.network.Network;
import io.patriot_framework.network_simulator.docker.cleanup.Cleaner;
import io.patriot_framework.network_simulator.docker.container.Container;
import io.patriot_framework.network_simulator.docker.files.FileUtils;
import io.patriot_framework.network_simulator.docker.image.docker.DockerImage;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import io.patriot_framework.network_simulator.docker.network.DockerNetwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * The type Docker controller.
 */
public class DockerExample {
    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
    private DockerManager dockerManager = new DockerManager();

    /**
     * Genererate enviroment.
     */
    public void genererateEnviroment() {
        List<String> networks = new ArrayList<>();
        List<String> conts = new ArrayList<>();
        try {
            DockerManager dockerManager = new DockerManager();
            String tagApp = "app_test:01";
            String tagRouter = "router_iproute:01";

            buildImages(tagApp, tagRouter);

            DockerNetwork serverNetwork =
                    (DockerNetwork) dockerManager.createNetwork("server_network", "172.28.0.0/16");
            networks.add(serverNetwork.getName());
            DockerNetwork clientNetwork =
                    (DockerNetwork) dockerManager.createNetwork("client_network", "172.29.0.0/16");
            networks.add(clientNetwork.getName());
            Container router = dockerManager.createContainer("router", tagRouter);
            conts.add(connectAndStart(dockerManager, router, Arrays.asList(clientNetwork, serverNetwork)));

            System.out.println(dockerManager.findIpAddress(router, serverNetwork));

            Container commClient = dockerManager.createContainer("comm_client", tagApp);
            conts.add(connectAndStart(dockerManager, commClient, Collections.singletonList(clientNetwork)));

            Container commServer = dockerManager.createContainer("comm_server", tagApp);
            conts.add(connectAndStart(dockerManager, commServer, Collections.singletonList(serverNetwork)));

        } catch (Exception e) {
            e.printStackTrace();
            Cleaner cleaner = new Cleaner();
            cleaner.cleanUp(networks, conts);
        }


    }

    /**
     * Connect containers to networks and start them.
     *
     * @param manager   the manager
     * @param container the container
     * @param networks  the networks
     * @return the string
     */
    private String connectAndStart(DockerManager manager, Container container, List<Network> networks) {
        container.connectToNetwork(networks);
        manager.startContainer(container);
        return container.getName();
    }

    /**
     * Build images.
     *
     * @param tagApp    the tag app
     * @param tagRouter the tag router
     */
    private void buildImages(String tagApp, String tagRouter) {

        FileUtils fileUtils = new FileUtils();
        DockerImage dockerImage = new DockerImage(dockerManager);
        try {
            dockerImage.buildAppImage(new HashSet<>(Arrays.asList(tagApp)));
            dockerImage.buildRouterImage(new HashSet<>(Arrays.asList(tagRouter)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
