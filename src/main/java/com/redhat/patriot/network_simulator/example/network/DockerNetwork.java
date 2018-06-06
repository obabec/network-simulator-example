package com.redhat.patriot.network_simulator.example.network;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.Network;

public class DockerNetwork {
    private DockerClient dockerClient;

    public DockerNetwork(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public Network createNetworkWithSubnet(String subnet, String name) {
        Network.Ipam ipam = new Network.Ipam().withConfig(new Network.Ipam.Config().withSubnet(subnet));

        CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd().withName(name)
                .withDriver("bridge")
                .withIpam(ipam)
                .exec();
        Network network = dockerClient.inspectNetworkCmd().withNetworkId(networkResponse.getId()).exec();
        return network;
    }

    public void deleteNetwork(String name) {
        dockerClient.removeNetworkCmd(name).exec();
    }

}
