package com.avkapp;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class LoginInfo {
	@JsonProperty("username")
	private String Username;

	@JsonProperty("password")
	private String Password;
	
	public String getUsername() {
		return this.Username;
	}

	public String getPassword() {
		return this.Password;
	}
	/**
	* Default constructor, used by Jackson
	*/
	public LoginInfo() {
	}

	public LoginInfo(String u, String p) {
		this.Username = u;
		this.Password = p;
	}

	@Override
	public String toString() {
		return "LoginInfo[" + 
						"username=" + Username + "," + 
				        "password=" + Password + 
				     "]";
	}
}