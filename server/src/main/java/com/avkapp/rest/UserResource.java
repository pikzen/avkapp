package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.data.Office;
import com.avkapp.dao.OfficeDAO;
import com.avkapp.data.LoginInfo;
import com.avkapp.dao.UserDAO;
import com.avkapp.data.User;
import com.avkapp.data.Profile;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.io.Serializable;
import javax.ws.rs.PathParam;
import org.codehaus.jackson.annotate.JsonProperty;
import javax.ws.rs.core.GenericEntity;
import java.util.List;


@Path("/users")
public class UserResource {

	// GET /users/waiting
	@POST
	@Path("waiting")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWaitingUsers(LoginInfo log) {
		int privileges = Profile.PERM_LISTALLWAITING;

		UserDAO uDao = new UserDAO();
		Response response = null;
		if (uDao.authorize(privileges, log.getUsername(), log.getPassword())) {
			try {
				List<User> waiting = uDao.getWaiting(log);

				if (waiting != null) {
					GenericEntity<List<User>> data = new GenericEntity<List<User>>(waiting) {};
					response = Response.status(200).entity(data).build();
				}
				else {
					response = Response.status(404).build();	
				}
			}
			catch (SQLException e) {
				response = Response.status(500).build();
			}
		}
		else {
			response = Response.status(403).build();
		}

		return response;


	}

	// GET /users
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(LoginInfo log) {
		int privileges = Profile.PERM_LISTALLUSERS;
		UserDAO uDao = new UserDAO();
		Response response = null;

		if (uDao.authorize(privileges, log.getUsername(), log.getPassword())) {
			try {
				ArrayList<User> waiting = uDao.listableUsers(log);
				GenericEntity<ArrayList<User>> data = new GenericEntity<ArrayList<User>>(waiting) {};
				response = Response.status(200).entity(data).build();
			}
			catch (SQLException e) {
				response = Response.status(500).build();
			}
		}
		else {
			response = Response.status(403).build();
		}

		return response;
	}
}
