package com.FCI.SWE.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.User;

/**
 * This class contains REST services, also contains action function for web
 * application
 */
@Path("/")
@Produces("text/html")
public class UserController {

	private String serviceUrl, urlParameters, retJson;

	/**
	 * Action function to render Signup page, this function will be executed
	 * using url like this /rest/signup
	 * 
	 * @return sign up page
	 */
	@GET
	@Path("/signup")
	public Response signUp() {
		return Response.ok(new Viewable("/jsp/register")).build();
	}

	/**
	 * Action function to render home page of application, home page contains
	 * only signup and login buttons
	 * 
	 * @return enty point page (Home page of this application)
	 */
	@GET
	@Path("/")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}

	/**
	 * Action function to render login page this function will be executed using
	 * url like this /rest/login
	 * 
	 * @return login page
	 */
	@GET
	@Path("/login")
	public Response login() {
		return Response.ok(new Viewable("/jsp/login")).build();
	}

	/**
	 * Action function to response to signup request, This function will act as
	 * a controller part and it will calls RegistrationService to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided user password
	 * @return Status string
	 */
	@POST
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public String response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		serviceUrl = "http://localhost:8888/rest/RegistrationService";
		try {
			urlParameters = "uname=" + uname + "&email=" + email + "&password="
					+ pass;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Registered Successfully";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error, There is a user with this Email found";
	}

	/**
	 * Action function to response to login request. This function will act as a
	 * controller part, it will calls login service to check user data and get
	 * user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return Home page view
	 **/

	@POST
	@Path("/home")
	@Produces("text/html")
	public Response home(@Context HttpServletRequest req,
			@FormParam("uname") String uname, @FormParam("password") String pass) {
		serviceUrl = "http://localhost:8888/rest/LoginService";
		try {
			urlParameters = "uname=" + uname + "&password=" + pass;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			User user = User.getUser(object.toJSONString());
			map.put("name", user.getName());
			map.put("email", user.getEmail());
			map.put("id", user.getIdString());

			req.getSession(true).setAttribute("current_user_name",
					user.getName());
			req.getSession(true).setAttribute("current_user_email",
					user.getEmail());
			req.getSession(true).setAttribute("current_user_id",
					user.getIdString());

			req.getSession(true).setAttribute("s", map);
			// System.out.println(req.getSession(true).getAttribute("current_user_id"));
			ResponseBuilder x = Response.ok(new Viewable("/jsp/home", map));
			return x.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@GET
	@Path("/logout")
	@Produces("text/html")
	public Response logout(@Context HttpServletRequest req)
			throws ParseException {
		urlParameters = "";
		req.getSession(false).removeAttribute("current_user_name");
		req.getSession(false).removeAttribute("current_user_email");
		req.getSession(false).removeAttribute("current_user_id");

		String current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/LogoutService";
		try {
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			return Response.ok(new Viewable("/jsp/login")).build();
			// connection.disconnect();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}