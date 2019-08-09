package com.gm.domain;

import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;

public class Topology {
    private String topologyName;
    private Config conf;
    private StormTopology stormTopology;

    public String getTopologyName() {
        return topologyName;
    }

    public void setTopologyName(String topologyName) {
        this.topologyName = topologyName;
    }

    public Config getConf() {
        return conf;
    }

    public void setConf(Config conf) {
        this.conf = conf;
    }

    public StormTopology getStormTopology() {
        return stormTopology;
    }

    public void setStormTopology(StormTopology stormTopology) {
        this.stormTopology = stormTopology;
    }
}
