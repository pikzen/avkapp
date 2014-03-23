package com.avkapp;

public class Profile {
	private int Id;
	private String Name;

	public static int ROLE_ADMIN = 1;
	public static int ROLE_RESPONSABLE = 2;
	public static int ROLE_MEDECIN = 3;
	public static int ROLE_INFIRMIER = 4;
	// Fill up if there are more roles


	public int getId() {
		return this.Id;
	}

	public String getName() {
		return this.Name;
	}

	public Profile(int i, String n) {
		this.Id = i;
		this.Name = n;
	}

	@Override
	public String toString() {
		return "Profile[" + 
						"id=" + Id + "," + 
				        "name=" + Name +
				     "]";
	}

}
