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

package io.patriot_framework.network_simulator.docker.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import io.patriot_framework.network_simulator.docker.DockerfileGenerator;
import io.patriot_framework.network_simulator.docker.TestClass;
import io.patriot_framework.network_simulator.docker.image.docker.DockerImage;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import io.patriot_framework.network_simulator.docker.network.DockerNetwork;
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

    @Test
    void destroyContainer() {
        DockerManager dockerManager = new DockerManager();

        DockerImage dockerImage = new DockerImage(dockerManager);

        List<String> tags = Arrays.asList("testtag:02");
        List<String> nameOfCont = Arrays.asList("test_cont", "test_cont_id");
        DockerfileGenerator testDockerfileGenerator = new DockerfileGenerator();
        Path dockerfile = testDockerfileGenerator.createAndGenerateDockerfile();
        dockerImage.buildImage(new HashSet<>(tags), dockerfile.toAbsolutePath().toString());

        DockerContainer dockerCont = (DockerContainer) dockerManager.createContainer("test_cont", "testtag:02");
        DockerContainer dockerContId = (DockerContainer) dockerManager.createContainer("test_cont_id", "testtag:02");
        DockerContainer destroyContId = new DockerContainer(dockerContId.getId());
        dockerManager.destroyContainer(dockerCont);
        dockerManager.destroyContainer(destroyContId);

        List<Container> outputConts = dockerClient.listContainersCmd().withShowAll(true)
                .withNameFilter(nameOfCont).exec();

        assertTrue(outputConts.isEmpty());

    }

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