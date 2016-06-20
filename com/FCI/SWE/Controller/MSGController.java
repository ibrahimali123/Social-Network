package com.FCI.SWE.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.Message;

@Path("/")
@Produces("text/html")
public class MSGController {

	private String serviceUrl, urlParameters, retJson;

	/*
	 * this method for sending message
	 */
	@POST
	@Path("/sendmsg")
	@Produces(MediaType.TEXT_PLAIN)
	public String SendMsg(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("recieverID") String receiverID,
			@FormParam("text") String text) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/sendmessageService";
		try {
			urlParameters = "recieverID=" + receiverID + "&text=" + text
					+ "&current_user_id=" + current_user_id;

			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			if (object.get("Status").equals("OK"))
				return "Message send ";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * this method for makeing message seen by spesific user
	 */

	@POST
	@Path("/seenmsg")
	@Produces(MediaType.TEXT_PLAIN)
	public String seenmsg(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("msgID") String msgID) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/seenMsgService";
		try {
			urlParameters = "msgID=" + msgID + "&current_user_id="
					+ current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return "";
			if (object.get("Status").equals("OK"))
				return "seen";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * this method for getting message notification
	 */

	@POST
	@Path("/msgnotification")
	@Produces("text/html")
	public Response preseenMsg(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/msgnotification";
		try {
			urlParameters = "&current_user_id=" + current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Message>> PassedMsg = new HashMap<String, Vector<Message>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Message> msg = new Vector<Message>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Message.parseMessageInfo(object.toJSONString()));
			}
			PassedMsg.put("msgnotificationList", msg);
			return Response.ok(new Viewable("/jsp/msgnotification", PassedMsg))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * this method for view messages
	 */

	@POST
	@Path("/messages")
	@Produces("text/html")
	public Response viewMessages(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("receiverID") String receiverID,
			@FormParam("group") String group, @FormParam("single") String single) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/ViewMessageService";
		try {
			urlParameters = "&receiverID=" + receiverID + "&group=" + group
					+ "&single=" + single + "&current_user_id="
					+ current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters, "POST",
					"application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Message>> PassedMsg = new HashMap<String, Vector<Message>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Message> msg = new Vector<Message>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Message.parseMessageInfo(object.toJSONString()));
			}
			PassedMsg.put("messagesList", msg);
			return Response.ok(new Viewable("/jsp/messages", PassedMsg))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}