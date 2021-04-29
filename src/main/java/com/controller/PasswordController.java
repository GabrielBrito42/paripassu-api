package com.controller;

import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.dao.PostgresqlDAO;
import com.model.Password;

public class PasswordController {
	private PostgresqlDAO postgres;
	
	public PasswordController() {
		postgres = new PostgresqlDAO();
	}
	
	public String getPasswords() throws ClassNotFoundException, SQLException {
		ArrayList<Password> allPasswords = postgres.getData();
		for(Password password : allPasswords) {
			password.setPassword(transformPassword(password.getType(), password.getPassword()));
		}

		List<Password> normalPasswords = allPasswords.stream().filter(p -> p.getType().equals("N") && p.getEnabled().equals(true))
				.collect(Collectors.toList());
		List<Password> preferencialPasswords = allPasswords.stream().filter(p -> p.getType().equals("P") && p.getEnabled().equals(true))
				.collect(Collectors.toList());
		List<Password> usedPasswords = allPasswords.stream().filter(p -> p.getEnabled().equals(false))
				.collect(Collectors.toList());

		JSONObject responseJson = new JSONObject();
		responseJson.put("normalPasswords", normalPasswords);
		responseJson.put("preferencialPasswords", preferencialPasswords);
		responseJson.put("usedPasswords", usedPasswords);
		responseJson.put("allPasswords", allPasswords);
		String response = responseJson.toString();

		return response;
	}
	
	public String createPassword(String params) throws Exception {
		String passwordType = params;
		if(!passwordType.equals("N") && !passwordType.equals("P")) {
			throw new Exception("Tipo de senha invalido");
		}
		int size = postgres.getPasswordsFromTypeSize(passwordType);
		int passwordValue = size+1;

		postgres.savePassword(passwordValue, passwordType);

		String result = transformPassword(passwordType, Integer.toString(passwordValue));
		Password password = new Password(result, passwordType, true);

		JSONObject responseObject = new JSONObject(password);
		String response = responseObject.toString();

		return response;
	}
	
	public String callNextPassword(String params) throws Exception {
		Boolean manager = Boolean.parseBoolean(params);
		if(manager == false) throw new Exception("Você não possui permissão para executar tal operação");

		int passwordSyze = postgres.getPasswordsFromTypeSizeTrue("P");
		if(passwordSyze>0) {
		    ArrayList<Password> passwordsType = postgres.getPasswordsFromType("P");
		String password = passwordsType.get(0).getPassword();
		    postgres.callNextPassword(password, "P");
		    password = transformPassword(passwordsType.get(0).getType(), password);
		    return password;
		}

		passwordSyze = postgres.getPasswordsFromTypeSizeTrue("N");
		if(passwordSyze==0) throw new Exception("Conjunto de senhas vázio");

		ArrayList<Password> passwordsType = postgres.getPasswordsFromType("N");
		String password = passwordsType.get(0).getPassword();
		postgres.callNextPassword(password, "N");
		password = transformPassword(passwordsType.get(0).getType(), password);
		return password;
	}
	
	public void resetPasswords(String params) throws Exception {
		Boolean manager = Boolean.parseBoolean(params);
		if(manager == false) throw new Exception("Você não possui permissão para executar tal operação");
		postgres.resetPasswords();
	}
	
	private String transformPassword(String type, String password) {
		String result = type + String.format("%04d", Integer.parseInt(password));
		return result;
	}
}
