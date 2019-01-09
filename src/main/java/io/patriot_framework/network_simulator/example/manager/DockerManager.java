package io.patriot_framework.network_simulator.example.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Network.Ipam;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import io.patriot_framework.network_simulator.example.container.Container;
import io.patriot_framework.network_simulator.example.container.DockerContainer;
import io.patriot_framework.network_simulator.example.network.DockerNetwork;
import io.patriot_framework.network_simulator.example.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

    public String findIpAddress(Container container) {
        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(container.getName()).exec();
        NetworkSettings netSettings = containerResponse.getNetworkSettings();

        return netSettings.getIpAddress();
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

    /**
     * Create container container.
     *
     * @param name       the name
     * @param tag        the tag
     * @param volumePath the volume path
     * @param bindPath   the bind path
     * @return the container
     */
    public Container createContainer(String name, String tag, String volumePath, String bindPath) {
        LOGGER.info("Starting creating container with volume " + volumePath);
        Volume volume = new Volume(volumePath);
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(tag)
                .withPrivileged(true)
                .withVolumes(volume)
                .withBinds(new Bind(bindPath, volume))
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
        List<com.github.dockerjava.api.model.Container> outputCont;

        if (container.getId() == null || container.getId().isEmpty()) {
            outputCont = dockerClient.listContainersCmd().withShowAll(true)
                .withNameFilter(Arrays.asList(container.getName())).exec();
        } else {
            outputCont = dockerClient.listContainersCmd().withShowAll(true)
                    .withIdFilter(Arrays.asList(container.getId())).exec();
        }

        if (!outputCont.isEmpty() && !outputCont.isEmpty()) {

            if (!outputCont.get(0).getStatus().contains("Exited") &&
                    !outputCont.get(0).getStatus().contains("Created")) {
                dockerClient.killContainerCmd(container.getId()).exec();
            }
            dockerClient.removeContainerCmd(outputCont.get(0).getNames()[0])
                    .withContainerId(outputCont.get(0).getId()).exec();
        } else {
            throw new NullPointerException("Container not found!");
        }

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

    public String getDefaultGwNetworkIp(Container container) {
        String ip = dockerClient.inspectContainerCmd(container.getName())
                .withContainerId(container.getId()).exec().getNetworkSettings().getGateway();
        int mask = getDefaultGwNetworkMask(container);
        return convertToNetworkIp(ip, mask);
    }

    /**
     * Method gathers gateway's network mask.
     * @param container
     * @return
     */
    public Integer getDefaultGwNetworkMask(Container container) {
        return dockerClient.inspectContainerCmd(container.getName()).withContainerId(container.getId())
                .exec().getNetworkSettings().getIpPrefixLen();
    }

    /**
     * Method converts host ip address to ip address of network or subnet.
     * Docker api doesn't store ip address of network in containers config so
     * we have to convert it from container's ip and mask. Convert uses logic
     * AND function.
     *
     * @param ip IP Address of container in target network.
     * @param mask mask of containers ip addres in target network.
     * @return Network IP Address
     */
    private String convertToNetworkIp(String ip, int mask) {
        String[] s = ip.split(Pattern.quote("."));
        ArrayList<String> binIp = new ArrayList<>(4);
         for (String octet : s) {
             binIp.add(Integer.toBinaryString(Integer.parseInt(octet)));
         }

         completeByte(binIp);
         ArrayList<String> binMask = convertCidrToBinMask(mask);
         completeByte(binMask);
         String networkIp = "";

         for (int i = 0; i < 4; i++) {
             String binNetworkIp = "";
             for (int y = 0; y < 8; y++) {
                binNetworkIp += Integer.parseInt(binIp.get(i).charAt(y) + "") &
                        Integer.parseInt(binMask.get(i).charAt(y) + "");
            }
             networkIp += String.valueOf(Integer.parseInt(binNetworkIp, 2));
            if (i != 3) {
                networkIp += ".";
            }
         }
         return networkIp;
    }

    /**
     * Method converts CIDR mask to binary mask (16 -> 11111111 11111111 00000000 00000000).
     * Binary mask is used for converting host ip to network ip.
     * @param mask CIDR mask
     * @return ArrayList with binary mask (each index is 1 bit)
     */
    private ArrayList<String> convertCidrToBinMask(int mask) {
        ArrayList<String> binMask = new ArrayList<>(4);
        String p = "";
        for (int i = 1; i <= 32; i++) {
            if (mask != 0) {
                p += "1";
                mask--;
            }
            if (i % 8 == 0) {
                binMask.add(p);
                p = "";
            }
        }
        completeByte(binMask);

        return binMask;
    }

    /**
     * Method completes byte with zero bites. Cooperates with convertCidrToBinMask method.
     * @param binMask ArrayList with binary mask
     */
    private void completeByte(ArrayList<String> binMask) {
        for (int i = 0; i < 4; i++) {
            String zeroBites = "";
            for (int y = binMask.get(i).length(); y < 8; y++) {
                zeroBites += "0";
            }
            binMask.set(i, zeroBites + binMask.get(i));
        }
    }



    public void delDefaultGateway(DockerContainer container) {
        this.runCommand(container, "ip route del default");
    }


}
