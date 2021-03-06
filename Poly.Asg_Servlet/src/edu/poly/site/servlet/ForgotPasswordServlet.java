package edu.poly.site.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.poly.common.EmailUtils;
import edu.poly.common.PageInfo;
import edu.poly.common.PageType;
import edu.poly.dao.UserDAO;
import edu.poly.domain.Email;
import edu.poly.model.User;

/**
 * Servlet implementation class ForgotPasswordServlet
 */
@WebServlet("/ForgotPassword")
public class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PageInfo.prepareAndForwardSite(request, response, PageType.SITE_FORGOT_PASSWORD_PAGE);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String emailAddress = request.getParameter("email");
			String username = request.getParameter("username");
			
			UserDAO dao = new UserDAO();
			User user = dao.findByUsernameAndEmail(username, emailAddress);
			
			if (user == null) {
				request.setAttribute("error", "Username or Email are incorrect");
			}else {
				Email email = new Email();
				email.setFrom("thogprovjp@gmail.com");
				email.setFromPassword("thongnguyen");
				email.setTo(emailAddress);
				email.setSubject("Forgot Password Function");
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ").append("<br>");
				sb.append("You are used the forgot password function. <br>");
				sb.append("Your password is <b>").append(user.getPassword()).append("</b>");
				sb.append("Regards<br>");
				sb.append("Administrator");
				
				email.setContent(sb.toString());
				EmailUtils.send(email);
				
				request.setAttribute("message", "Email sent to the mail Address. "
						+ " Please check and get your password");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			request.setAttribute("error", "Error:" + e.getMessage());
		}
		PageInfo.prepareAndForwardSite(request, response, PageType.SITE_FORGOT_PASSWORD_PAGE);
	}

}
