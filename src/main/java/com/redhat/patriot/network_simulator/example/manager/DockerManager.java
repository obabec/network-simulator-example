package com.redhat.patriot.network_simulator.example.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Network.Ipam;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import com.redhat.patriot.network_simulator.example.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Class providing connection between Patriot api and java-docker api.
 */
public class DockerManager implements Manager {


    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);
    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();

    @Override
    public String findIpAddress(Container container, Network network) {
        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(container.getName()).exec();
        NetworkSettings netSettings = containerResponse.getNetworkSettings();

        return netSettings.getNetworks().get(network.getName()).getIpAddress();
    }

    @Override
    public void buildImage(File dockerfile, Set<String> tag) {
        dockerClient.buildImageCmd(dockerfile).withTags(tag).exec(new BuildImageResultCallback()).awaitImageId();
    }

    @Override
    public void deleteImage(String tag) {
        dockerClient.removeImageCmd(tag).exec();
    }

    @Override
    public Container createContainer(String name, String tag) {
        LOGGER.info("Started creating container");
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(tag)
                .withPrivileged(true)
                .withCmd()
                .withName(name)
                .exec();
        LOGGER.info("Container created with id: " + containerResponse.getId());
        return new DockerContainer(name, containerResponse.getId(), new DockerManager());
    }


    @Override
    public Network createNetwork(String name, String subnet) {

        Ipam ipam = new Ipam().withConfig(new Ipam.Config().withSubnet(subnet));

        CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd().withName(name)
                .withDriver("bridge")
                .withIpam(ipam)
                .exec();

        return new DockerNetwork(name, networkResponse.getId());
    }

    /**
     * Method providing service for finding containers and return list of all created containers
     * @return
     */
    @Override
    public List<Container> listContainers() {
        List<com.github.dockerjava.api.model.Container> outputConts = dockerClient.listContainersCmd()
                .withShowAll(true).exec();
        List<Container> dockerContainers = new ArrayList<>();
        for (com.github.dockerjava.api.model.Container c: outputConts) {
            dockerContainers.add(new DockerContainer(Arrays.toString(c.getNames()), c.getId()));
        }
        return dockerContainers;
    }

    /**
     * Method providing service for finding networks and return list of all created networks
     * @return
     */
    @Override
    public List<Network> listNetworks() {
        List<com.github.dockerjava.api.model.Network> modelNetworks = dockerClient.listNetworksCmd().exec();
        List<Network> networks = new ArrayList<>();
        for (com.github.dockerjava.api.model.Network network :  modelNetworks) {
            networks.add(new DockerNetwork(network.getName(), network.getId()));
        }
        return networks;
    }

    @Override
    public void connectContainerToNetwork(Container container, Network network) {
        dockerClient.connectToNetworkCmd().withNetworkId(network.getId()).withContainerId(container.getId()).exec();
    }

    /**
     * Method which destroys docker network based on network id.
     * @param container
     */
    @Override
    public void destroyContainer(Container container) {
        List<com.github.dockerjava.api.model.Container> outputCont = dockerClient.listContainersCmd().withShowAll(true).
                withNameFilter(Arrays.asList(container.getName())).exec();

        if (!outputCont.get(0).getStatus().contains("Exited") && !outputCont.get(0).getStatus().contains("Created")) {
            dockerClient.killContainerCmd(container.getId()).exec();
        }

        dockerClient.removeContainerCmd(container.getName()).exec();
    }

    /**
     * Method which destroys docker network based on network id.
     * @param network
     */
    @Override
    public void destroyNetwork(Network network) {
        dockerClient.removeNetworkCmd(network.getName()).withNetworkId(network.getId()).exec();
    }

    /**
     * Method is providing execution of commands directly in running docker container.
     * @param container
     * @param command
     */
    @Override
    public void runCommand(Container container, String command) {

        try {
            String[] commandWithArguments = command.split("\\s+");
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                    .withAttachStdout(true)
                    .withCmd(commandWithArguments)
                    .withUser("root")
                    .exec();
            dockerClient.execStartCmd(execCreateCmdResponse.getId())
                        .exec(new ExecStartResultCallback(System.out, System.err))
                        .awaitCompletion(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which starts docker container based on container name.
     * @param container
     */
    @Override
    public void startContainer(Container container) {
        LOGGER.info("Starting container");
        dockerClient.startContainerCmd(container.getName()).exec();
    }


}
