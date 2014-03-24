package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class INRTreatment {
	@JsonProperty("id")
	private int Id;

	@JsonProperty("med")
	private int Medication;

	@JsonProperty("phase")
	private int Phase;

	public int getMedication() {
		return this.Medication;
	}

	public int getPhase() {
		return this.Phase;
	}

	public int getId() {
		return this.Id;
	}

	public INRTreatment() {}
	public INRTreatment(int id, int med, int ph) {
		this.Id = id;
		this.Medication = med;
		this.Phase = ph;
	}
}