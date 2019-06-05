package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import model.Message;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private UserDao userDao;
	
	@Override
	public void init() {
		userDao = new UserDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)  
             throws ServletException, IOException {    
		HttpSession session = request.getSession(false);
		if(session != null) {
			String username = (String) session.getAttribute("name");
			Message message = new Message();
			message.setFrom(username);
			message.setContent("Wylogował się");
			userDao.setActiveUserStatus(username, false);
			session.invalidate();
		}
		Cookie cookie = new Cookie("info", "ok");
		response.addCookie(cookie);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
    	rd.include(request, response); 
	}  
}
