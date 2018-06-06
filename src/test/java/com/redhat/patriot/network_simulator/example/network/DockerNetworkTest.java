package com.redhat.patriot.network_simulator.example.network;

import com.github.dockerjava.api.model.Network;
import com.redhat.patriot.network_simulator.example.TestClass;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerNetworkTest extends TestClass {

    @Test
    void createNetworkWithSubnet() {

        DockerNetwork dockerNetwork = new DockerNetwork(dockerClient);

        String networkName = "test_network";
        dockerNetwork.createNetworkWithSubnet("175.16.0.0/16",
                networkName);

        List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(networkName).exec();

        assertEquals(false, networks.isEmpty());

        dockerNetwork.deleteNetwork(networkName);

    }
}