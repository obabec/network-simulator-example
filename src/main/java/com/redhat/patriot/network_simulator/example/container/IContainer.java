package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.manager.IManager;
import com.redhat.patriot.network_simulator.example.network.INetwork;

public interface IContainer {

    IManager getManager();
    void setManager(IManager dockerManager);
    boolean isAlive();
    boolean exist();
    INetwork connectToNetwork(INetwork network);
    void destroyContainer();

}
