package com.avkapp;

public class LoginInfo {
	private String login;
	private String password;
	private String pin;

	public String getLogin() {
		return this.login;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPin() {
		return this.pin;
	}

	public LoginInfo() {
		this.login = "";
		this.password = "";
		this.pin = "";
		
	}
}