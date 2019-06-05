package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class ConnectionFactory {
	public static Properties props;
	public static String url;
	public static String user;
	public static String password;
	
	public static Connection getConnection() {
		url = props.getProperty("db.url");
		user = props.getProperty("db.user");
		password = props.getProperty("db.pass");
      try {
    	  Class.forName("org.postgresql.Driver");
          return DriverManager.getConnection(url, user, password);
      } catch (Exception ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      
      }
    }
	
	public static void setProperties(Properties properties) {
		props = properties;
	}
}
