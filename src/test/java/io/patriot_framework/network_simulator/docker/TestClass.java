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

package io.patriot_framework.network_simulator.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;

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
