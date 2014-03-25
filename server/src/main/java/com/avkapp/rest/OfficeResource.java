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
import com.avkapp.data.Profile;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.io.Serializable;
import javax.ws.rs.PathParam;
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

	@POST
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
