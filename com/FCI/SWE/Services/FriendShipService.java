package com.FCI.SWE.Services;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.Models.FriendShip;
import com.FCI.SWE.Models.User;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class FriendShipService extends HttpServlet {

	@POST
	@Path("/addFriendService")
	public String addFriendService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("recieverID") String recieverID) {
		JSONObject object = new JSONObject();
		if (FriendShip.sendRequest(CurrentUserID, recieverID))
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}

	@POST
	@Path("/unFriendService")
	public String unFriendService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("recieverID") String recieverID) {
		JSONObject object = new JSONObject();
		if (FriendShip.unFriendRequest(CurrentUserID, recieverID))
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}

	@POST
	@Path("/preaddFriendService")
	public String preaddFriendService(
			@FormParam("current_user_id") String CurrentUserID) {
		Vector<User> users = FriendShip.getUsers(CurrentUserID);

		JSONArray returnedJson = new JSONArray();
		for (User user : users) {
			JSONObject object = new JSONObject();
			object.put("id", user.getId());
			object.put("name", user.getName());
			object.put("email", user.getEmail());

			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}

	@POST
	@Path("/preacceptFriendService")
	public String preacceptFriendService(
			@FormParam("current_user_id") String CurrentUserID) {
		System.out.println(CurrentUserID + " ---ok");
		Vector<User> users = FriendShip.getNotifications(CurrentUserID);
		JSONArray returnedJson = new JSONArray();
		for (User user : users) {
			JSONObject object = new JSONObject();
			object.put("id", user.getId());
			object.put("name", user.getName());
			object.put("email", user.getEmail());

			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}

	@POST
	@Path("/acceptFriendService")
	public String acceptFriendService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("recieverID") String recieverID) {
		JSONObject object = new JSONObject();
		if (FriendShip.acceptFriendRequest(CurrentUserID, recieverID))
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}

	@POST
	@Path("/myFriendsService")
	public String myFriendsService(
			@FormParam("current_user_id") String CurrentUserID) {
		Vector<User> users = FriendShip.getMyFriends(CurrentUserID);
		System.out.println(CurrentUserID + " ---ok");
		JSONArray returnedJson = new JSONArray();
		for (User user : users) {
			JSONObject object = new JSONObject();
			object.put("id", user.getId());
			object.put("name", user.getName());
			object.put("email", user.getEmail());

			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}
}