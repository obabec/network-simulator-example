package io.patriot_framework.network_simulator.example.container.config;

public class AppConfig {
    private String ipadd;
    private String status;
    private String name;
    private String startCommand;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public AppConfig(String name, String startCommand) {
        this.name = name;
        this.startCommand = startCommand;
    }

    public AppConfig(String ipadd, String status, String name) {
        this.ipadd = ipadd;
        this.status = status;
        this.name = name;
    }
    public AppConfig(String name) {
        this.name = name;
    }

    public String getIPAdd() {
        return ipadd;
    }

    public void setIPAdd(String ipadd) {
        this.ipadd = ipadd;
    }
}
