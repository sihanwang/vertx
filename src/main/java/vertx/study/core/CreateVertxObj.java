package vertx.study.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class CreateVertxObj {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		
		Context context = vertx.getOrCreateContext();
		
		if (context.isEventLoopContext()) {
			  System.out.println("Context attached to Event Loop");
			} else if (context.isWorkerContext()) {
			  System.out.println("Context attached to Worker Thread");
			} else if (context.isMultiThreadedWorkerContext()) {
			  System.out.println("Context attached to Worker Thread - multi threaded worker");
			} else if (! Context.isOnVertxThread()) {
			  System.out.println("Context not attached to a thread managed by vert.x");
			}
		
		
		System.out.println("Main thread:"+Thread.currentThread().getName());
		
		
		Verticle myVerticle = new MyVerticle();
		
		JsonObject config = new JsonObject().put("name", "tim").put("directory", "/blah");
		DeploymentOptions options = new DeploymentOptions().setConfig(config);
		
		
		vertx.deployVerticle(myVerticle, options,res -> {
			  if (res.succeeded()) {
			    System.out.println("Deployment id is: " + res.result());
			  } else {
			    System.out.println("Deployment failed!");
			  }
			  
			  System.out.println("MyVerticle deployVerticle done thread:"+Thread.currentThread().getName());
			});
		
		
		
		
		//Run periodic code
		vertx.setPeriodic(1000, id -> {
			  // This handler will get called every second

			  System.out.println("timer fired! with timer id:"+id);
			  System.out.println("Periodic thread:"+Thread.currentThread().getName());
			});
		
		
		//Execute blocking code
		vertx.executeBlocking(future -> {
			  // Call some blocking API that takes a significant amount of time to return
			  try {
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			  future.complete("Done");
			  System.out.println("executeBlocking thread:"+Thread.currentThread().getName());
			  
			},
			
			res -> {
			  System.out.println("The result is: " + res.result());
			  System.out.println("executeBlocking done thread:"+Thread.currentThread().getName());
			});
			
			
	}

}

