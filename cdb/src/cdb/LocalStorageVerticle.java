package cdb;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.hazelcast.com.eclipsesource.json.ParseException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.lang.*;

@SuppressWarnings("unused")
public class LocalStorageVerticle extends AbstractVerticle{
private String channel2Consume;
private int messageItv = 100;
private int subItv = 100000;
private boolean enable = true;
private Integer count = 0;	
private String serial;
private EventBus eb;
private JsonObject request_imu_config;
private JsonObject request_gnss_config;
private JsonObject request_spm_config;
private boolean isStream1;
private boolean isStream2;
private boolean isStream3;
private String imuTopic;
private String gnssTopic;
private String spmTopic;
private String stream1_json_path;
private String stream2_json_path;
private String stream3_json_path;

	LocalStorageVerticle(String serial, String consChannel, String args, String args2, String isStream1, String isStream2, String isStream3) throws UnsupportedEncodingException, IOException {
		this.channel2Consume = consChannel;
		this.serial = serial;
		this.messageItv = Integer.parseInt(args);
		this.subItv = Integer.parseInt(args2);
		this.isStream1 = Boolean.parseBoolean(isStream1);
		this.isStream2 = Boolean.parseBoolean(isStream2);
		this.isStream3 = Boolean.parseBoolean(isStream3);
		this.stream1_json_path = "stream1_imu.json";
		this.stream2_json_path = "stream1_gnss.json";
		this.stream3_json_path = "stream1_spm.json";
        
        this.request_imu_config = new RequestFactory().setChannelConfig(stream1_json_path, messageItv, subItv, this.isStream1);
        this.request_gnss_config = new RequestFactory().setChannelConfig(stream2_json_path, messageItv, subItv, this.isStream2);
        this.request_spm_config = new RequestFactory().setChannelConfig(stream3_json_path, messageItv, subItv, this.isStream3);
    }
	
