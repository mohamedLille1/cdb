package cdb;

public class Util {

    public static String getSafeMeasurementId(String id) {
        return id.replace(".", "_");
    }

}
