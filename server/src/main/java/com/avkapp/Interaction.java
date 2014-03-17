package com.avkapp;

public class Interaction {
	private String Medicament;
	private int Effect;
	private String Note;

	public String getName() {
		return this.Medicament;
	}

	public int getEffect() {
		return this.Effect;
	}

	public String getNote() {
		return this.Note;
	}

	public Interaction(String med, int eff, String not) {
		this.Medicament = med;
		this.Effect = eff;
		this.Note = not;
	}

	@Override
	public String toString() {
		return "Interaction[name=" + Medicament + "," + 
				   "effect=" + Effect + "," +
				   "note=" + Note + "]";
	}

}
