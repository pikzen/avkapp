package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class INRTreatmentPhase {
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

	public INRTreatmentPhase() {}
	public INRTreatmentPhase(int id, String name) {
		this.Id = id;
		this.Name = name;
	}
}