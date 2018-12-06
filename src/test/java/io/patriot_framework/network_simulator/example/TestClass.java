package io.patriot_framework.network_simulator.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import io.patriot_framework.network_simulator.example.manager.DockerManager;

/**
 * The type Test class.
 */
public class TestClass {
    /**
     * The Docker client.
     */
    protected DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();

    /**
     * The Docker manager.
     */
    protected DockerManager dockerManager = new DockerManager();

    /**
     * Gets docker manager.
     *
     * @return the docker manager
     */
    public DockerManager getDockerManager() {
        return dockerManager;
    }

    /**
     * Gets docker client test.
     *
     * @return the docker client
     */
    public DockerClient getDockerClient() {
        return dockerClient;
    }


}
