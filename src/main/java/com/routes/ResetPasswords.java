package com.routes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controller.PasswordController;

@WebServlet("/reset-passwords")
public class ResetPasswords extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PasswordController controller = new PasswordController();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String params = request.getParameter("manager");
	    try {
			controller.resetPasswords(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
