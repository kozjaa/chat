package listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import db.ConnectionFactory;


@WebListener
public class ChatContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		InputStream is = arg0.getServletContext().getResourceAsStream("application.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		arg0.getServletContext().setAttribute("properties", properties);
		ConnectionFactory.setProperties(properties);	
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}


}
