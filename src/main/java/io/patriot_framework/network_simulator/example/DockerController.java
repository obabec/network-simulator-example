package io.patriot_framework.network_simulator.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import io.patriot_framework.network_simulator.example.cleanup.Cleaner;
import io.patriot_framework.network_simulator.example.container.Container;
import io.patriot_framework.network_simulator.example.files.FileUtils;
import io.patriot_framework.network_simulator.example.image.docker.DockerImage;
import io.patriot_framework.network_simulator.example.manager.DockerManager;
import io.patriot_framework.network_simulator.example.network.DockerNetwork;
import io.patriot_framework.network_simulator.example.network.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * The type Docker controller.
 */
public class DockerController {
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
            String tagRouter  = "router_iproute:01";

            buildImages(tagApp, tagRouter);

            DockerNetwork serverNetwork =
                    (DockerNetwork) dockerManager.createNetwork("server_network", "172.28.0.0/16");
            networks.add(serverNetwork.getName());
            DockerNetwork clientNetwork =
                    (DockerNetwork) dockerManager.createNetwork("client_network", "172.29.0.0/16");
            networks.add(clientNetwork.getName());
            Container router = dockerManager.createContainer("router",tagRouter);
            conts.add(connectAndStart(dockerManager, router, Arrays.asList(clientNetwork, serverNetwork)));

            System.out.println(dockerManager.findIpAddress(router, serverNetwork));

            Container commClient = dockerManager.createContainer("comm_client", tagApp);
            conts.add(connectAndStart(dockerManager, commClient, Arrays.asList(clientNetwork)));

            Container commServer = dockerManager.createContainer("comm_server", tagApp);
            conts.add(connectAndStart(dockerManager, commServer, Arrays.asList(serverNetwork)));

        } catch (Exception e ) {
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
    String connectAndStart(DockerManager manager,Container container, List<Network> networks) {
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
    void buildImages(String tagApp, String tagRouter){

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
