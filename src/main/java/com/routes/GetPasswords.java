package com.routes;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controller.PasswordController;


@WebServlet("/get-passwords")
public class GetPasswords extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PasswordController controller = new PasswordController();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String passwords = null;
		try {
			passwords = controller.getPasswords();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(passwords);
		response.getWriter().flush();
	}
}
