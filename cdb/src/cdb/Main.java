package cdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import rx.Observable;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) {
		String serial = System.getenv("SERIAL");
        if(serial == null){
            System.err.println("No device serial specified (SERIAL env-var), shutting down.");
            return;
        }
        
        String hostIp;
        if (System.getenv("PUBLIC_HOST") != null)
            hostIp = System.getenv("PUBLIC_HOST");
        else if(System.getenv("INTERFACE") != null) {
            try {
                hostIp = getHostIPv4FromInterface(System.getenv("INTERFACE"));

            } catch (SocketException e) {
                System.err.println("Can't retrieve host address from interface: " + e.getMessage());
                return;
            }
        }
        else{
            System.err.println("No interface or host_ip specified (INTERFACE | PUBLIC_HOST env-var), shutting down.");
            return;
        }
        
        Config hazelcastConfig = new XmlConfigBuilder(Main.class.getResourceAsStream("/cluster.xml")).build();
        hazelcastConfig.getNetworkConfig().getInterfaces().addInterface(hostIp);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().getTrustedInterfaces().add(hostIp);
        hazelcastConfig.getNetworkConfig().setPublicAddress(hostIp);
        
        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions()
                .setClusterManager(mgr)
                .setClustered(true)
                .setClusterHost(hostIp);
        
        Vertx.clusteredVertx(options, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                Vertx vertx = vertxAsyncResult.result();
                ConfigRetriever retriever = ConfigRetriever.create(vertx,
                        new ConfigRetrieverOptions().addStore(
                                new ConfigStoreOptions().setType("env")));

                retriever.getConfig(ar -> {
                    if (ar.failed()) {
                        System.err.println("Couldn't retrieve configuration.");
                    } else {
                        JsonObject config = ar.result();
//                        String influxHost = config.getString("INFLUX_HOST","127.0.0.1");
//                        String influxDb = config.getString("INFLUX_DB", "COSAMIRA_DB");
//                        Integer influxPort = config.getInteger("INFLUX_PORT", 8086);

//                        System.out.println("Influx config:\t" + influxHost + ":" + influxPort + "\t " + influxDb);
                        System.out.println("Deploying LocalStorageVerticle");

                        vertx.eventBus().consumer("test", System.out::println);
                        try {
							vertx.deployVerticle(new LocalStorageVerticle(serial, args[0], args[1], args[2], args[3], args[4], args[5]));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

                        // Create and register the EventbusService;
//                        QueryService service = new QueryServiceImpl(vertx, influxHost,influxPort,influxDb);
//                        new ServiceBinder(vertx).setAddress(serial + ".database.query-service").register(QueryService.class, service);
                    }
                });
            } else {
                System.err.println("Failed " + vertxAsyncResult.cause());
            }
        });

	}
	
	 private static String getHostIPv4FromInterface(String interfaceName) throws SocketException {
	        return Observable.from(Collections.list(NetworkInterface.getByName(interfaceName).getInetAddresses()))
	                .filter(inetAddress -> inetAddress.getHostAddress().length() < "255.255.255.255".length())
	                .map(InetAddress::getHostAddress)
	                .toBlocking().first();
	    }
}
