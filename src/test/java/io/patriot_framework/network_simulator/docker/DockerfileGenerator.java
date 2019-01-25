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

import io.patriot_framework.network_simulator.docker.files.FileUtils;
import io.patriot_framework.network_simulator.docker.image.docker.builder.DockerFileBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * The type Dockerfile generator.
 */
public class DockerfileGenerator {

    private Path dirPath;

    /**
     * Gets dir path.
     *
     * @return the dir path
     */
    public Path getDirPath() {
        return dirPath;
    }

    /**
     * Create and generate dockerfile path.
     *
     * @return the path
     */
    public Path createAndGenerateDockerfile() {
        Path tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory(Paths.get("/tmp"),"tmpDockerDir");
            Path testFile = Files.createTempFile(tmpDir, "Dockerfile", "");
            generateDockerFile(testFile.toAbsolutePath());
            dirPath = tmpDir;
            return testFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate docker file.
     *
     * @param path the path
     */
    void generateDockerFile(Path path) {
        DockerFileBuilder dockerFileBuilder = new DockerFileBuilder();
        dockerFileBuilder.from("phusion/baseimage:0.10.1")
                .run("echo \"deb http://archive.ubuntu.com/ubuntu trusty main universe\" > /etc/apt/sources.list")
                .run("apt-get -y update")
                .run("DEBIAN_FRONTEND=noninteractive apt-get install -y " +
                        "-q python-software-properties software-properties-common")
                .workdir("/")
                .cmd(Arrays.asList("/sbin/my_init")).write(path);
    }

    /**
     * Delete dir.
     */
    public void deleteDir() {
        FileUtils fileUtils = new FileUtils();
        fileUtils.deleteDirWithFiles(dirPath.toFile());
    }
}
