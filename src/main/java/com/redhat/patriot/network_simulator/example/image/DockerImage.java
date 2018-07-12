package com.redhat.patriot.network_simulator.example.image;

import com.redhat.patriot.network_simulator.example.files.FileUtils;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Class representing DockerImage. Providing necessary functionality, like building and deleting images.
 */
public class DockerImage  implements Image{
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImage.class);

    private DockerManager dockerManager;

    /**
     * Instantiates a new Docker image.
     *
     * @param dockerManager the docker manager
     */
    public DockerImage(DockerManager dockerManager) {
        this.dockerManager = dockerManager;
    }

    /**
     * Method providing image building and selecting right path for resource files.
     * @param tag Set of image tags
     * @param path Path for dockerfile
     */
    public void buildImage(Set<String> tag, String path) {

        FileUtils fileUtils = new FileUtils();
        File dockerFile = new File(DockerImage.class.getClassLoader().getResource(path).getFile());
        if (dockerFile.exists()) {

            LOGGER.info("Looking in resources");
            dockerManager.buildImage(dockerFile, tag);

        } else if (DockerImage.class.getClassLoader().getResourceAsStream(path) != null){

            LOGGER.info("Looking in root");

            try {
                Path tmpDir = Files.createTempDirectory(Paths.get(""),"tmpDir");
                File docker = new File(fileUtils.convertToFile(DockerImage.class.getClassLoader()
                        .getResourceAsStream(path), tmpDir.toAbsolutePath() + "/Dockerfile"));

                File script = new File(fileUtils.convertToFile(DockerImage.class.getClassLoader()
                        .getResourceAsStream("app/setGW"), tmpDir.toAbsolutePath() + "/setGW"));

                script.setExecutable(true);

                dockerManager.buildImage(docker, tag);

                fileUtils.deleteDirWithFiles(tmpDir.toFile());

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            LOGGER.warn("DOCKERFILES does not exists");
            System.exit(0);
        }
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
