package com.redhat.patriot.network_simulator.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

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
     * Gets docker client test.
     *
     * @return the docker client
     */
    public DockerClient getDockerClient() {
        return dockerClient;
    }
}
