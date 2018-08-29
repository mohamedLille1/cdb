package cdb;

public interface Props {
    String COSAMIRA_LOCAL_STORE = "cosamira.local-store";

    String COSAMIRA_EVENTS = "cosamira.events";
    String COSAMIRA_DOCKER_EVENTS = "cosamira.docker.events";
    String COSAMIRA_SENSORDATA = "cosamira.sensordata";

    int INFLUX_PORT = 8086;
    String INFLUX_HOST = "172.20.0.1";
    String INFLUX_DB = "COSAMIRA_DB";

    String EVENT_DUMMY = "dummyMeasurement";
    String EVENT_DISCOVERY = "discovery";

    String CONF_STORE_DATA = "Configure database store data";
    String CONF_STORE_MSG = "Configure database store message";
    String DECONF = "Deconfigure database";
}

interface ConfigKeys {
    String requestId = "id";
    String measurementName = "name";
    String messageType = "type";
    String bandwidthMode = "bandwidth";
    String messageInterval = "messageInterval";
    String sampleInterval = "sampleInterval";
    String channels = "channels";
    String dataType = "dataType";
    String tags = "tags";
    String topic = "topic";
}
