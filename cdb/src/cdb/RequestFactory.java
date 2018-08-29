package cdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.vertx.core.json.JsonObject;

public class RequestFactory {
	public JsonObject setChannelConfig(String path, int messageItv, int subItv, boolean streamEnabled) {
		JsonObject request_config = null;
		String config;
		try {
			
			config = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
			request_config = new JsonObject(config);
			request_config = new JsonUtils(request_config).setMessageItv(messageItv);
			request_config =  new JsonUtils(request_config).setSubSampleItv(subItv);
			if(streamEnabled) {request_config =  new JsonUtils(request_config).enableStream();}
			else {request_config =  new JsonUtils(request_config).disableStream();}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new DisplayFactory().configMessage(request_config);
		return request_config;
	}
}
