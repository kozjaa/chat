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


@WebServlet("/conversation")
public class ConversationServlet extends HttpServlet {

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String current = (String) request.getSession().getAttribute("name");
		String receiver = (String) request.getParameter("name");
		Cookie cookie = new Cookie("receiver", receiver);
		Cookie currentUser = new Cookie("current", current);
		request.setAttribute("receiver", receiver);
		response.addCookie(currentUser);
		response.addCookie(cookie);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/conversation.jsp");
        rd.include(request, response);
    }

}
