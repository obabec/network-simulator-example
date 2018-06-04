package com.redhat.patriot.network_simulator.example.cleanup;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Network;

import java.util.List;


public class Cleaner {
    private DockerClient dockerClient;

    public Cleaner(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void cleanUp(List<String> networks, List<String> containers) {

        List<Container> outputCont = dockerClient.listContainersCmd().withShowAll(true).
                withNameFilter(containers).exec();

        if (!outputCont.isEmpty()) {
            for (Container container: outputCont) {

                if (!container.getStatus().contains("Exited")) {
                    dockerClient.killContainerCmd(container.getId()).exec();
                }
                dockerClient.removeContainerCmd(container.getId()).exec();
            }
        }

        List<Network> outputNetwork = dockerClient.listNetworksCmd()
                .withNameFilter(networks.get(0), networks.get(1)).exec();
        if (!outputNetwork.isEmpty()) {
            for (Network network: outputNetwork) {
                dockerClient.removeNetworkCmd(network.getName()).exec();

            }
        }
    }
}
