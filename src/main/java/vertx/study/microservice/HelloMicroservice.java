package vertx.study.microservice;

import java.util.Random;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;

public class HelloMicroservice extends AbstractVerticle {
	
	
	public static void main(String[] args)
	{
		
		VertxOptions options = new VertxOptions().setClusterManager(new InfinispanClusterManager());

		
		HelloMicroservice hm=new HelloMicroservice();
		
		Vertx.clusteredVertx(options, res -> {
			  if (res.succeeded()) {
				hm.vertx = res.result();
			    EventBus eventBus = hm.vertx.eventBus();
			    System.out.println("We now have a clustered event bus: " + eventBus);
			    hm.vertx.deployVerticle(hm);
			    
			  } else {
			    System.out.println("Failed: " + res.cause());
			  }
			});

		
	}


    @Override
    public void start() {
        // Receive message from the address 'hello'
        vertx.eventBus().<String>consumer("hello", message -> {
        	
        	Random random = new Random();
        	int chaos = random.nextInt(10);
        	
            JsonObject json = new JsonObject()
                .put("served-by", this.toString());
            
            if (chaos < 6) {
                // Normal behavior
                if (message.body().isEmpty()) {
                    message.reply(json.put("message", "hello"));
                } else {
                    message.reply(json.put("message", "hello " + message.body()));
                }
            } else if (chaos < 9) {
                System.out.println("Returning a failure");
                // Reply with a failure
                message.fail(500, "message processing failure");
            } else {
                System.out.println("Not replying");
                // Just do not reply, leading to a timeout on the consumer side.
            }
            
        });
    }

}
