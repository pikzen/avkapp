package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.data.Profile;
import com.avkapp.dao.ProfileDAO;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;


@Path("/profiles")
public class ProfileResource {


	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfileById(@PathParam("id") String id) {
		ProfileDAO inter = new ProfileDAO();
		Response response = null;

		try {
			Profile profile = inter.getById(id);
			if (profile != null) {
				response = Response.status(200).entity(profile.getName()).build();
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
	public Response createProfile(Profile inter) {
		ProfileDAO iDao = new ProfileDAO();
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
	public Response getJSONProfiles() {
		ProfileDAO inter = new ProfileDAO();
		Response response = null;

		try {
			ArrayList<Profile> all = inter.getAll();
			Logger log = Logger.getLogger("AVKapp");
			if (all != null) {
			}
			else {
			}
			response = Response.status(200).
			entity(all).
			build();
		}
		catch (SQLException e) {
			Logger log = Logger.getLogger("AVKappLogger");
			log.log(java.util.logging.Level.WARNING, e.getMessage());
			response = Response.status(500).build();
		}

		return response;
	}
}
