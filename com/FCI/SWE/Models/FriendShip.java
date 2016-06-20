package com.FCI.SWE.Models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class FriendShip {

	public static boolean sendRequest(String senderID, String receiverID) {

		if (senderID.equals(receiverID))
			return false;

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("notification");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("notification", list
					.get(list.size() - 1).getKey().getId() + 1);

			request.setProperty("senderID", senderID);
			request.setProperty("receiverID", receiverID);
			datastore.put(request);
		} else {
			Entity request = new Entity("notification", 1);

			request.setProperty("senderID", senderID);
			request.setProperty("receiverID", receiverID);
			datastore.put(request);
		}

		return true;

	}

	public static boolean unFriendRequest(String senderID, String receiverID) {
		if (senderID.equals(receiverID))
			return false;

		long ID1 = Long.parseLong(senderID);
		long ID2 = Long.parseLong(receiverID);

		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("friends");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());
		for (Entity entity : p.asIterable()) {
			if (entity.getProperty("friendID1").toString().equals(senderID)
					&& entity.getProperty("friendID2").toString()
							.equals(receiverID)
					|| entity.getProperty("friendID2").toString()
							.equals(senderID)
					&& entity.getProperty("friendID1").toString()
							.equals(receiverID)) {
				Entity req = new Entity("friends", entity.getKey().getId());
				data.delete(entity.getKey());
			}
		}
		return true;

	}

	public static boolean acceptFriendRequest(String senderID, String receiverID) {
		if (senderID.equals(receiverID))
			return false;

		long ID1 = Long.parseLong(senderID), ID2 = Long.parseLong(receiverID);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("friends", list.get(list.size() - 1)
					.getKey().getId() + 1);

			request.setProperty("friendID1", senderID);
			request.setProperty("friendID2", receiverID);
			datastore.put(request);
		} else {
			Entity request = new Entity("friends", 1);

			request.setProperty("friendID1", senderID);
			request.setProperty("friendID2", receiverID);
			datastore.put(request);
		}

		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("notification");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());
		System.out.println(senderID + " " + receiverID);
		for (Entity entity : p.asIterable()) {
			if (entity.getProperty("senderID").toString().equals(senderID)
					&& entity.getProperty("receiverID").toString()
							.equals(receiverID)
					|| entity.getProperty("receiverID").toString()
							.equals(senderID)
					&& entity.getProperty("senderID").toString()
							.equals(receiverID)) {
				Entity req = new Entity("notification", entity.getKey().getId());
				data.delete(entity.getKey());
			}
		}
		return true;

	}

	public static Vector<User> getUsers(String id) {

		long ID = Long.parseLong(id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		HashSet<Long> lis = new HashSet<>();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			lis.add(entity.getKey().getId());
		}

		lis.remove(ID);

		Query gaeQuery1 = new Query("notification");
		PreparedQuery pq1 = datastore.prepare(gaeQuery1);

		for (Entity entity : pq1.asIterable()) {
			if (Long.parseLong(entity.getProperty("senderID").toString()) == ID)
				lis.remove(Long.parseLong(entity.getProperty("receiverID")
						.toString()));
			else if (Long
					.parseLong(entity.getProperty("receiverID").toString()) == ID)
				lis.remove(Long.parseLong(entity.getProperty("senderID")
						.toString()));
		}

		Query gaeQuery2 = new Query("friends");
		PreparedQuery pq2 = datastore.prepare(gaeQuery2);

		for (Entity entity : pq2.asIterable()) {
			if (Long.parseLong(entity.getProperty("friendID1").toString()) == ID)
				lis.remove(Long.parseLong(entity.getProperty("friendID2")
						.toString()));
			else if (Long.parseLong(entity.getProperty("friendID2").toString()) == ID)
				lis.remove(Long.parseLong(entity.getProperty("friendID1")
						.toString()));
		}

		Iterator iterator = lis.iterator();
		Vector<User> temp = new Vector<User>();
		while (iterator.hasNext()) {
			User users = new User();
			users = User.getUser((long) iterator.next());
			temp.add(users);
		}
		return temp;
	}

	public static boolean isFriend(String id1, String id2) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		HashSet<Long> lis = new HashSet<>();

		Query gaeQuery = new Query("friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("friendID1").toString().equals(id1)
					&& entity.getProperty("friendID2").toString().equals(id2))
				return true;
			else if (entity.getProperty("friendID2").toString().equals(id1)
					&& entity.getProperty("friendID1").toString().equals(id2))
				return true;
		}
		return false;
	}

	public static Vector<User> getMyFriends(String id) {

		long ID = Long.parseLong(id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		HashSet<Long> lis = new HashSet<>();

		Query gaeQuery = new Query("friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("friendID1").toString().equals(id))
				lis.add(Long.parseLong(entity.getProperty("friendID2")
						.toString()));
			else if (entity.getProperty("friendID2").toString().equals(id))
				lis.add(Long.parseLong(entity.getProperty("friendID1")
						.toString()));
		}
		Iterator iterator = lis.iterator();
		Vector<User> temp = new Vector<User>();
		while (iterator.hasNext()) {
			User users = new User();
			users = User.getUser((long) iterator.next());
			temp.add(users);
		}
		return temp;
	}

	public static Vector<User> getNotifications(String id) {

		long ID = Long.parseLong(id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		HashSet<Long> lis = new HashSet<>();

		Query gaeQuery = new Query("notification");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("receiverID").toString().equals(id))
				lis.add(Long.parseLong(entity.getProperty("senderID")
						.toString()));
		}

		Iterator iterator = lis.iterator();
		Vector<User> temp = new Vector<User>();
		while (iterator.hasNext()) {
			User users = new User();
			users = User.getUser((long) iterator.next());

			temp.add(users);
		}

		return temp;
	}

}
