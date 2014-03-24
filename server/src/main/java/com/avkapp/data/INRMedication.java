package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class INRMedication {
	@JsonProperty("id")
	private int Id;

	@JsonProperty("name")
	private String Name;

	public int getId() {
		return this.Id;
	}

	public String getName() {
		return this.Name;
	}

	public INRMedication() {}
	public INRMedication(int id, String name) {
		this.Id = id;
		this.Name = name;
	}
}