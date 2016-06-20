package com.FCI.SWE.Models;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Message {

	private long id;
	public String senderID;
	public String groupID;
	public String receiverID;
	public String text;
	public String seen;
	public String times;

	public Message() {

	}

	public Message(String string, String receiver, String groupID,
			String timestamp, String text) {
		this.senderID = string;
		this.receiverID = receiver;
		this.times = timestamp;
		this.groupID = groupID;
		this.text = text;
	}

	private void setId(long id) {
		this.id = id;
	}

	private void setTimeStamp(String timestamp) {
		this.times = timestamp;
	}

	private void setText(String text) {
		this.text = text;
	}

	private void setReceiver(String receiverID) {
		this.receiverID = receiverID;
	}

	private void setGroupID(String groupid) {
		this.groupID = groupid;
	}

	private void setSender(String string) {
		this.senderID = string;
	}

	private void setSeen(String string) {
		this.seen = string;
	}

	public String getText() {
		return text;
	}

	public String getSeen() {
		return seen;
	}

	public long getid() {
		return id;
	}

	public String getGroupID() {
		return groupID;
	}

	public String getTimeStamp() {
		return times;
	}

	public String getReceiverID() {
		return receiverID;
	}

	public String getSenderID() {
		return senderID;
	}

	public static Message parseMessageInfo(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			Message msg = new Message();
			msg.setSender(object.get("senderID").toString());
			msg.setGroupID(object.get("groupID").toString());
			msg.setText(object.get("text").toString());
			msg.setReceiver(object.get("receiverID").toString());
			msg.setSeen(object.get("seen").toString());
			msg.setTimeStamp(object.get("times").toString());
			msg.setId(Long.parseLong(object.get("msgID").toString()));
			return msg;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static boolean seenMsg(String msgID, long MyID) {
		long ID = Long.parseLong(msgID);
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("msgnotification");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());
		System.out.println(ID + "  " + MyID);
		for (Entity entity : p.asIterable()) {
			if (Long.parseLong(entity.getProperty("msgID").toString()) == ID
					&& Long.parseLong(entity.getProperty("receiverID")
							.toString()) == MyID) {
				System.out.println("ok");
				Entity msg = new Entity("msgnotification", entity.getKey()
						.getId());
				msg.setProperty("GroupID", entity.getProperty("GroupID"));
				msg.setProperty("senderID", entity.getProperty("senderID"));
				msg.setProperty("msgID", entity.getProperty("msgID"));
				msg.setProperty("receiverID", entity.getProperty("receiverID"));
				msg.setProperty("timestamp", entity.getProperty("timestamp"));
				msg.setProperty("text", entity.getProperty("text"));
				msg.setProperty("seen", 1);
				data.put(msg);
			}
		}
		return true;
	}

	public void MessageNotification(long senderID2, String ids[], long groupID,
			String timestamp, String text, long recordID) {
		DatastoreService datastorenotifi = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuerynotifi = new Query("msgnotification");
		PreparedQuery pq1 = datastorenotifi.prepare(gaeQuerynotifi);
		List<Entity> list1 = pq1.asList(FetchOptions.Builder.withDefaults());

		for (int i = 0; i < ids.length; i++) {
			PreparedQuery pqqq = datastorenotifi.prepare(gaeQuerynotifi);
			List<Entity> list = pqqq
					.asList(FetchOptions.Builder.withDefaults());
			if (list.size() != 0) {
				Entity msg = new Entity("msgnotification", list
						.get(list.size() - 1).getKey().getId() + 1);
				if (ids.length == 1)
					msg.setProperty("GroupID", -1);
				else
					msg.setProperty("GroupID", groupID);
				msg.setProperty("msgID", recordID);
				msg.setProperty("senderID", senderID2);
				msg.setProperty("receiverID", ids[i]);
				msg.setProperty("timestamp", timestamp);
				msg.setProperty("text", text);
				msg.setProperty("seen", 0);
				datastorenotifi.put(msg);
			} else {
				Entity msg = new Entity("msgnotification", 1);
				if (ids.length == 1)
					msg.setProperty("GroupID", -1);
				else
					msg.setProperty("GroupID", groupID);
				msg.setProperty("senderID", senderID2);
				msg.setProperty("msgID", recordID);
				msg.setProperty("receiverID", ids[i]);
				msg.setProperty("timestamp", timestamp);
				msg.setProperty("text", text);
				msg.setProperty("seen", 0);
				datastorenotifi.put(msg);
			}

		}

	}

	public int getGroupIDFromDB(String senderID, String GroupIDSring) {
		String GroupIDS[] = GroupIDSring.split("/");
		String[] GroupIDArray = new String[GroupIDS.length + 1];
		for (int i = 0; i < GroupIDS.length; i++) {
			GroupIDArray[i] = GroupIDS[i];
		}
		GroupIDArray[GroupIDS.length] = senderID;
		Arrays.sort(GroupIDArray);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("receiverID").toString()
					.equals(GroupIDSring)
					&& entity.getProperty("senderID").toString()
							.equals(senderID)) {
				return Integer.parseInt(entity.getProperty("GroupID")
						.toString());
			}
		}
		for (Entity entity : pq.asIterable()) {
			GroupIDSring = entity.getProperty("receiverID").toString();
			senderID = entity.getProperty("senderID").toString();

			String ides2[] = GroupIDSring.split("/");
			String[] ides22 = new String[ides2.length + 1];

			if (ides2.length + 1 != GroupIDS.length)
				continue;

			for (int i = 0; i < ides2.length; i++) {
				ides22[i] = ides2[i];
			}
			ides22[ides2.length] = senderID;

			Arrays.sort(ides22);

			int cnt = 0;
			for (int i = 0; i < ides22.length; i++) {
				if (GroupIDS[i].equals(ides22[i]))
					cnt++;
			}

			if (cnt == GroupIDS.length)
				return Integer.parseInt(entity.getProperty("GroupID")
						.toString());

		}
		return -1;
	}

	public Boolean sendMessage() {
		String[] ids = receiverID.split("/");
		if (hasContainID(ids, this.senderID))
			return false;

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		long GroupID, recordID;

		if (list.size() == 0) {
			GroupID = recordID = 1;
		} else {
			System.out.println(getGroupIDFromDB(this.senderID, this.receiverID)
					+ " " + this.senderID + " " + this.receiverID);
			if (getGroupIDFromDB(this.senderID, this.receiverID) == -1)
				GroupID = (Long) list.get(list.size() - 1).getKey().getId() + 1;
			else
				GroupID = getGroupIDFromDB(this.senderID, this.receiverID);
			recordID = list.get(list.size() - 1).getKey().getId() + 1;
		}

		if (list.size() != 0) {
			Entity msg = new Entity("messages", list.get(list.size() - 1)
					.getKey().getId() + 1);
			if (ids.length == 1)
				msg.setProperty("GroupID", -1);
			else
				msg.setProperty("GroupID", GroupID);
			msg.setProperty("senderID", this.senderID);
			msg.setProperty("receiverID", this.receiverID);
			msg.setProperty("timestamp", this.times);
			msg.setProperty("text", this.text);
			datastore.put(msg);
		} else {
			Entity msg = new Entity("messages", 1);
			if (ids.length == 1)
				msg.setProperty("GroupID", -1);
			else
				msg.setProperty("GroupID", GroupID);
			msg.setProperty("senderID", this.senderID);
			msg.setProperty("receiverID", this.receiverID);
			msg.setProperty("timestamp", this.times);
			msg.setProperty("text", this.text);
			datastore.put(msg);
		}

		MessageNotification(Long.valueOf(this.senderID), ids, GroupID,
				this.times, this.text, recordID);
		return true;
	}

	public static Message getMsg(long id, long currentserID, String seen) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		if (id < 0) {
			id *= -1;
			for (Entity entity : pq.asIterable()) {
				if (entity.getProperty("receiverID").toString()
						.equals(String.valueOf(currentserID))
						&& Long.parseLong(entity.getProperty("senderID")
								.toString()) == id) {
					Message returnedMsg = new Message(entity.getProperty(
							"senderID").toString(), entity.getProperty(
							"receiverID").toString(), entity.getProperty(
							"GroupID").toString(), String.valueOf(entity
							.getProperty("timestamp").toString()), entity
							.getProperty("text").toString());
					returnedMsg.setSeen(seen);
					returnedMsg.setId(entity.getKey().getId());
					return returnedMsg;
				}
			}
		} else {
			for (Entity entity : pq.asIterable()) {
				if (Long.parseLong(entity.getProperty("GroupID").toString()) == id) {
					Message returnedMsg = new Message(entity.getProperty(
							"senderID").toString(), entity.getProperty(
							"receiverID").toString(), entity.getProperty(
							"GroupID").toString(), String.valueOf(entity
							.getProperty("timestamp").toString()), entity
							.getProperty("text").toString());
					returnedMsg.setSeen(seen);
					returnedMsg.setId(entity.getKey().getId());
					return returnedMsg;
				}
			}
		}
		return null;
	}

	public static Message getMsgByID(long id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == id) {
				Message returnedMsg = new Message(entity
						.getProperty("senderID").toString(), entity
						.getProperty("receiverID").toString(), entity
						.getProperty("GroupID").toString(),
						String.valueOf(entity.getProperty("timestamp")
								.toString()), entity.getProperty("text")
								.toString());
				returnedMsg.setId(entity.getKey().getId());
				returnedMsg.setSender(User.getUserNameByID(entity.getProperty(
						"senderID").toString()));
				if (!entity.getProperty("receiverID").toString().contains("/"))
					returnedMsg.setReceiver(User.getUserNameByID(entity
							.getProperty("receiverID").toString()));
				return returnedMsg;
			}
		}
		return null;
	}

	public static Vector<Message> getMessageNotification(String id) {
		long ID = Long.parseLong(id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("msgnotification");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Vector<Message> temp = new Vector<Message>();
		long groupid;
		for (Entity entity : pq.asIterable()) {
			if (Long.parseLong(entity.getProperty("receiverID").toString()) == ID) {
				if (Long.parseLong(entity.getProperty("GroupID").toString()) == -1)
					groupid = -1
							* Long.parseLong(entity.getProperty("senderID")
									.toString());
				else
					groupid = Long.parseLong(entity.getProperty("GroupID").toString());

				Message msg = new Message();
				msg = Message.getMsg(groupid, ID, entity.getProperty("seen")
						.toString());
				temp.add(msg);
			}
		}
		return temp;
	}

	public static boolean hasContainID(String ids[], String id) {
		for (int i = 0; i < ids.length; i++)
			if (ids[i].equals(id))
				return true;

		return false;
	}

	public static Vector<Message> ViewMessagesBysender(String myid,
			String receiverID) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Vector<Message> temp = new Vector<Message>();

		if (myid.equals(receiverID))
			return temp;

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("senderID").toString().equals(myid)
					&& entity.getProperty("receiverID").toString()
							.equals(receiverID)) {
				Message msg = new Message();
				msg = Message.getMsgByID(entity.getKey().getId());
				temp.add(msg);
			} else if (entity.getProperty("receiverID").toString().equals(myid)
					&& entity.getProperty("senderID").toString()
							.equals(receiverID)) {
				Message msg = new Message();
				msg = Message.getMsgByID(entity.getKey().getId());
				msg.setSeen("1");
				temp.add(msg);
			}
		}
		return temp;
	}

	public static Vector<Message> ViewMessagesByGroup(String myid,
			String receiverID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Vector<Message> temp = new Vector<Message>();
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("GroupID").toString().equals(receiverID)) {
				String[] ids = entity.getProperty("receiverID").toString()
						.split("/");

				if (entity.getProperty("senderID").toString().equals(myid)
						|| hasContainID(ids, myid)) {
					Message msg = new Message();
					msg = Message.getMsgByID(entity.getKey().getId());
					msg.setSeen("1");
					temp.add(msg);
				}
			}
		}
		return temp;
	}

}
