package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class User {
	@JsonProperty("id")
	private int Id;

	@JsonProperty("firstname")
	private String Firstname;

	@JsonProperty("lastname")
	private String Lastname;

	@JsonProperty("email")
	private String Email;

	@JsonProperty("login")
	private String Login;

	@JsonProperty("phone")
	private String Phone;

	@JsonProperty("password")
	private String Password;

	@JsonProperty("profile")
	private int Profile;

	@JsonProperty("office")
	private int Office;

	@JsonProperty("pin")
	private String Pin;

	@JsonProperty("validated")
	private int Validated;

	public int getValidated() {
		return this.Validated;
	}

	public String getPin() {
		return this.Pin;
	}

	public String getLogin() {
		return this.Login;
	}

	public int getId() {
		return this.Id;
	}

	public String getFirstname() {
		return this.Firstname;
	}

	public String getLastname() {
		return this.Lastname;
	}

	public String getEmail() {
		return this.Email;
	}

	public String getPhone() {
		return this.Phone;
	}

	public String getPassword() {
		return this.Password;
	}

	public int getProfile() {
		return this.Profile;
	}

	public int getOffice() {
		return this.Office;
	}

	public User(int i, String f, String l, String e, String log, String phn, String pw, String pin, int pro, int o) {
		this.Id = i;
		this.Firstname = f;
		this.Lastname = l;
		this.Email = e;
		this.Login = log;
		this.Phone = phn;
		this.Password = pw;
		this.Profile = pro;
		this.Office = o;
		this.Pin = pin;
		this.Validated = 0;
	}
	public User(int i, String f, String l, String e, String log, String phn, String pw, String pin, int pro, int o, int val) {
		this.Id = i;
		this.Firstname = f;
		this.Lastname = l;
		this.Email = e;
		this.Login = log;
		this.Phone = phn;
		this.Password = pw;
		this.Profile = pro;
		this.Office = o;
		this.Pin = pin;
		this.Validated = val;
	}

	/**
	* Default constructor, used by Jackson
	*/
	public User() {
	}

	@Override
	public String toString() {
		return "User[" + 
						"id=" + Id + "," + 
				        "firstname=" + Firstname + "," +
				        "lastname=" + Lastname + "," +
				        "email=" + Email + "," +
				        "phone=" + Phone + "," +
				        "password=" + Password + "," +
				        "profile=" + Profile + "," +
				        "office=" + Office + 
				     "]";
	}

}
