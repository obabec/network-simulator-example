package com.redhat.patriot.network_simulator.example.network;

import com.redhat.patriot.network_simulator.example.manager.IManager;

public interface INetwork {
    void exist(IManager dockerManager);
    void destroyNetwork(IManager dockerManager);
}
