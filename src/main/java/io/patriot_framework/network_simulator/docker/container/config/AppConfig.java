/*
 * Copyright 2019 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.network_simulator.docker.container.config;

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
