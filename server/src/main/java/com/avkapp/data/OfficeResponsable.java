package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;

public class OfficeResponsable {
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

	public OfficeResponsable() {}
	public OfficeResponsable(int uId, int oId) {
		this.UserId = uId;
		this.OfficeId = oId;
	}
}