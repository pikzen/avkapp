package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class Office {
	public static final int NO_OFFICE = 0;
	public static final int CREATE_OFFICE = 1;

	@JsonProperty("id")
	private int Id;

	@JsonProperty("name")
	private String Name;

	@JsonProperty("address")
	private String Address;

	@JsonProperty("phone")
	private String Phone;

	public int getId() {
		return this.Id;
	}

	public String getName() {
		return this.Name;
	}

	public String getAddress() {
		return this.Address;
	}

	public String getPhone() {
		return this.Phone;
	}

	public Office(int i, String n, String a, String p) {
		this.Id = i;
		this.Name = n;
		this.Address = a;
		this.Phone = p;
	}

	@Override
	public String toString() {
		return "Office[" + 
						"id=" + Id + "," + 
				        "name=" + Name + "," +
				        "address=" + Address + "," +
				        "phone=" + Phone + 
				     "]";
	}

}
