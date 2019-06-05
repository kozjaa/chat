package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/room")
public class ChatRoomServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession currentSession = request.getSession(false);
        String currentUsername = (String) currentSession.getAttribute("name");
		Cookie cookie = new Cookie("name", currentUsername);
		response.addCookie(cookie);
		request.setAttribute("name", currentUsername);
		request.getRequestDispatcher("/room.jsp").forward(request, response);
    }
}
