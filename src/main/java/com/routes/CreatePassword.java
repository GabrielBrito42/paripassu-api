package com.routes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controller.PasswordController;

@WebServlet("/create-password")
public class CreatePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PasswordController controller = new PasswordController();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String params = request.getParameter("type");
	    String password = null;
	    try {
			password = controller.createPassword(params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(password);
		response.getWriter().flush();
	}
}
