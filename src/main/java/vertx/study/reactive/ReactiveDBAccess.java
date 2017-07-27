package vertx.study.reactive;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;

public class ReactiveDBAccess {
	
	final static String DB_URL="jdbc:oracle:thin:@(description = (ADDRESS = (PROTOCOL = TCP)(HOST = pc-scan-dd0076.int.thomsonreuters.com)(PORT = 1521))(connect_data = (server = dedicated) (service_name = pocb02t.int.thomsonreuters.com)))";
	final static String USER="CEF_CNR";
	final static String PASSWORD="test";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		
		
		PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource(); 
		pds.setURL(DB_URL);
		pds.setUser(USER);
		pds.setPassword(PASSWORD);
		pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource"); 

		pds.setMinPoolSize(1); 
		pds.setMaxPoolSize(10); 
		pds.setInitialPoolSize(2); 		
		
		SQLClient client = JDBCClient.create(vertx, pds);
		
		client.getConnection(res -> {
			
			System.out.println("Got connection:"+Thread.currentThread().getName());
			
			  if (res.succeeded()) {
			    SQLConnection connection = res.result();
			    connection.query("SELECT sysdate FROM dual", rs -> {
			      System.out.println("RS returned:"+Thread.currentThread().getName());
			      if (rs.succeeded()) {
					ResultSet resultSet = rs.result();
					List<JsonObject> rows = resultSet.getRows();
					
					for (JsonObject row : rows) {
						String attr = row.getString("SYSDATE");
						System.out.println(attr);
					}
			      }
			      else
			      {
			    	  System.out.println("Query failed");
			      }
			    });
			    
			  } else {
			    System.out.println("Failed to get DB connection:"+res.cause().fillInStackTrace().toString());
			  }
			});
		
		


	}

}