	@Override
	public void start(Future<Void> startFuture) throws FileNotFoundException, IOException, ParseException {
		System.out.println("CosamiraDB verticle started.");
		EventBus eb = this.eb;
		eb = vertx.eventBus();
		MyFileWriter imuWriter = new MyFileWriter();
//		vertx.setPeriodic(1000, v -> {
//		vertx.setTimer(1000, v -> {
			//Json request direct entry
//			JsonObject requestSensorTopo_dentry = new JsonObject().put("id", "1662479-003.sens").put("type","Sensor configuration request").put("integrityCheck", "my_CRC");
//			
		//Get sensors configuration
//		String channels_config = new String(Files.readAllBytes(Paths.get("config_request.json")), "UTF-8");
//		JsonObject request_channels_config = new JsonObject(channels_config);
//		eb.publish("sensorconfiguration.request", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		
		//Get FPGA configuration
		String channels_config = new String(Files.readAllBytes(Paths.get("fpga_config_request.json")), "UTF-8");
		JsonObject request_channels_config = new JsonObject(channels_config);
		eb.publish(this.serial+".FPGAconfiguration", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		eb.publish(this.serial+".FPGAconfiguration.request", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
		eb.publish("FPGAconfiguration", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		eb.publish("FPGAconfiguration.request", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		eb.publish(this.serial+".fpgaconfiguration", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		eb.publish(this.serial+".fpgaconfiguration.request", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		eb.publish("fpgaconfiguration", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
//		eb.publish("fpgaconfiguration.request", request_channels_config, new DeliveryOptions().addHeader("type", "json"));
		
		//Configure channels
//		String spm_config = new String(Files.readAllBytes(Paths.get("stream1_spm.json")), "UTF-8");
//		JsonObject request_spm_config = new JsonObject(spm_config);
//		eb.publish(this.serial+".channelconfiguration.request", request_spm_config, new DeliveryOptions().addHeader("type", "json"));
//		String gnss_config = new String(Files.readAllBytes(Paths.get("stream1_gnss.json")), "UTF-8");
//		JsonObject request_gnss_config = new JsonObject(gnss_config);
//		eb.publish(this.serial+".channelconfiguration.request", request_gnss_config, new DeliveryOptions().addHeader("type", "json"));
		
//		eb.consumer(this.serial+".FPGAconfiguration", message -> {
//		  System.out.println(this.serial+".FPGAconfiguration: " + message.body().toString());
//		});
//		
//		eb.consumer(this.serial+".FPGAconfiguration.request", message -> {
//			  System.out.println(this.serial+".FPGAconfiguration.request: " + message.body().toString());
//		});
//		
//		eb.consumer("FPGAconfiguration", message -> {
//			  System.out.println("FPGAconfiguration: " + message.body().toString());
//		});
//			
//		eb.consumer("FPGAconfiguration.request", message -> {
//			  System.out.println("FPGAconfiguration.request: " + message.body().toString());
//		});
//		
//		eb.consumer("FPGAconfiguration.reply", message -> {
//			  System.out.println("FPGAconfiguration.reply: " + message.body().toString());
//		});
//		
//		eb.consumer(this.serial+".FPGAconfiguration.reply", message -> {
//			  System.out.println(this.serial+".FPGAconfiguration.reply: " + message.body().toString());
//		});
//		
//		eb.consumer("FPGAconfiguration.status", message -> {
//			  System.out.println("FPGAconfiguration.status: " + message.body().toString());
//		});
//		
//		eb.consumer(this.serial+".FPGAconfiguration.status", message -> {
//			  System.out.println(this.serial+".FPGAconfiguration.status: " + message.body().toString());
//		});
////		============================================================================================
//		
//		eb.consumer(this.serial+".fpgaconfiguration", message -> {
//		  System.out.println(this.serial+".fpgaconfiguration: " + message.body().toString());
//		});
//		
//		eb.consumer(this.serial+".fpgaconfiguration.request", message -> {
//			  System.out.println(this.serial+".fpgaconfiguration.request: " + message.body().toString());
//		});
//		
//		eb.consumer("fpgaconfiguration", message -> {
//			  System.out.println("fpgaconfiguration: " + message.body().toString());
//		});
//			
//		eb.consumer("fpgaconfiguration.request", message -> {
//			  System.out.println("fpgaconfiguration.request: " + message.body().toString());
//		});
//		
//		eb.consumer("fpgaconfiguration.reply", message -> {
//			  System.out.println("fpgaconfiguration.reply: " + message.body().toString());
//		});
//		
//		eb.consumer(this.serial+".fpgaconfiguration.reply", message -> {
//			  System.out.println(this.serial+".fpgaconfiguration.reply: " + message.body().toString());
//		});
//		
//		eb.consumer("fpgaconfiguration.status", message -> {
//			  System.out.println("fpgaconfiguration.status: " + message.body().toString());
//		});
//		
//		eb.consumer(this.serial+".fpgaconfiguration.status", message -> {
//			  System.out.println(this.serial+".fpgaconfiguration.status: " + message.body().toString());
//		});
		
		
		
		
		
		
//		
//		eb.consumer("channelconfiguration.reply", message -> {
//			  System.out.println("channelconfiguration.reply: " + message.body().toString());
//		});
////		
//		eb.consumer("cosamira.sensordata", message -> {
//			  System.out.println("cosamira.sensordata: " + message.body().toString());
//		});
//		
//		eb.consumer("cosamira.events", message -> {
//			  System.out.println("cosamira.events: " + message.body().toString());
//		});
		
//		eb.consumer("1662479-0005.sensordata.consumer", message -> {
////			String mess = message.body();
////			Byte b1 = Byte.decode(mess);
//			System.out.println("messageStart");
//			System.out.println(message.body());
//			System.out.println("messageEnd");
//		});
		
//		eb.consumer("1662479-0003.sensor", message -> {
//			System.out.println("1662479-0003.sensor: ");
////			System.out.println("1662479-0003.sensor: " + message.body().toString());
//		});
		
//		eb.consumer("1662479-0005.sensordata.consumer", message -> {
//			String buffer = message.body().toString();
//			System.out.print("got some data:");
//			System.out.print(buffer);
//			System.out.println("end of data");
////			System.out.println("1662479-0003.sensor: " + message.body().toString());
//		});
		
		
////	===============================================================================================
////	===============================================================================================
		
		
		
		eb.publish(this.serial+".channelconfiguration.request", this.request_imu_config, new DeliveryOptions().addHeader("type", "json"));
		eb.publish(this.serial+".channelconfiguration.request", this.request_gnss_config, new DeliveryOptions().addHeader("type", "json"));
		eb.publish(this.serial+".channelconfiguration.request", this.request_spm_config, new DeliveryOptions().addHeader("type", "json"));
		
		eb.<Buffer>consumer("imu.sensordata.consumer", message -> {
			int timestamp_size = 6; //bytes-1
			int spc = this.messageItv/(this.subItv/1000);
			int c_num_imu = 0;
			int c_num_gnss = 0;
			int c_num_spm = 0;
			if(this.isStream1) {
				c_num_imu = new JsonUtils(this.request_imu_config).getChannelsNumber();
			}
			if(this.isStream2) {
				c_num_gnss = new JsonUtils(this.request_gnss_config).getChannelsNumber();
			}
			if(this.isStream3) {
				c_num_spm = new JsonUtils(this.request_spm_config).getChannelsNumber();
			}
			int c_num = c_num_imu + c_num_gnss + c_num_spm;
			
			int totBits = 48+16*spc*c_num+16;
			int mess_size = message.body().getBytes().length*8;
			int mess_byte_size = message.body().getBytes().length;
			byte[] ts = message.body().getBytes(0,timestamp_size);
			
			InputStream streamMess = new ByteArrayInputStream(message.body().getBytes());
			
			
//			long timestamp = ByteBuffer.wrap(ts).order(ByteOrder.LITTLE_ENDIAN);
			
			long timestamp = longLittleEndian(message.body().getBytes(0,timestamp_size));
			
			try {
				imuWriter.writeFile("imu", streamMess);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println("===========================================================================================================");
//			System.out.print("timestamp: ");
//			System.out.print(timestamp);
//			System.out.println(" s");
//			System.out.println(new Date(new Timestamp(timestamp).getTime()));
//			System.out.print("Number of channels: ");
//			System.out.print(c_num);
//			System.out.println("");
//			System.out.print("Number of samples per channel: ");
//			System.out.print(spc);
//			System.out.println("");
//			System.out.print("theoritical message length: ");
//			System.out.print(totBits/8);
//			System.out.println(" bytes");
//			System.out.print("Measured message length: ");
//			System.out.print(mess_byte_size);
//			System.out.println(" bytes");
//			
//			//x y z, pitch roll yaw
//			double[] scaling = {12800,12800,1280,204.8,204.8,204.8};
////			for(int j=0;j<spc;j++) {
////				timestamp_size = timestamp_size + j*2;
//				int k=0;
//				for(int i=timestamp_size;i<mess_byte_size-2;i=i+2*spc) {
////					byte[] msb = message.body().getBytes(i,i+7);
////					byte[] lsb = message.body().getBytes(i+8,i+16);
//					byte[] bytes = message.body().getBytes(i,i+2);
//					float val = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
////					double value = val/scaling[k];
//					System.out.print(val);
//					System.out.print("\t\t");
//					k++;
//				}
//				byte[] crc = message.body().getBytes(mess_byte_size-2,mess_byte_size);
//				System.out.print(CRC16CCITT(crc));
//				System.out.println("");
////			}
		});
		
		eb.<Buffer>consumer("gnss.sensordata.consumer", message -> {
			int timestamp_size = 6; //bytes-1
			
			InputStream streamMess = new ByteArrayInputStream(message.body().getBytes());
			
			long timestamp = longLittleEndian(message.body().getBytes(0,timestamp_size));
			
			try {
				imuWriter.writeFile("gnss", streamMess);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		eb.<Buffer>consumer("spm.sensordata.consumer", message -> {
			int timestamp_size = 6; //bytes-1
			
			InputStream streamMess = new ByteArrayInputStream(message.body().getBytes());
			
			long timestamp = longLittleEndian(message.body().getBytes(0,timestamp_size));
			
			try {
				imuWriter.writeFile("spm", streamMess);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		System.out.println("CosamiraDB Verticle is ready.");
	}
	
	private long longLittleEndian(byte[] bytes){
        long timestamp=0;
        for (int i=bytes.length-1; i>=0; i--){
            timestamp = (timestamp << 8) + (bytes[i] & 0xff);
        }
        return timestamp;
    }

    private int CRC16CCITT(byte[] bytes){
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }

        crc &= 0xffff;
//        System.out.println("CRC16-CCITT = " + Integer.toHexString(crc));
        return crc;
    }
}
