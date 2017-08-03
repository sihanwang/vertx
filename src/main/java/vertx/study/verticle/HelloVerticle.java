package vertx.study.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * A simple verticle starting a HTTP server and returning "Hello from Vert.x".
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HelloVerticle extends AbstractVerticle {
	
	public static void main(String[] args)
	{
		HelloVerticle hv=new HelloVerticle();
		hv.vertx=Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		hv.vertx.deployVerticle(hv);
	}

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
            .requestHandler(req -> {
                req.response().end("Hello from Vert.x");
            })
            .listen(8080);
    }

}
