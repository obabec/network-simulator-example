package com.redhat.patriot.network_simulator.example.manager;

import com.redhat.patriot.network_simulator.example.container.IContainer;
import com.redhat.patriot.network_simulator.example.network.INetwork;

public interface IManager {
    IContainer createContainerResponse();
    INetwork createNetwork();

}
