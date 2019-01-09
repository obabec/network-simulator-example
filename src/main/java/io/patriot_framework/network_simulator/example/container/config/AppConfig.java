package io.patriot_framework.network_simulator.example.container.config;

/**
 * Class representing application config can be used by user to next work with app.
 * Class includes most necessary info which is name, ip, status (running, stopped, ...).
 */
public class AppConfig {
    private String ipadd;
    private String status;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
