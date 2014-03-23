package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.Office;
import com.avkapp.OfficeDAO;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.ws.rs.PathParam;


@Path("/offices")
public class OfficeResource {
	


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOffice(Office inter) {
		OfficeDAO iDao = new OfficeDAO();
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
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOfficeById(@PathParam("id") String id) {	
		OfficeDAO inter = new OfficeDAO();
		Response response = null;

		try {
			Office office = inter.getById(id);
			Logger log = Logger.getLogger("AVKapp");
			if (office != null) {
				response = Response.status(200).entity(office).build();
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSONOffices() {	
		OfficeDAO inter = new OfficeDAO();
		Response response = null;

		try {
			ArrayList<Office> all = inter.getAll();
			Logger log = Logger.getLogger("AVKapp");
			if (all != null) {
				response = Response.status(200).entity(all).build();
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
}
