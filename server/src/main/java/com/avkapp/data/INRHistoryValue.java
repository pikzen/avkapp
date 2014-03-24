package com.avkapp.data;
import java.sql.Date;
import org.codehaus.jackson.annotate.JsonProperty;

public class INRHistoryValue {
	@JsonProperty("id")
	private int Id;

	@JsonProperty("name")
	private Date DateHist;

	@JsonProperty("value")
	private float Value;

	@JsonProperty("bleeding")
	private boolean Bleeding;

	@JsonProperty("phase")
	private int Phase;

	@JsonProperty("patient")
	private int Patient;

	public int getId() {
		return this.Id;
	}

	public int getPatient() {
		return this.Patient;
	}

	public Date getDateHist() {
		return this.DateHist;
	}

	public float getValue() {
		return this.Value;
	}

	public boolean getBleeding() {
		return this.Bleeding;
	}

	public int getPhase() {
		return this.Phase;
	}

	public INRHistoryValue() {}
	public INRHistoryValue(int id, Date d, float val, boolean ble, int ph, int pt) {
		this.Id = id;
		this.DateHist = d;
		this.Value = val;
		this.Bleeding = ble;
		this.Phase = ph;
		this.Patient = pt;
	}
}