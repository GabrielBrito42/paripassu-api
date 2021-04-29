package com.model;

public class Password {
	private String password;
	private String type;
	private Boolean enabled;
	
	public Password() {
		super();
	}
	
	public Password(String password, String type, Boolean enabled) {
		this.password = password;
		this.type = type;
		this.enabled = enabled;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
