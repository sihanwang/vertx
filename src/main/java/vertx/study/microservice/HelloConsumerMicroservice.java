package vertx.study.microservice;

import io.vertx.rxjava.core.Vertx;

import java.util.concurrent.TimeUnit;

import io.vertx.core.VertxOptions;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Observable;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VertxOptions options = new VertxOptions()
				.setClusterManager(new InfinispanClusterManager());

		HelloConsumerMicroservice hcm = new HelloConsumerMicroservice();

		Vertx.rxClusteredVertx(options).subscribe(vertx -> {
			hcm.vertx = vertx;
			Observable<String> deployment = RxHelper.deployVerticle(hcm.vertx, hcm);
			deployment.subscribe(id -> {
				System.out.println("Deployed");
			}, err -> {
				System.out.println("Deployment failed");
			});

		});
	}

	@Override
	public void start() {
		vertx.createHttpServer().requestHandler(req -> {

			System.out.println("Request received");
			Single<JsonObject> obs1 = vertx.eventBus().
                    <JsonObject>rxSend("hello", "Luke")
                    .subscribeOn(RxHelper.scheduler(vertx))
                    .timeout(3, TimeUnit.SECONDS)
                    .retry((i, t) -> {
                        System.out.println("Retrying... because of " + t.getMessage());
                        return true;
                    })
                    .map(Message::body);
                Single<JsonObject> obs2 = vertx.eventBus().
                    <JsonObject>rxSend("hello", "Leia")
                    .subscribeOn(RxHelper.scheduler(vertx))
                    .timeout(3, TimeUnit.SECONDS)
                    .retry((i, t) -> {
                        System.out.println("Retrying... because of " + t.getMessage());
                        return true;
                    })
                    .map(Message::body);

                Single
                    .zip(obs1, obs2, (luke, leia) ->
                        new JsonObject()
                            .put("Luke", luke.getString("message")
                                + " from " + luke.getString("served-by"))
                            .put("Leia", leia.getString("message")
                                + " from " + leia.getString("served-by"))
                    )
                    .subscribe(
                        x -> req.response().end(x.encodePrettily()),
                        t -> req.response().setStatusCode(500).end(t.getMessage())
                    );
		}).listen(8082);
	}

}
