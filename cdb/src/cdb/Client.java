package cdb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.rx.java.RxHelper;
import rx.Completable;
import rx.Subscription;
import rx.Observable;

@SuppressWarnings("unused")
public class Client {
	private InetAddress gserverAdress;
	private int gserverPort;
	private boolean compression = false;
	private Socket socket;
	private NetSocket socketVertx;
	private NetSocket socketVertx_forWriting;
    private NetClient clientVertx;
    private int timeout = 10000; //in ms
    private int reconnectAttempts = 10; //number of reconnection attempts
    private int reconnectIntervals = 500;//in ms
    private Vertx vertx;
    
    Client(InetAddress serverAddress, int serverPort) throws Exception {
    	gserverAdress = serverAddress;
    	gserverPort = serverPort;
    	this.vertx = Vertx.vertx();
    	NetClientOptions options = new NetClientOptions().setConnectTimeout(timeout);
    	options.setReconnectAttempts(reconnectAttempts);
    	options.setReconnectInterval(reconnectIntervals);
    	this.clientVertx = vertx.createNetClient(options);
    }
    
    public void init() {
    	this.clientVertx.connect(gserverPort+1, gserverAdress.getHostAddress(), res -> {
    		  if (res.succeeded()) {
    		    this.socketVertx = res.result();
    		    this.socketVertx.pause();
    		  }
      	});
    }
    
    public void send(long timestamp, double value) throws IOException {	
    	
    	byte[] data = new byte[16];
    	
    	byte[] b_timestamp = ByteBuffer.allocate(8).putLong(timestamp).array();
    	byte[] b_value = ByteBuffer.allocate(8).putDouble(value).array();
    	
    	data = custFormatter.byteCat(b_timestamp,b_value);
    	
    	Buffer bufferVertx = Buffer.buffer(data);
    	
    	this.clientVertx.connect(gserverPort+1, gserverAdress.getHostAddress(), res -> {
  		  if (res.succeeded()) {
  		    this.socketVertx = res.result();
  			String ipAddress = this.socketVertx.remoteAddress().toString();
  		    System.out.println("Vertx: Connected to server: "+ipAddress);
  		    this.socketVertx.write(bufferVertx);
  		    System.out.println("Vertx: Sent " + bufferVertx.getBytes().length + " bytes.");
  		    System.out.print("Vertx: Sent raw message: ");
  		    String messAsHex = custFormatter.bytesToHex(bufferVertx.getBytes());
  		    System.out.println(messAsHex);
  		  } else {
  		    System.out.println("Vertx: Failed to connect: " + res.cause().getMessage());
  		  }
    	});
    }
    
    public void send(long timestamp[], double value[]) throws IOException {
    	int datasize = 0;
    	ByteArrayOutputStream outstream = new ByteArrayOutputStream( );
		for (int i=0;i<timestamp.length;i++) {
			byte[] b_timestamp = ByteBuffer.allocate(8).putLong(timestamp[i]).array();
	    	byte[] b_value = ByteBuffer.allocate(8).putDouble(value[i]).array();
	    	byte[] data = custFormatter.byteCat(b_timestamp,b_value);
	    	if(compression==true) {
//	    		byte[] datac = Snappy.compress(data);
//				this.socket.getOutputStream().write(datac);
//				datasize = datasize + datac.length;
	    	}
	    	else {
				this.socket.getOutputStream().write(data);
				datasize = datasize + data.length;
	    	}
		}
		
		byte[] data2insert = outstream.toByteArray();
    	
    	for (byte i : data2insert) {
            this.socket.getOutputStream().write(i);
        }
        
        System.out.println("\r\n"+System.currentTimeMillis()+" Sent " + datasize + " bytes to server.");
    }
    
    public void send(byte[] data) throws IOException {
    	ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    	Buffer bufferVertx = Buffer.buffer(data);
//    	this.socketVertx.resume();
//    	this.socketVertx.write(bufferVertx).end();
    	this.clientVertx.connect(gserverPort+1, gserverAdress.getHostAddress(), res -> {
    		  if (res.succeeded()) {
    		    this.socketVertx = res.result();
//    			String ipAddress = this.socketVertx.remoteAddress().toString();
//    		    System.out.println("Vertx: Connected to server: "+ipAddress);
    		    this.socketVertx.write(bufferVertx);
//    		    System.out.println("Vertx: Sent " + bufferVertx.getBytes().length + " bytes.");
//    		    System.out.print("Vertx: Sent raw message: ");
//    		    String messAsHex = custFormatter.bytesToHex(bufferVertx.getBytes());
//    		    System.out.println(messAsHex);
//    		    this.socketVertx.close();
    		  } else {
//    		    System.out.println("Vertx: Failed to connect: " + res.cause().getMessage());
    		  }
      	});
//    	this.socketVertx_forWriting.write(bufferVertx);
//    	this.clientVertx.close();
    }
    
