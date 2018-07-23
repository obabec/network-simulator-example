package com.redhat.patriot.network_simulator.example.container.config;

public class AppConfig {
    private String ipadd;
    private String status;
    private String containerId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public AppConfig(String ipadd, String status, String containerId) {
        this.ipadd = ipadd;
        this.status = status;
        this.containerId = containerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppConfig(String ipadd, String status) {
        this.ipadd = ipadd;
        this.status = status;
    }

    public AppConfig(String ipadd) {
        this.ipadd = ipadd;
    }

    public String getIpadd() {
        return ipadd;
    }

    public void setIpadd(String ipadd) {
        this.ipadd = ipadd;
    }
}
