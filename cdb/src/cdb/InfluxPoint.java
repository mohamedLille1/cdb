package cdb;

import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by wkerckho on 13/07/2017.
 */
@SuppressWarnings("unused")
public class InfluxPoint {

    public static final InfluxPoint EMPTY_POINT = new InfluxPoint("", -1L, new JsonObject());

    protected final String measurementId;
    protected final long timestamp;
    protected final JsonObject fields;
    protected final JsonObject tags;

    public InfluxPoint(String measurementId, long timestamp, JsonObject fields) {
        this(measurementId, timestamp, fields, new JsonObject());
    }

    public InfluxPoint(String measurementId, long timestamp, JsonObject fields, JsonObject tags) {
        this.measurementId = Util.getSafeMeasurementId(measurementId);
        this.timestamp = timestamp;
        this.fields = fields;
        this.tags = tags;
    }

    public String getMeasurementId() {
        return measurementId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public JsonObject getFields() {
        return fields;
    }

    public JsonObject getTags() {
        return tags;
    }

    @Override
    public String toString() {
        String tagsPart = tags.size() > 0 ? "," + tags.stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(",")) : "";
        String timePart = " " + TimeUnit.NANOSECONDS.convert(timestamp, TimeUnit.MICROSECONDS);
        return measurementId + tagsPart + " " + fields.stream().map(e -> e.getKey() + "=" + jsonValToString(e.getValue())).collect(Collectors.joining(",")) + timePart;
    }

    private String jsonValToString(Object val) {
        return (val instanceof String) ? "\"" + val + "\"" : val.toString();
    }

}
