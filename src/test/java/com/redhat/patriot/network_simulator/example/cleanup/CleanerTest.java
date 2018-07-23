package com.redhat.patriot.network_simulator.example.cleanup;

import com.redhat.patriot.network_simulator.example.DockerfileGenerator;
import com.redhat.patriot.network_simulator.example.TestClass;
import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.image.docker.DockerImage;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
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