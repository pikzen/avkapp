package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.User;
import com.avkapp.UserDAO;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.HashMap;


@Path("/inscription")
public class RegisterResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User i) {
		boolean errors = false;

		HashMap<String, String> hm = new HashMap<String, String>();

		UserDAO iDao = new UserDAO();
		Response response = null;

		if (i.getFirstname().equals("")) {
			hm.put("errorFirstname", "Le champ ne doit pas être vide.");
		}
		if (i.getLastname().equals("")) {
			hm.put("errorLastname", "Le champ ne doit pas être vide.");
		}
		if (i.getLogin().equals("")) {
			hm.put("errorLogin", "Le champ ne doit pas être vide.");
		}
		try {
			if (iDao.loginExists(i.getLogin())) {
				hm.put("errorLogin", "Ce nom d'utilisateur existe déjà.");
			}
		}
		catch(SQLException e) {
			hm.put("globalError", "Le serveur à renvoyé une erreur.");
		}
		if (i.getPassword().equals("")) {
			hm.put("errorPassword", "Ce champ ne doit pas être vide.");
		}
		if (i.getEmail().equals("")) {
			hm.put("errorEmail", "Ce champ ne doit pas être vide");
		}
		if (i.getProfile() == -1) {
			hm.put("errorProfile", "Veuillez sélectionner votre fonction.");
		}
		if (i.getOffice() == -1) {
			hm.put("errorOffice", "Aucun cabinet sélectionné. Vous n'aurez accès à aucune donnée tant que vous n'appartenez pas à un cabinet.");
		}
		if (i.getPin().equals("")) {
			hm.put("errorPin", "Ce champ ne doit pas être vide.");
		}

		org.json.JSONObject errorlist = new org.json.JSONObject(hm);


		if (!errors) {

			try {
				iDao.insert(i);
				response.status(200).build();
			}
			catch (SQLException e) {
				response.status(500).build();
			}
		}
		else {
			response.status(400).entity(errorlist.toString()).build();
		}

		return response;
	}
}
