package io.patriot_framework.network_simulator.example.image;

import com.github.dockerjava.api.model.Image;
import io.patriot_framework.network_simulator.example.image.docker.DockerImage;
import io.patriot_framework.network_simulator.example.manager.DockerManager;
import io.patriot_framework.network_simulator.example.DockerfileGenerator;
import io.patriot_framework.network_simulator.example.TestClass;
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