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

import io.patriot_framework.network_simulator.docker.DockerfileGenerator;
import io.patriot_framework.network_simulator.docker.TestClass;
import io.patriot_framework.network_simulator.docker.container.DockerContainer;
import io.patriot_framework.network_simulator.docker.image.docker.DockerImage;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import io.patriot_framework.network_simulator.docker.network.DockerNetwork;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Cleaner test.
 */
class CleanerTest extends TestClass {

    /**
     * Clean up test.
     */
    @Test
    void cleanUp() {
        DockerManager dockerManager = new DockerManager();
        List<String> tags = Arrays.asList("testtag:01");
        DockerImage dockerImage = new DockerImage(dockerManager);
        DockerfileGenerator dockerfileGenerator = new DockerfileGenerator();
        Path dockerfile = dockerfileGenerator.createAndGenerateDockerfile();
        dockerImage.buildImage(new HashSet<>(tags), dockerfile.toAbsolutePath().toString());

        DockerContainer dockerContainer =
                (DockerContainer) dockerManager.createContainer("test_cont", tags.get(0));

        DockerNetwork dockerNetwork =
                (DockerNetwork) dockerManager.createNetwork("test_network", "175.28.0.0/16");

        dockerContainer.connectToNetwork(Arrays.asList(dockerNetwork));

        Cleaner cleaner = new Cleaner();
        List<String> networks = new ArrayList<>();
        networks.add(dockerNetwork.getName());
        List<String> conts = new ArrayList<>();
        conts.add(dockerContainer.getName());

        cleaner.cleanUp(Arrays.asList(dockerNetwork.getName()), Arrays.asList(dockerContainer.getName()));

        assertEquals((dockerContainer.exists() && dockerNetwork.exists(dockerManager)), false);
        dockerfileGenerator.deleteDir();
    }
}