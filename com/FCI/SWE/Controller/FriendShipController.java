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
public class FriendShipController {

	private String serviceUrl, urlParameters, retJson;

	@POST
	@Path("/preaddfriend")
	@Produces("text/html")
	public Response preaddFriend(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/preaddFriendService";
		try {
			urlParameters = "&current_user_id=" + current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");

			Map<String, Vector<User>> PassedUsers = new HashMap<String, Vector<User>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<User> users = new Vector<User>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				users.add(User.parseUserInfo(object.toJSONString()));
			}
			PassedUsers.put("usersList", users);
			return Response.ok(new Viewable("/jsp/preaddfriend", PassedUsers))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@POST
	@Path("/unfriend")
	@Produces("text/html")
	public String unFriend(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("recieverID") String receiverID) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/unFriendService";
		try {
			urlParameters = "recieverID=" + receiverID + "&current_user_id="
					+ current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			if (object.get("Status").equals("OK"))
				return "now, you become unfriend with him";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@POST
	@Path("/addfriend")
	@Produces(MediaType.TEXT_PLAIN)
	public String addFriend(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("recieverID") String receiverID) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/addFriendService";
		try {
			urlParameters = "recieverID=" + receiverID + "&current_user_id="
					+ current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			if (object.get("Status").equals("OK"))
				return "sent Successfully";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@POST
	@Path("/preacceptfriend")
	@Produces("text/html")
	public Response preacceptFriend(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/preacceptFriendService";
		try {
			urlParameters = "&current_user_id=" + current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<User>> PassUsers = new HashMap<String, Vector<User>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<User> users = new Vector<User>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);

				users.add(User.parseUserInfo(object.toJSONString()));
			}
			// System.out.println(users.get(0));
			PassUsers.put("notificationList", users);
			return Response.ok(new Viewable("/jsp/preacceptfriend", PassUsers))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@POST
	@Path("/acceptfriend")
	@Produces(MediaType.TEXT_PLAIN)
	public String acceptFriend(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("recieverID") String receiverID) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/acceptFriendService";
		try {
			urlParameters = "recieverID=" + receiverID + "&current_user_id="
					+ current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			if (object.get("Status").equals("OK"))
				return "you become Friends with him now :) :)";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@POST
	@Path("/showMyfriends")
	@Produces("text/html")
	public Response showMyfriends(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/myFriendsService";
		try {
			urlParameters = "&current_user_id=" + current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<User>> UsersPassed = new HashMap<String, Vector<User>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<User> users = new Vector<User>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				users.add(User.parseUserInfo(object.toJSONString()));
			}
			UsersPassed.put("FriendsList", users);
			return Response.ok(new Viewable("/jsp/myfriends", UsersPassed))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}