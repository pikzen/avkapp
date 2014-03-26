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

	@JsonProperty("validated")
	private int Validated;

	public int getValidated() {
		return this.Validated;
	}

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

	public Office(int i, String n, String a, String p, int v) {
		this.Id = i;
		this.Name = n;
		this.Address = a;
		this.Phone = p;
		this.Validated = v;
	}

  public Office(String n, String a, String p) {
    this.Name = n;
    this.Address = a;
    this.Phone = p;
    this.Validated = 5;
    this.Id = -1;
  }

  public Office() {
  }

	@Override
	public String toString() {
		return "Office[" +
						"id=" + Id + "," +
				        "name=" + Name + "," +
				        "address=" + Address + "," +
				        "phone=" + Phone + "," +
				        "validated=" + Validated + 
				     "]";
	}

}
