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
import com.avkapp.data.LoginInfo;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.HashMap;


@Path("/login")
public class LoginResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginInfo log) {
		UserDAO iDao = new UserDAO();
		Response response = null;

		try {
			if (iDao.validate(log)) {
        User user = iDao.getUser(log);

        if (user != null) {
          HashMap<String, String> hm = new HashMap<String, String>();
          hm.put("firstname", user.getFirstname());
          hm.put("lastname",  user.getLastname());
          hm.put("email",     user.getEmail());
          hm.put("phone",     user.getPhone());
          hm.put("profile",   (String)user.getProfile());
          hm.put("profileText", getProfileAsText(user.getProfile()));
          hm.put("office",    (String)user.getOffice());
          hm.put("officeText", getOfficeAsText(user.getOffice()));

          org.json.JSONObject data = new org.json.JSONObject(hm);

		  		response = Response.status(200).entity(data.toString()).build();
        }
        else {
          response = Response.status(500).build();
        }
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
