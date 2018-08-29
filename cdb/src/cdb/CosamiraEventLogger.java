package cdb;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class CosamiraEventLogger {

    private String device;
    private String service;

    public CosamiraEventLogger(String device, String service) {
        this.device = device;
        this.service = service;
    }

    public void log(Vertx vertx, String message){
        assert vertx != null;
        vertx.eventBus().publish("cosamira.events", new JsonObject()
                .put("timestamp", System.currentTimeMillis()*1000)
                .put("device", device)
                .put("service", service)
                .put("message", message));
    }
}
