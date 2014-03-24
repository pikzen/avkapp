package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class OfficeSwitch {
	@JsonProperty("userid")
	private int UserId;

	@JsonProperty("officeid")
	private int OfficeId;

	public int getUserId() {
		return this.UserId;
	}

	public int getOfficeId() {
		return this.OfficeId;
	}

	public OfficeSwitch() {

	}	

	public OfficeSwitch(int u, int o) {
		this.UserId = u;
		this.OfficeId = o;
	}

	@Override
	public String toString() {
		return "OfficeSwitch[" + 
						"userid=" + UserId + "," + 
				        "officeid=" + OfficeId + 
				     "]";
	}
}