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

package io.patriot_framework.network_simulator.docker.ipv4;

import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IPAddressTest {
    @Test
    public void testNetworkIpConvert() {
        String hostAddress = "192.168.1.5";
        int mask = 24;
        String networkAdd = "192.168.1.0";

        DockerManager dockerManager = new DockerManager();
        Assertions.assertTrue(networkAdd.equals(dockerManager.convertToNetworkIp(hostAddress, mask)));
    }
}
