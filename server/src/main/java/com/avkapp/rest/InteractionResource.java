package com.avkapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import com.sun.jersey.api.json.JSONWithPadding;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.avkapp.data.Interaction;
import com.avkapp.dao.InteractionDAO;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import com.avkapp.data.Profile;
import com.avkapp.dao.UserDAO;
import org.codehaus.jackson.annotate.JsonProperty;
import com.avkapp.data.LoginInfo;
import java.io.Serializable;

@Path("/interactions")
public class InteractionResource {
	
		/**
	* Wrapper pour la methode POST, afin de pouvoir recevoi le contenu du form ainsi que les informations de connexion
	*
	*/
	class InteractionLoginContainer implements Serializable {
		@JsonProperty("inter")
		private Interaction Inter;

		@JsonProperty("login")
		private LoginInfo Login;

		public Interaction getInter() {
			return this.Inter;
		}

		public LoginInfo getLogin() {
			return this.Login;
		}


	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createInteraction(InteractionLoginContainer ctn) {
		int privileges = Profile.PERM_ADDMEDICATION;

		InteractionDAO iDao = new InteractionDAO();
		UserDAO uDao = new UserDAO();
		Response response = null;
		
		if (uDao.authorize(privileges, ctn.getLogin().getUsername(), ctn.getLogin().getPassword())) {
			try {
				iDao.insert(ctn.getInter());
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
