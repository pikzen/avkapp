package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.avkapp.data.Office;
import com.avkapp.data.LoginInfo;
import com.avkapp.data.Profile;
import com.avkapp.data.User;
import com.avkapp.dao.OfficeDAO;
import com.avkapp.dao.UserDAO;

import java.sql.SQLException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;

import org.codehaus.jackson.annotate.JsonProperty;


@Path("/offices")
public class OfficeResource {

  	static class OfficeLoginContainer implements Serializable {
		@JsonProperty("office")
		private Office Office;

		@JsonProperty("login")
		private LoginInfo Login;

	    public Office getOffice() {
	      return this.Office;
	    }

		public LoginInfo getLogin() {
			return this.Login;
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOffice(OfficeLoginContainer data) {
		int privileges = Profile.PERM_CREATEOFFICE;

	    OfficeDAO iDao = new OfficeDAO();
	    UserDAO uDao = new UserDAO();
		Response response = null;

	    Logger.getLogger("AVKAPP").log(Level.WARNING, data.getLogin().toString());

	    if (uDao.authorize(privileges, data.getLogin().getUsername(), data.getLogin().getPassword())) {
			  try {
				  iDao.insert(data.getOffice());
				  response = Response.status(200).build();
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

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(LoginInfo log) {
		UserDAO uDao = new UserDAO();
		OfficeDAO oDao = new OfficeDAO();
		Response response = null;
		int privileges = Profile.PERM_LISTALLOFFICES;

		if (uDao.authorize(privileges, log.getUsername(), log.getPassword())) {
			try {
				ArrayList<Office> all = oDao.getAll(log);

				if (all != null) {
					GenericEntity<List<Office>> data = new GenericEntity<List<Office>>(all) {};
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

	@POST
	@Path("waiting")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWaiting(LoginInfo log) {
		UserDAO uDao = new UserDAO();
		OfficeDAO oDao = new OfficeDAO();
		Response response = null;
		int privileges = Profile.PERM_LISTOFFICEWAITING;

		if (uDao.authorize(privileges, log.getUsername(), log.getPassword())) {
			try {
				ArrayList<Office> waiting = oDao.getWaiting(log);

				if (waiting != null) {
					GenericEntity<List<Office>> data = new GenericEntity<List<Office>>(waiting) {};
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

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOfficeById(@PathParam("id") String id) {
		OfficeDAO inter = new OfficeDAO();
		Response response = null;

		try {
			Office office = inter.getById(id);
			Logger log = Logger.getLogger("AVKapp");
			if (office != null) {
				response = Response.status(200).entity(office.getName()).build();
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
