package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.data.User;
import com.avkapp.dao.UserDAO;
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
		try {
			if (iDao.loginExists(i.getLogin())) {
				hm.put("errorLogin", "Ce nom d'utilisateur existe déjà.");
				errors = true;
			}
			if (iDao.emailExists(i.getEmail())) {
				hm.put("errorEmail", "Cet email existe déjà");
				errors = true;
			}
		}
		catch(SQLException e) {
			hm.put("globalError", "Le serveur à renvoyé une erreur.");
			errors = true;
		}
		if (!errors) {
			hm.put("success", "1");
		}

		org.json.JSONObject errorlist = new org.json.JSONObject(hm);


		if (!errors) {
			try {
				iDao.insert(i);
				response = Response.status(200).entity(errorlist.toString()).build();
			}
			catch (SQLException e) {
				response.status(500).build();
			}
		}
		else {
			response = Response.status(400).entity(errorlist.toString()).build();
		}

		return response;
	}
}
