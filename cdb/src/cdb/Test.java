package cdb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;



import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonParser;

@SuppressWarnings("unused")
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonObject vertx_imu_conf = new JsonObject();
		
		try {
			String content = new String(Files.readAllBytes(Paths.get("./stream1_gnss.json")), "UTF-8");
			vertx_imu_conf = new JsonObject(content);
//			JsonUtils jsonUtils = new JsonUtils();
		
	
			
			int start_idx = 0;
			int stop_idx = 0;
			String jstring = vertx_imu_conf.toString();
//			System.out.println(jstring);
//			String fd = "dasd45dasdas";
//			int i = 0;
//			while (i < fd.length() && !Character.isDigit(fd.charAt(i))) i++;
//			int j = i;
//			while (j < fd.length() && Character.isDigit(fd.charAt(j))) j++;
//			System.out.println(i);
//			System.out.println(j);
			
//			System.out.println(Integer.parseInt(fd.substring(i, j)));
			
//			String regex = "subsampleInterval";
//			int k = 0;
//			int itv = 154;
//			while(start_idx!=-1) {
//				start_idx = jstring.indexOf(regex,stop_idx);
//				if(start_idx!=-1) {
//					
//					int i = start_idx;
//					while (i < jstring.length() && !Character.isDigit(jstring.charAt(i))) i++;
//					int j = i;
//					while (j < jstring.length() && Character.isDigit(jstring.charAt(j))) j++;
//					
//					
//					start_idx = start_idx + regex.length()+2;
//					stop_idx = start_idx;
//					
//					k++;
//					
////					stop_idx = jstring.indexOf("\n",start_idx);
//					jstring = jstring.substring(0,start_idx)+String.valueOf(itv)+jstring.substring(start_idx+String.valueOf(itv).length()+3,jstring.length());
//				}
//			}
			
			vertx_imu_conf = new JsonUtils(vertx_imu_conf).setMessageItv(10);
			vertx_imu_conf = new JsonUtils(vertx_imu_conf).setSubSampleItv(222222);
			
			System.out.println(new JsonUtils(vertx_imu_conf).getMessageItv());
			
			System.out.println(new JsonUtils(vertx_imu_conf).getSubsampleItv());
			
			System.out.println(new JsonUtils(vertx_imu_conf).getChannelsNumber());
			
			System.out.println(new JsonUtils(vertx_imu_conf).getTopic());
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
    }
		
}


