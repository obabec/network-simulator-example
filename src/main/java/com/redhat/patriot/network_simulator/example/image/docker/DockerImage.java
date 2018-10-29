package com.redhat.patriot.network_simulator.example.image.docker;

import com.redhat.patriot.network_simulator.example.files.FileUtils;
import com.redhat.patriot.network_simulator.example.image.Image;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Class representing DockerImage. Providing necessary functionality, like building and deleting images.
 */
public class DockerImage  implements Image {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImage.class);
    private Path tmpDockerDir;
    private FileUtils fileUtils;

    private DockerManager dockerManager;

    /**
     * Instantiates a new Docker image.
     *
     * @param dockerManager the docker manager
     */
    public DockerImage(DockerManager dockerManager) {
        this.dockerManager = dockerManager;
        this.fileUtils = new FileUtils();
    }

    /**
     * Method providing image building and selecting right path for resource files.
     * @param tag Set of image tags
     * @param path Path for dockerfile
     */
    public void buildImage(Set<String> tag, String path) {
        dockerManager.buildImage(new File(path), tag);
    }

    public void buildRouterImage(Set<String> tag) throws IOException {
        InputStream in = getClass().getResourceAsStream("/router/Dockerfile");
        if (tmpDockerDir == null) tmpDockerDir = Files.createTempDirectory(Paths.get(""),"tmpTestDir");
        Path testFile = Files.createTempFile(tmpDockerDir, "RouterDockerfile", "");

        dockerManager.buildImage(new File(fileUtils.convertToFile(in, testFile.toString())), tag);
    }

    public void buildAppImage(Set<String> tag) throws IOException {

        InputStream in = getClass().getResourceAsStream("/app/Dockerfile");
        if (tmpDockerDir == null) tmpDockerDir = Files.createTempDirectory(Paths.get(""),"tmpTestDir");
        Path testFile = Files.createTempFile(tmpDockerDir, "AppDockerfile", "");

        dockerManager.buildImage(new File(fileUtils.convertToFile(in, testFile.toString())), tag);

    }

    @Override
    public void deleteImage(Set<String> tags) {

    }

    /**
     * Delete image.
     *
     * @param imageTag the image tag
     */
    public void deleteImage(String imageTag) {
        dockerManager.deleteImage(imageTag);
    }



}
