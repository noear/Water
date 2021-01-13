package org.noear.water.integration.solon;

import org.noear.solon.core.LoadBalance;

public class WaterUpstreamFactoryImp implements LoadBalance.Factory {
    @Override
    public LoadBalance create(String service) {
        return WaterUpstream.get(service);
    }
}