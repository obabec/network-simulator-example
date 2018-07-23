package com.redhat.patriot.network_simulator.example;

import com.redhat.patriot.network_simulator.example.files.FileUtils;
import com.redhat.patriot.network_simulator.example.image.docker.builder.DockerFileBuilder;

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
