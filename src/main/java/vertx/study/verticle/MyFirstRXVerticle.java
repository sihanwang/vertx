package vertx.study.verticle;

import io.vertx.rxjava.core.Vertx;
import io.vertx.core.VertxOptions;
//We use the .rxjava. package containing the RX-ified APIs
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.http.HttpServer;
import rx.Observable;

public class MyFirstRXVerticle extends AbstractVerticle {
	
	public static void main(String[] args)
	{
		MyFirstRXVerticle MFRXV=new MyFirstRXVerticle();
		MFRXV.vertx=Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		
		Observable<String> deployment = RxHelper.deployVerticle(MFRXV.vertx, MFRXV);

		deployment.subscribe(id -> {
		  System.out.println("Deployed");
		}, err -> {
			System.out.println("Deployment failed");
		});

	}
	
	
	@Override
	public void start() {
		HttpServer server = vertx.createHttpServer();
		// We get the stream of request as Observable
		server.requestStream().toObservable().subscribe(req ->
		// for each HTTP request, this method is called
		req.response().end("Hello from " + Thread.currentThread().getName()));
		// We start the server using rxListen returning a
		// Single of HTTP server. We need to subscribe to
		// trigger the operation
		server.rxListen(8080).subscribe();
	}
}