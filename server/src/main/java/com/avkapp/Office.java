package com.avkapp;

public class Office {
	private int Id;
	private String Name;
	private String Address;
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
