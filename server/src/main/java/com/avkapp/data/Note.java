package com.avkapp.data;
import java.sql.Date;
import org.codehaus.jackson.annotate.JsonProperty;

public class Note{
	@JsonProperty("id")
	private int Id;

	@JsonProperty("date")
	private Date Datenote;

	@JsonProperty("note")
	private String Note;

	public int getId() {
		return this.Id;
	}

	public Date getDatenote() {
		return this.Datenote;
	}

	public String getNote() {
		return this.Note;
	}


	public Note(int i, Date dn, String n) {
		this.Id = i;
		this.Datenote = dn;
		this.Note = n;
	}

	@Override
	public String toString() {
		return "Note[" + 
						"id=" + Id + "," + 
				        "date=" + Datenote.toString() + "," +
				        "note=" + Note + 
				     "]";
	}

}
