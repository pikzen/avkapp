package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.Interaction;
import com.avkapp.InteractionDAO;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;

@Path("/interactions")
public class InteractionResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createInteraction(String req) {
		Logger log = Logger.getLogger("AVKApp");
		log.log(Level.WARNING, req);



		InteractionDAO iDao = new InteractionDAO();
		Response response = null;
		Interaction inter = new Interaction(1, "", 1, "");
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
	public Response getJSONInteractions() {	
		InteractionDAO inter = new InteractionDAO();
		Response response = null;

		try {
			ArrayList<Interaction> all = inter.getAll();
			
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
