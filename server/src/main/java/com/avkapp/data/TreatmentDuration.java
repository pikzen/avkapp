package com.avkapp.data;
import java.sql.Date;
import org.codehaus.jackson.annotate.JsonProperty;

public class TreatmentDuration {
	
	@JsonProperty("id")
	private int Id;

	@JsonProperty("treatment")
	private int Treatment;

	@JsonProperty("phase")
	private int Phase;

	@JsonProperty("begin")
	private Date Begin;

	@JsonProperty("end")
	private Date End;

	public int getPhase() {
		return this.Phase;
	}

	public int getTreatment() {
		return this.Treatment;
	}

	public int getId() {
		return this.Id;
	}


	public Date getBegin() {
		return this.Begin;
	}

	public Date getEnd() {
		return this.End;
	}

	public TreatmentDuration() {}
	public TreatmentDuration(int i, int tre, int pha, Date beg, Date end) {
		this.Id = i;
		this.Treatment = tre;
		this.Phase = pha;
		this.Begin = beg;
		this.End = end;
	}
}