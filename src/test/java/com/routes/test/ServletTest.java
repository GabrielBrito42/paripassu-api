package com.routes.test;

import static org.junit.Assert.assertThrows;


import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.controller.PasswordController;
import com.dao.PostgresqlDAO;
import com.model.Password;
import com.routes.CallPassword;
import com.routes.CreatePassword;
import com.routes.GetPasswords;
import com.routes.ResetPasswords;

class ServletTest extends Mockito{
	
	@Mock
	private Password password;
	
	@Mock
	private PasswordController passwordController;
	
	@Mock
	private PostgresqlDAO postgresql;
	
	@Test
	public void testCreatePasswordRoute() throws Exception {
	    HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);    
        
        when(request.getParameter("type")).thenReturn("N");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new CreatePassword().doPost(request, response);
        
        passwordController = mock(PasswordController.class);
        
        password = new Password("N0002", "N", true);
	    
	    when(passwordController.createPassword("N")).thenReturn(password.toString());
	    
	    JSONObject responseObject = new JSONObject(password);
	    String responseExpected = responseObject.toString();
        verify(request, atLeast(1)).getParameter("type");
        assertTrue(stringWriter.toString().contains(responseExpected));
        writer.flush();
	}
	
	@Test
	public void testGetPasswordRoute() throws IOException, ServletException, ClassNotFoundException, SQLException {
	    HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        String array[] = new String[0];
		JSONObject responseJson = new JSONObject();
		responseJson.put("normalPasswords", array);
		responseJson.put("preferencialPasswords", array);
		responseJson.put("usedPasswords", array);
		responseJson.put("allPasswords", array);
        
        passwordController = mock(PasswordController.class);
        when(passwordController.getPasswords()).thenReturn(responseJson.toString());
        
        new GetPasswords().doGet(request, response);
        
        writer.flush();
        assertTrue(stringWriter.toString().contains(responseJson.toString()));
	}
	
	@Test
	public void testResetPasswordRoute() throws Exception {
	    HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);    
        
        when(request.getParameter("manager")).thenReturn("true");

        new ResetPasswords().doPost(request, response);
        passwordController = mock(PasswordController.class);
        
		doNothing().when(passwordController).resetPasswords(null);
        
        verify(request, atLeast(1)).getParameter("manager");
	}
	
	@Test
	public void testCallPasswordRoute() throws Exception {
	    HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);    
        
        when(request.getParameter("manager")).thenReturn("true");
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        passwordController = mock(PasswordController.class);
        postgresql = mock(PostgresqlDAO.class);
        
        when(passwordController.callNextPassword("true")).thenReturn("N0002");
        when(postgresql.getPasswordsFromTypeSize("N")).thenReturn(1);
        when(postgresql.getPasswordsFromTypeSize("P")).thenReturn(1);
        
        new CallPassword().doPost(request, response);
        
        
        verify(request, atLeast(1)).getParameter("manager");
        writer.flush();
	}
	
	@Test
	public void testResetPasswordsManagerExceptionController() throws ServletException, IOException, ClassNotFoundException, SQLException {
		String params = null;
		passwordController = new PasswordController();
		
	    Exception exception = assertThrows(Exception.class, () -> {
	        passwordController.resetPasswords(params);
	    });

	    String expectedMessage = "Você não possui permissão para executar tal operação";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void testCreatePasswordControllerException() {
		String params = "";
		passwordController = new PasswordController();
		
	    Exception exception = assertThrows(Exception.class, () -> {
	        passwordController.createPassword(params);
	    });

	    String expectedMessage = "Tipo de senha invalido";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void testCallNextPasswordControllerEmpetyException() {
		String params = "true";
		passwordController = new PasswordController();
		
	    Exception exception = assertThrows(Exception.class, () -> {
	        passwordController.callNextPassword(params);
	    });

	    String expectedMessage = "Conjunto de senhas vázio";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void testCreatePasswordController() throws Exception {
		String params = "N";
		passwordController = new PasswordController();
		
		postgresql = mock(PostgresqlDAO.class);
		doNothing().when(postgresql).savePassword(0, params);

        password = new Password("N0001", "N", true);
	    
	    String actualMessage = passwordController.createPassword(params);
	    JSONObject responseObject = new JSONObject(password);
	    String responseExpected = responseObject.toString();
	    assertTrue(actualMessage.contains(responseExpected));
	}
	
	@Test
	public void testResetPasswordController() throws Exception {
		passwordController = new PasswordController();
		String manager = "true";
		postgresql = mock(PostgresqlDAO.class);
		
		doNothing().when(postgresql).resetPasswords();
        assertDoesNotThrow(() -> passwordController.resetPasswords(manager));
	}
}
