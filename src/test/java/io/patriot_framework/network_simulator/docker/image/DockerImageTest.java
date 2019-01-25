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

package io.patriot_framework.network_simulator.docker.image;

import com.github.dockerjava.api.model.Image;
import io.patriot_framework.network_simulator.docker.image.docker.DockerImage;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import io.patriot_framework.network_simulator.docker.DockerfileGenerator;
import io.patriot_framework.network_simulator.docker.TestClass;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Docker image test.
 */
class DockerImageTest extends TestClass {

    /**
     * Build image test.
     */
    @Test
    void buildImage() {
        DockerManager dockerManager = new DockerManager();
        List<String> tags = Arrays.asList("testtag:01");
        DockerImage dockerImage = new DockerImage(dockerManager);
        DockerfileGenerator dockerfileGenerator = new DockerfileGenerator();
        Path testFile = dockerfileGenerator.createAndGenerateDockerfile();
        dockerImage.buildImage(new HashSet<>(tags), testFile.toAbsolutePath().toString());
        List<Image> outputImage = dockerClient.listImagesCmd()
                .withShowAll(true).withImageNameFilter(tags.get(0)).exec();

        assertEquals(false, outputImage.isEmpty());

        dockerImage.deleteImage(tags.get(0));
        dockerfileGenerator.deleteDir();


    }


}