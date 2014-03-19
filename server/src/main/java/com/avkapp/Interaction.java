package com.avkapp;

public class Interaction {
	private int Id;
	private String Medicament;
	private int Effect;
	private String Note;

	public int getId() {
		return this.Id;
	}

	public String getName() {
		return this.Medicament;
	}

	public int getEffect() {
		return this.Effect;
	}

	public String getNote() {
		return this.Note;
	}

	public Interaction(int id, String med, int eff, String not) {
		this.Id = id;
		this.Medicament = med;
		this.Effect = eff;
		this.Note = not;
	}

	@Override
	public String toString() {
		return "Interaction[" + 
							"id=" + Id + "," + 
							"name=" + Medicament + "," + 
				        	"effect=" + Effect + "," +
				        	"note=" + Note + 
				     	  "]";
	}
}
