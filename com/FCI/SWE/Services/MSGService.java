package com.FCI.SWE.Services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.Models.FriendShip;
import com.FCI.SWE.Models.Message;
import com.FCI.SWE.Models.User;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class MSGService {

	@POST
	@Path("/sendmessageService")
	public String sendMessageService(@FormParam("current_user_id") String CurrentUserID,
			@FormParam("recieverID") String recieverID,
			@FormParam("text") String text) {
		JSONObject object = new JSONObject(); 
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timestamp = dateFormat.format(cal.getTime()).toString();
		
		Message m = new Message(CurrentUserID, recieverID,recieverID, timestamp, text);
		if (m.sendMessage())
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}
    
	@POST
	@Path("/seenMsgService")
	public String seenMsgService(@FormParam("current_user_id") String CurrentUserID , @FormParam("msgID") String msgID) {
		JSONObject object = new JSONObject();
		if (Message.seenMsg(msgID,Long.parseLong(CurrentUserID)))
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}
	
	@POST
	@Path("/ViewMessageService")
	public String ViewMessageService(@FormParam("current_user_id") String CurrentUserID ,@FormParam("receiverID") String receiverID , @FormParam("group") String group ,
			@FormParam("single") String single) {
		Vector<Message> msg;
		if(group.equals("null"))
		{
			msg = Message.ViewMessagesBysender(CurrentUserID,receiverID);
		}else{
			msg = Message.ViewMessagesByGroup(CurrentUserID,receiverID);
		}
		
		JSONArray returnedJson = new JSONArray();
		for (Message m : msg) {
			JSONObject object = new JSONObject();
			object.put("text", m.getText());
			object.put("groupID", m.getGroupID());
			object.put("senderID", m.getSenderID());
			object.put("msgID", m.getid());
			object.put("times", m.getTimeStamp());
			object.put("receiverID", m.getReceiverID());
			object.put("seen", "1");
			returnedJson.add(object);
		}

		return returnedJson.toJSONString();

	}
	
	@POST
	@Path("/msgnotification")
	public String msgNotificationService(@FormParam("current_user_id") String CurrentUserID) {
		Vector<Message> msg = Message.getMessageNotification(CurrentUserID);

		JSONArray returnedJson = new JSONArray();
		for (Message m : msg) {
			JSONObject object = new JSONObject();
			object.put("text", m.getText());
			object.put("groupID", m.getGroupID());
			object.put("senderID", m.getSenderID());
			object.put("msgID", m.getid());
			object.put("times", m.getTimeStamp());
			object.put("receiverID", m.getReceiverID());
			object.put("seen", m.getSeen());
			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}
}