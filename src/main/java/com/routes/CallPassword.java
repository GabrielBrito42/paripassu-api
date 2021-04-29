package com.routes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controller.PasswordController;

@WebServlet("/call-next-password")
public class CallPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PasswordController controller = new PasswordController();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String params = request.getParameter("manager");
		String password = "";
		try {
			password = controller.callNextPassword(params);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			response.resetBuffer();
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setHeader("Content-Type", "application/json");
			response.flushBuffer();
		}
		response.getWriter().print(password);
		response.getWriter().flush();
	}
}
