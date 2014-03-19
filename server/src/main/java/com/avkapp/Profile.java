package com.avkapp;

public class Profile {
	private int Id;
	private String Name;


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
