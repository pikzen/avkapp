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
import com.avkapp.LoginInfo;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;


@Path("/login")
public class LoginResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginInfo log) {
		UserDAO iDao = new UserDAO();
		Response response = null;

		try {
			if (iDao.validate(log)) { 
				response = Response.status(200).build();
			}
			else {
				response = Response.status(400).entity("Informations de connexion erronées : " + log.toString()).build();
				Logger.getLogger("AVKAPP").log(Level.WARNING, log.toString());
			}
		}
		catch (SQLException e) {
			response = Response.status(500).build();
		}

		return response;
	}
}
