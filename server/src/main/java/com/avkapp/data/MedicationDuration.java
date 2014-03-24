package com.avkapp.data;
import java.sql.Date;
import org.codehaus.jackson.annotate.JsonProperty;

public class MedicationDuration {
	
	@JsonProperty("id")
	private int Id;

	@JsonProperty("med")
	private int Medication;

	@JsonProperty("patient")
	private int Patient;

	@JsonProperty("begin")
	private Date Begin;

	@JsonProperty("end")
	private Date End;

	public int getId() {
		return this.Id;
	}

	public int getMedication() {
		return this.Medication;
	}

	public int getPatient() {
		return this.Patient;
	}

	public Date getBegin() {
		return this.Begin;
	}

	public Date getEnd() {
		return this.End;
	}

	public MedicationDuration() {}
	public MedicationDuration(int i, int med, int pat, Date beg, Date end) {
		this.Id = i;
		this.Medication = med;
		this.Patient = pat;
		this.Begin = beg;
		this.End = end;
	}
}