package vertx.study.reactive;

import io.vertx.rxjava.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.rxjava.ext.sql.SQLConnection;
import io.vertx.rxjava.ext.sql.SQLRowStream;
import rx.Single;

public class RxjavaDBAccess {

	final static String DB_URL = "jdbc:oracle:thin:@(description = (ADDRESS = (PROTOCOL = TCP)(HOST = oracle2.beta.commodities.int.thomsonreuters.com)(PORT = 1521)) (connect_data = (server = dedicated)(service_name = pocbt.int.thomsonreuters.com)))";
	final static String USER = "cef_cnr";
	final static String PASSWORD = "test";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

		JsonObject config = new JsonObject().put("url", DB_URL)
				.put("driver_class","oracle.jdbc.driver.OracleDriver")
				.put("user", USER)
				.put("password", PASSWORD);

		JDBCClient client = JDBCClient.createShared(vertx, config);

		Single<SQLConnection> connection = client.rxGetConnection();

		connection.flatMapObservable(conn -> {
			
			System.out.println("1st flatMapObservable with thread:" + Thread.currentThread().getName());
			// Execute the query
			return conn.rxQueryStream("select object_name from user_objects where rownum <5")
					// Publish the rows one by one in a new Observable
					.flatMapObservable(srs -> { 
						
						System.out.println("2nd flatMapObservable with thread:" + Thread.currentThread().getName());
						return srs.toObservable();
						
					})
					// Don't forget to close the connection
					.doAfterTerminate(conn::close);
		})
				// Map every row to a Product
				.map(ja -> {
					
					System.out.println("map with thread:" + Thread.currentThread().getName());
					return ja.add("From database");
					
				})
				// Display the result one by one
				.subscribe(js -> {
					
					System.out.println("executing oberver with thread:" + Thread.currentThread().getName());
					System.out.println(js.toString());
					
				
				
				});

	}

}
