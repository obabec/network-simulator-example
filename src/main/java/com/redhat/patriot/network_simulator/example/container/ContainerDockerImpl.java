package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.manager.DockerManagerImpl;
import com.redhat.patriot.network_simulator.example.manager.IManager;
import com.redhat.patriot.network_simulator.example.network.INetwork;

public class ContainerDockerImpl implements IContainer {
    String name;
    String id;
    DockerManagerImpl dockerManager;

    public ContainerDockerImpl(String name, String id, DockerManagerImpl dockerManager) {
        this.name = name;
        this.id = id;
        this.dockerManager = dockerManager;
    }

    @Override
    public IManager getManager() {
        return dockerManager;
    }

    @Override
    public void setManager(IManager iManager) {
        this.dockerManager = (DockerManagerImpl) iManager;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public INetwork connectToNetwork(INetwork network) {
        return null;
    }

    @Override
    public void destroyContainer() {

    }

}
