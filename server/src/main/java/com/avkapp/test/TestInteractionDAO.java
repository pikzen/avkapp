package com.avkkap.test;

import com.avkapp.InteractionDAO;
import java.util.ArrayList;
import com.avkapp.Interaction;

public class TestInteractionDAO {
	public static void main(String[] args) {
		System.out.println("############# TestInteractionDAO ######");
		System.out.print("--- getAll() : ");
		InteractionDAO dao = new InteractionDAO();
		try {
			ArrayList<Interaction> resp = dao.getAll();
			System.out.println("PASS");
		}
		catch (java.sql.SQLException e) {
			System.out.println("FAIL");
		}
	}
}
