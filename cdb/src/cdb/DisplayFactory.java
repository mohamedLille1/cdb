package cdb;

import io.vertx.core.json.JsonObject;

public class DisplayFactory {
	public void configMessage(JsonObject json) {
		System.out.println("==============================================================");
		System.out.print(new JsonUtils(json).getChannelsNumber());
		System.out.print(" channel(s) have been ");
		System.out.print("configured on topic: ");
		System.out.println(new JsonUtils(json).getTopic());
		System.out.print("Message interval: ");
		System.out.print(new JsonUtils(json).getMessageItv());
		System.out.println(" ms");
		System.out.print("Subsample interval: ");
		System.out.print(new JsonUtils(json).getSubsampleItv());
		System.out.println(" ms");
		System.out.print("Streaming: ");
		System.out.println(new JsonUtils(json).isStream());
		System.out.println("==============================================================");
	}
	
	public void introMessage() {
		
	}
}
