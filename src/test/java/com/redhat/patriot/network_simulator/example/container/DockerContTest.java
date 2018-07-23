package com.redhat.patriot.network_simulator.example.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.redhat.patriot.network_simulator.example.TestClass;
import com.redhat.patriot.network_simulator.example.DockerfileGenerator;
import com.redhat.patriot.network_simulator.example.image.docker.DockerImage;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Docker cont test.
 */
class DockerContTest extends TestClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContTest.class);

    /**
     * Create container test.
     */
    @Test
    void createContainer() {

        DockerManager dockerManager = new DockerManager();

        DockerImage dockerImage = new DockerImage(dockerManager);

        List<String> tags = Arrays.asList("testtag:02");
        List<String> nameOfCont = Arrays.asList("test_cont");
        DockerfileGenerator testDockerfileGenerator = new DockerfileGenerator();
        Path dockerfile = testDockerfileGenerator.createAndGenerateDockerfile();
        dockerImage.buildImage(new HashSet<>(tags), dockerfile.toAbsolutePath().toString());
        DockerContainer dockerCont = (DockerContainer) dockerManager.createContainer("test_cont", "testtag:02");

        List<Container> outputConts = dockerClient.listContainersCmd().withShowAll(true)
                .withNameFilter(nameOfCont).exec();

        assertEquals(false, outputConts.isEmpty());
        dockerManager.destroyContainer(dockerCont);
        testDockerfileGenerator.deleteDir();
    }

    /**
     * Connect cont to network test.
     */
    @Test
    void connectContToNetwork() {

        List<String> tags = Arrays.asList("test_tag:01");
        DockerManager dockerManager = new DockerManager();
        DockerImage dockerImage = new DockerImage(dockerManager);
        DockerfileGenerator testDockerfileGenerator = new DockerfileGenerator();
        Path dockerfile = testDockerfileGenerator.createAndGenerateDockerfile();
        dockerImage.buildImage(new HashSet<>(tags), dockerfile.toAbsolutePath().toString());

        DockerContainer dockerCont = (DockerContainer) dockerManager
                .createContainer("test_cont", tags.get(0));

        DockerNetwork dockerNetwork = (DockerNetwork) dockerManager
                .createNetwork("test_network", "172.42.0.0/16");


        dockerCont.connectToNetwork(Arrays.asList(dockerNetwork));

        Assertions.assertNotNull(dockerClient.inspectContainerCmd(dockerCont.getId()).exec()
                .getNetworkSettings().getNetworks().toString().contains(dockerNetwork.getId()));

        dockerManager.destroyContainer(dockerCont);
        dockerManager.destroyNetwork(dockerNetwork);
        dockerImage.deleteImage(tags.get(0));
    }

    /**
     * Test volume.
     */
    @Deprecated
    @Test
    public void TestVolume() {
        DockerClient dockerClient = DockerClientBuilder.
                getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
        DockerManager dockerManager = new DockerManager();
        DockerImage dockerImage = new DockerImage(dockerManager);
        String tag = "volume_test:01";
        DockerfileGenerator dockerfileGenerator = new DockerfileGenerator();
        Path dockerfile = dockerfileGenerator.createAndGenerateDockerfile();
        dockerImage.buildImage(new HashSet<>(Arrays.asList(tag)), dockerfile.toAbsolutePath().toString());
        String volume = "/opt/app";
        String bind = "app";
        DockerContainer dockerContainer =
                (DockerContainer) dockerManager.createContainer("volume_test", tag, volume, bind);
        dockerManager.startContainer(dockerContainer);

        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(dockerContainer.getId()).exec();
        assertFalse(containerResponse.getMounts().isEmpty());
        dockerContainer.destroyContainer();
        dockerfileGenerator.deleteDir();
    }
}