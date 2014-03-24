package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class OfficePatient {
	@JsonProperty("id")
	private int Id;

	@JsonProperty("patient")
	private int Patient;

	@JsonProperty("officeid")
	private int OfficeId;

	public int getId() {
		return this.Id;
	}

	public int getPatient() {
		return this.Patient;
	}

	public int getOfficeId() {
		return this.OfficeId;
	}

	public OfficePatient() {}
	public OfficePatient(int id, int pat, int office) {
		this.Id = id;
		this.Patient = pat;
		this.OfficeId = office;
	}
}