    public void sendFromCsv(String filename) throws IOException {
    	String separator = ",";
    	File file= new File(filename);
    	
    	List<Long> timestamps = new ArrayList<>();
        List<Double> values = new ArrayList<>();
    	
    	Scanner inputStream;
    	
    	try{
            inputStream = new Scanner(file);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] line_content = line.split(separator);
                // this adds the currently parsed line to the 2-dimensional string array
                
                timestamps.add(Long.parseLong(line_content[0]));
                values.add(Double.parseDouble(line_content[1]));
            }

            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    	
    	long[] l_timestamps = new long[timestamps.size()];
    	double[] d_values = new double[values.size()];
    	
    	this.send(l_timestamps, d_values);
    }
    
    public void emulSens(int channels,int subsamples, int duration) throws IOException {
    	
    	double[] values_line = new double[channels];
    	
    	for(int i=0;i<channels;i++) {
    		values_line[i] = 154.00;
    	}
    	
    	ByteArrayOutputStream outstream = new ByteArrayOutputStream( );
		for (double val : values_line) {
			byte[] tobuf = ByteBuffer.allocate(8).putDouble(val).array();
			outstream.write(tobuf);
		}
		byte[] line2insert = outstream.toByteArray();
		byte[] time2insert = ByteBuffer.allocate(8).putLong(154444L).array();
		
		//expandable byte array to hold entire message
		ByteArrayOutputStream fullmessStream = new ByteArrayOutputStream( );
		
    	for(int i=0;i<subsamples*duration;i++) {
    		byte[] input = custFormatter.byteCat(time2insert,line2insert);
    		   
    		  // Compressor with highest level of compression
    		  Deflater compressor = new Deflater();
    		  compressor.setLevel(Deflater.BEST_COMPRESSION);
    		   
    		  // Give the compressor the data to compress
    		  compressor.setInput(input);
    		  compressor.finish();
    		   
    		  // Create an expandable byte array to hold the compressed data.
    		  // It is not necessary that the compressed data will be smaller than
    		  // the uncompressed data.
    		  ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
    		   
    		  // Compress the data
    		  byte[] buf = new byte[1024];
    		  while (!compressor.finished()) {
    		      int count = compressor.deflate(buf);
    		      bos.write(buf, 0, count);
    		  }
    		  try {
    		      bos.close();
    		  } catch (IOException e) {
    		  }
    		   
    		  // Get the compressed data
    		  byte[] compressedData = bos.toByteArray();
    		
    		  fullmessStream.write(compressedData);
    		  
    		  
    		  
//    		this.socket.getOutputStream().write(compressedData);
//    		System.out.println("\r\n"+System.currentTimeMillis()+" Sent " + compressedData.length + " bytes to server.");
    	}
    	
    	Buffer bufferVertx = Buffer.buffer(fullmessStream.toByteArray());
    	
    	this.clientVertx.connect(gserverPort+1, gserverAdress.getHostAddress(), res -> {
  		  if (res.succeeded()) {
  		    this.socketVertx = res.result();
  			String ipAddress = this.socketVertx.remoteAddress().toString();
  		    System.out.println("Vertx: Connected to server: "+ipAddress);
  		    this.socketVertx.write(bufferVertx);
  		    System.out.println("Vertx: Sent " + bufferVertx.getBytes().length + " bytes.");
//  		    System.out.print("Vertx: Sent raw message: ");
//  		    String messAsHex = custFormatter.bytesToHex(bufferVertx.getBytes());
//  		    System.out.println(messAsHex);
  		  } else {
  		    System.out.println("Vertx: Failed to connect: " + res.cause().getMessage());
  		  }
    	});
    }
    
    public void readBus() {
    	// Registering Consumer for the Cosamira Event Logger
        RxHelper.toObservable(this.vertx.eventBus().<JsonObject>consumer(Props.COSAMIRA_EVENTS).bodyStream())
                .doOnNext(System.out::println) // print to std out for debugging
                .buffer(5, TimeUnit.SECONDS);
    }
    /**
     * Requires 3 arguments:
     *     1: TCP/IP server host name or IP address
     *     2: TCP/IP server port number
     *     3: Absolute path and file name of file to send
     *     
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {                        
//        MyClientSocketBinary client = new MyClientSocketBinary(
//                InetAddress.getByName(args[0]), 
//                Integer.parseInt(args[1]));
//        Client client = new Client(
//                InetAddress.getByName(args[0]), 
//                4859);
        Client client = new Client(
                InetAddress.getByName("172.16.100.3"), 
                4859);
//        client.send(Long.parseLong(args[2]),Double.parseDouble(args[3]));
        
        byte[] dummy = {1,5,4};
        client.send(dummy);
//        client.send(15654654L,266.23);
//        client.sendFromCsv(args[3]);
//        client.emulSens(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
//        client.emulSens(24, 1000, 10);
//        client.readBus();
    }
}
