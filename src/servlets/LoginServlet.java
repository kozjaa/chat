package servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private UserDao userDao;
	
	@Override
	public void init() {
		userDao = new UserDao();
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String name = String.valueOf(request.getParameter("name")).trim().toLowerCase();
		String password = String.valueOf(request.getParameter("password")).trim().toLowerCase();
		User user = userDao.getUserByUsername(name);
		Set<User> allActiveUsers = userDao.getAllActiveUsers();
        Set<String> allActiveUsersnames = new HashSet<>();
        allActiveUsersnames  = allActiveUsers.stream().map(User::getUsername).collect(Collectors.toSet());
		String daoUserUsername = user.getUsername().trim().toLowerCase();
		String daoUserPassword = user.getPassword().trim().toLowerCase();
		
		if(allActiveUsersnames.contains(name)) {
			Cookie cookie = new Cookie("info", "locked");
			response.addCookie(cookie);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
        	rd.include(request, response);
		}
		
		if(daoUserUsername.trim().toLowerCase() == null && daoUserPassword .trim().toLowerCase() == null) {
			registerUser(name, password, request, response);
		}		
		else if(daoUserUsername.equals(name) && daoUserPassword .equals(password)) {
			logInUser(name, request, response);
		}
		else {
			Cookie cookie = new Cookie("info", "error");
			response.addCookie(cookie);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
        	rd.include(request, response);
		}

    }
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession currentSession = request.getSession(false);

        if(currentSession == null) {
        	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
        	rd.include(request, response);
        }
        else {
        	response.sendRedirect("room");
        }
    }
	
	private void registerUser(String name, String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession newSession = request.getSession();
		User newUser = new User();
		newUser.setUsername(name);
		newUser.setPassword(password);
		newUser.setIsActive(true);
		userDao.insertUser(newUser);
		newSession.setAttribute("name", name);
		Cookie cookie = new Cookie("name", name);
		response.addCookie(cookie);
		response.sendRedirect("room");
	}
	
	private void logInUser(String name, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession newSession = request.getSession();
		userDao.setActiveUserStatus(name, true);
		newSession.setAttribute("name", name);
		Cookie cookie = new Cookie("name", name);
		response.addCookie(cookie);
		response.sendRedirect("room");	
	}

}
