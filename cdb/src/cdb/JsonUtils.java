package cdb;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonUtils {
	private JsonObject json;
	private String messageItvRegEx = "messageInterval";
	private String topic  = "topic";
	private String stream = "stream";
	private String subsampleItvRegEx = "subsampleInterval";
	
	JsonUtils(JsonObject json) {
		this.json = json;
    }
	
	public boolean isStream() {
		boolean streamEnabled = (boolean) json.getValue("enable");
		return streamEnabled;
	}
	
	public JsonObject enableStream() {
		this.json = this.json.put("enable", true);
		return this.json;
	}
	
	public JsonObject disableStream() {
		this.json = this.json.put("enable", false);
		return this.json;
	}
	
	public JsonObject toggleStream() {
		boolean isStream = this.isStream();
		if(isStream) {
			this.json = this.json.put("enable", false);
		}else {
			this.json = this.json.put("enable", true);
		}
		return this.json;
	}
	
	public JsonObject setMessageItv(int itv) {
		this.json = this.json.put(this.messageItvRegEx, itv);
		return this.json;
	}
	
	public JsonObject setSubSampleItv(int itv) {
		
		String regex = this.subsampleItvRegEx;
		
		int start_idx = 0;
		int stop_idx = 0;
		String jstring = this.json.toString();
		int k=0;
		while(start_idx!=-1) {
			start_idx = jstring.indexOf(regex,stop_idx);
			if(start_idx!=-1) {
				
				int i = start_idx;
				while (i < jstring.length() && !Character.isDigit(jstring.charAt(i))) i++;
				int j = i;
				while (j < jstring.length() && Character.isDigit(jstring.charAt(j))) j++;
				
				start_idx = start_idx + regex.length()+2;
				stop_idx = j;
				
				jstring = jstring.substring(0,start_idx)+String.valueOf(itv)+jstring.substring(stop_idx,jstring.length());
				k++;
			}
		}
		System.out.print("subsample message interval has changed for ");
		System.out.print(k);
		if(k>1) {
			System.out.println(" channels.");
		} else {
			System.out.println(" channel.");
		}
		
		this.json = new JsonObject(jstring); 
		return this.json;
	}
	
	
	public int getMessageItv() {
		JsonObject json = this.json;
		int itv = json.getInteger(messageItvRegEx);
		return itv;
	}
	
	public int getSubsampleItv() {
		String regex = this.subsampleItvRegEx;
		
		int start_idx = 0;
		int stop_idx = 0;
		String jstring = this.json.toString();
				
		start_idx = jstring.indexOf(regex,stop_idx);
				
		int i = start_idx;
		while (i < jstring.length() && !Character.isDigit(jstring.charAt(i))) i++;
		int j = i;
		while (j < jstring.length() && Character.isDigit(jstring.charAt(j))) j++;
		
		start_idx = start_idx + regex.length()+2;
		stop_idx = j;
		
		String itv = jstring.substring(start_idx,stop_idx);
		int itv_ = Integer.parseInt(itv);
		
		return itv_;
	}
	
	public String getTopic() {
		JsonObject json = this.json;
		String topic = json.getString(this.topic);
		return topic;
	}
	
	public int getChannelsNumber() {
		JsonObject json = this.json;
		JsonArray streamArray = json.getJsonArray(stream);
		int cnumber = streamArray.size();
		return cnumber;
	}
}
