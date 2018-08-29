package cdb;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import cdb.MyFileWriter;
import cdb.Server;
import cdb.custFormatter;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import rx.Observable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

@SuppressWarnings("unused")
public class Server {
	 private ServerSocket server;
	    private NetServer serverVertx;    
	    
	    public Server(int port) throws Exception {
	    	Vertx vertx = Vertx.vertx();
	    	NetServerOptions options = new NetServerOptions().setPort(port+1);
	    	this.serverVertx = vertx.createNetServer(options);
	    	this.serverVertx.connectHandler(new Handler<NetSocket>() {
	            @Override
	            public void handle(NetSocket netSocket) {
//	                System.out.println("Incoming connection!");
	            }
	        });
	    	
//	        if (ipAddress != null && !ipAddress.isEmpty()) 
//	            this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
//	        else 
//	            this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
	    }
	    
	    public void listen() throws Exception {
	    	MyFileWriter fw = new MyFileWriter();
	    	
	    	
	    	
	    	this.serverVertx.connectHandler(socket -> {
	    			String ip_address = socket.localAddress().toString();
//	    			String nameServer = socket.indicatedServerName().toString();
	    			System.out.println("Vertx: Incoming connection from " + ip_address);
//	    			System.out.println("Vertx: Name server: " + nameServer);
	    			socket.handler(buffer -> {
	    			    System.out.println("Vertx: I received " + buffer.length() + " bytes.");
	    			    System.out.print("Vertx: Received raw message: ");
	    			    byte[] message = buffer.getBytes();
//	    			    String messAsHex = custFormatter.bytesToHex(message);
//	    			    System.out.println(messAsHex);
	    			    
	    			    long cur_time = System.currentTimeMillis();
	    			    InputStream streamMess = new ByteArrayInputStream(message);
	    		        try {
							int fileSize = fw.writeFile(String.valueOf(cur_time), streamMess);
							System.out.println("Vertx: Wrote " + fileSize + " bytes to db.");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    			  });
	    			socket.closeHandler(v -> {
	    	    		  System.out.println("Vertx: The socket has been closed");
	    	    		});
	    		});
	    	
	    	this.serverVertx.listen(res -> {
	  		  if (res.succeeded()) {
	  			    System.out.println("Vertx: Server is now listening!");
	  			  } else {
	  			    System.out.println("Vertx: Failed to bind!");
	  			  }
	  			});
	    }
	    
	    public InetAddress getSocketAddress() {
	        return this.server.getInetAddress();
	    }
	    
	    public int getPort() {
	        return this.server.getLocalPort();
	    }
	    
	    public static void main(String[] args) throws Exception {
//	        MyServerSocketBinary app = new MyServerSocketBinary(Integer.parseInt(args[0]));
	    	Server app = new Server(4859);
	        while(true) {
	            app.listen();
	        }
	    }
	        
	    
}
