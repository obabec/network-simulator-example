package com.redhat.patriot.network_simulator.example.container;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Network;
import com.redhat.patriot.network_simulator.example.TestClass;
import com.redhat.patriot.network_simulator.example.image.DockerImage;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerContTest extends TestClass {

    @Test
    void createContainer() {

        DockerCont dockerCont = new DockerCont(dockerClient);
        DockerImage dockerImage = new DockerImage(dockerClient);

        List<String> tags = Arrays.asList("testtag:02");
        List<String> nameOfCont = Arrays.asList("test_cont");

        dockerImage.buildImage(new HashSet<>(tags), "router/Dockerfile");
        dockerCont.createContainer(tags.get(0), nameOfCont.get(0));

        List<Container> outputConts = dockerClient.listContainersCmd().withShowAll(true)
                .withNameFilter(nameOfCont).exec();

        assertEquals(false, outputConts.isEmpty());

        dockerCont.deleteCont(nameOfCont.get(0));
    }

    @Test
    void connectContToNetwork() {

        DockerCont dockerCont = new DockerCont(dockerClient);
        DockerNetwork dockerNetwork = new DockerNetwork(dockerClient);
        DockerImage dockerImage = new DockerImage(dockerClient);

        List<String> tags = Arrays.asList("test_tag:01");
        String contName = "test_cont";
        String networkName = "test_network";

        dockerImage.buildImage(new HashSet<>(tags), "router/Dockerfile");
        CreateContainerResponse containerResponse = dockerCont.createContainer(tags.get(0), contName);
        Network network = dockerNetwork.createNetworkWithSubnet("172.42.0.0/16", networkName);

        dockerCont.connectContToNetwork(containerResponse, network.getId());

        Assertions.assertNotNull(dockerClient.inspectContainerCmd(containerResponse.getId()).exec()
                .getNetworkSettings().getNetworks().get(networkName));

        dockerNetwork.deleteNetwork(networkName);
        dockerCont.deleteCont(contName);
        dockerImage.deleteImage(tags.get(0));
    }
}