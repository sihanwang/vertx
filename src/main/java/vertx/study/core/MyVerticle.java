package vertx.study.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;

class MyVerticle extends AbstractVerticle {

	  // Called when verticle is deployed
	  public void start() {
		  System.out.println("Configuration: " + config().getString("name"));
		  System.out.println("My verticle ");
		  
		  if (context.isEventLoopContext()) {
			  System.out.println("Context attached to Event Loop");
			} else if (context.isWorkerContext()) {
			  System.out.println("Context attached to Worker Thread");
			} else if (context.isMultiThreadedWorkerContext()) {
			  System.out.println("Context attached to Worker Thread - multi threaded worker");
			} else if (! Context.isOnVertxThread()) {
			  System.out.println("Context not attached to a thread managed by vert.x");
			}
		  
		  System.out.println("MyVerticle start thread:"+Thread.currentThread().getName());
		  
		  context.put("data", "hello");
		  context.runOnContext((v) -> {
		    String hello = context.get("data");
		    System.out.println("context thread:"+Thread.currentThread().getName());
		    
		  });
		  
	  }

	  // Optional - called when verticle is undeployed
	  public void stop() {
	  }

	}