package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
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


@Path("/users")
public class UserResource {
	
	@POST
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfileById(@PathParam("id") String id, LoginInfo info) {	
		UserDAO uDao = new UserDAO();
		Response response = null;

		try {
			User profile = uDao.getByLoginInfo(info);
			if (profile != null) {
				response = Response.status(200).entity(profile).build();
			}
			else {
				response = Response.status(400).build();
			}
		}
		catch (SQLException e) {
			Logger log = Logger.getLogger("AVKappLogger");
			log.log(java.util.logging.Level.WARNING, e.getMessage());
			response = Response.status(500).build();
		}

		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User inter) {
		UserDAO iDao = new UserDAO();
		Response response = null;

		try {
			iDao.insert(inter);
			response = Response.status(200).build();
		}
		catch (SQLException e) {
			response = Response.status(500).build();
		}

		return response;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONUsers() {	
		UserDAO inter = new UserDAO();
		Response response = null;

		try {
			ArrayList<User> all = inter.getAll(new LoginInfo());
			Logger log = Logger.getLogger("AVKapp");
			if (all != null) {
			}
			else {
			}
			response = Response.status(200).entity(all).build();
		}
		catch (SQLException e) {
			Logger log = Logger.getLogger("AVKappLogger");
			log.log(java.util.logging.Level.WARNING, e.getMessage());
			response = Response.status(500).build();
		}

		return response;
	}
}
