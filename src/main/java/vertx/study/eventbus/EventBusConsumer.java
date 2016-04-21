package vertx.study.eventbus;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class EventBusConsumer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		EventBus eb = vertx.eventBus();

		/*
		MessageConsumer<String> consumer1 = eb.consumer("news.uk.sport");
		consumer.completionHandler(res -> {
			  if (res.succeeded()) {
			    System.out.println("The handler registration has reached all nodes"+res.toString());
			  } else {
			    System.out.println("Registration failed!");
			  }
			});
		*/
		
		/*
		consumer1.handler(message -> {
			
		  System.out.println("I have received a message: " +  message.headers().toString()  +"|"+ message.body());
		  System.out.println("Handler 1 thread:"+Thread.currentThread().getName());
		});
		
		*/
		
		MessageConsumer<String> consumer2 = eb.consumer("news.uk.sport");
		consumer2.handler(message -> {
			System.out.println("I have received a message: "+  message.headers().toString() +"|" + message.body());
			System.out.println("Handler 2 thread:"+Thread.currentThread().getName());
			message.reply("This is a reply");
			});
		
		//eb.publish("news.uk.sport", "Yay! Someone kicked a ball");

		DeliveryOptions options = new DeliveryOptions();
		options.addHeader("some-header", "some-value");
		eb.send("news.uk.sport", "Yay! Someone kicked a ball",ar -> {
			  if (ar.succeeded()) {
				    System.out.println("Received reply: " + ar.result().body());
				  }
				});
	}
	

}
