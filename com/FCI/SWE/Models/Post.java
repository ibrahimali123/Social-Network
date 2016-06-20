package com.FCI.SWE.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Post {
	private String userID, text, privacy, hashTag, creationTime, va;
	private long id, pageID, numberOFLike, numberOFShare;

	public Post(String userID, long pageID, String text, String creationTime,
			long numberOFLike) {
		this.userID = userID;
		this.pageID = pageID;
		this.text = text;
		this.creationTime = creationTime;
		this.numberOFLike = numberOFLike;
	}

	public Post() {
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	private void settext(String text) {
		this.text = text;
	}

	public String gettext() {
		return text;
	}

	public void setOwnerID(String userID) {
		this.userID = userID;
	}

	public String getOwnerID() {
		return userID;
	}

	public void setpageID(long pageID) {
		this.pageID = pageID;
	}

	public void setVa(String va_) {
		this.va = va_;
	}

	public long getpageID() {
		return pageID;
	}

	public void setNumberOFLike(long numberOFLike) {
		this.numberOFLike = numberOFLike;
	}

	public long getNumberOFLike() {
		return numberOFLike;
	}

	public void setNumberOFShare(long numberOFShare) {
		this.numberOFShare = numberOFShare;
	}

	public long getNumberOFShare() {
		return numberOFShare;
	}

	private void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setprivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getprivacy() {
		return privacy;
	}

	public String getVa() {
		return va;
	}

	public static Post parsePostInfo(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			Post page = new Post();
			page.setId(Long.parseLong(object.get("id").toString()));
			page.setOwnerID(object.get("ownerID").toString());
			page.setpageID(Long.parseLong(object.get("pageID").toString()));
			page.settext(object.get("text").toString());
			page.setprivacy(object.get("privacy").toString());
			page.setHashTag(object.get("hashTag").toString());
			page.setCreationTime(object.get("CreationTime").toString());
			page.setNumberOFLike(Long.parseLong(object.get("NumberOFLike")
					.toString()));
			page.setNumberOFShare(Long.parseLong(object.get("NumberOFShare")
					.toString()));
			page.setVa(object.get("va").toString());

			return page;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * This method will be used to update post likes counter by +1 in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static boolean updatelikeCounter(String postid) {
		long postID = Long.parseLong(postid);
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("pagesposts");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : p.asIterable()) {
			if (entity.getKey().getId() == postID) {
				Entity msg = new Entity("pagesposts", entity.getKey().getId());
				msg.setProperty("ownerID", entity.getProperty("ownerID"));
				msg.setProperty("pageID", entity.getProperty("pageID"));
				msg.setProperty("text", entity.getProperty("text"));
				msg.setProperty("privacy", entity.getProperty("privacy"));
				msg.setProperty("hashTag", entity.getProperty("hashTag"));
				msg.setProperty("creationTime",
						entity.getProperty("creationTime"));
				msg.setProperty("numberOfLike",
						(long) entity.getProperty("numberOfLike") + 1);
				msg.setProperty("NumberOFShare",
						entity.getProperty("NumberOFShare"));
				data.put(msg);
			}
		}
		return true;
	}

	/**
	 * This method will be used to like post to table postslikes in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static boolean likePost(String userID, String postID) {
		long owner = Long.parseLong(userID);
		long post = Long.parseLong(postID);

		if (isLikePost(userID, postID))
			return false;

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("postslikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("postslikes", list.get(list.size() - 1)
					.getKey().getId() + 1);

			request.setProperty("userID", owner);
			request.setProperty("postID", postID);

			datastore.put(request);
		} else {
			Entity request = new Entity("postslikes", 1);

			request.setProperty("userID", owner);
			request.setProperty("postID", postID);

			datastore.put(request);
		}
		updatelikeCounter(postID);
		return true;
	}

	/**
	 * This method will be used to like post to table pagesposts in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static boolean updatelikeCounter(String post, int cha) {
		long postID = Long.parseLong(post);
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("pagesposts");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : p.asIterable()) {
			if (entity.getKey().getId() == postID) {
				Entity msg = new Entity("pagesposts", entity.getKey().getId());
				msg.setProperty("ownerID", entity.getProperty("ownerID"));
				msg.setProperty("pageID", entity.getProperty("pageID"));
				msg.setProperty("text", entity.getProperty("text"));
				msg.setProperty("privacy", entity.getProperty("privacy"));
				msg.setProperty("hashTag", entity.getProperty("hashTag"));
				msg.setProperty("creationTime",
						entity.getProperty("creationTime"));
				msg.setProperty("numberOfLike",
						(long) entity.getProperty("numberOfLike") + cha);
				msg.setProperty("NumberOFShare",
						entity.getProperty("NumberOFShare"));
				data.put(msg);
			}
		}
		return true;
	}

	/**
	 * This method will be used to save post to table in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static boolean CreatePost(String text, String ownerID, String privacy) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timestamp = dateFormat.format(cal.getTime()).toString();

		String hashTag = "";
		long owner = Long.parseLong(ownerID);
		if (text.contains("#"))
			hashTag = text.substring(text.indexOf("#"), text.indexOf("#")
					+ text.length() - text.indexOf("#"));

		String arr[] = hashTag.split(" ");
		hashTag = arr[0];

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("pagesposts", list.get(list.size() - 1)
					.getKey().getId() + 1);

			request.setProperty("ownerID", owner);
			request.setProperty("pageID", "-1");
			request.setProperty("text", text);
			request.setProperty("creationTime", timestamp);
			request.setProperty("privacy", privacy);
			request.setProperty("hashTag", hashTag);
			request.setProperty("numberOfLike", 0);
			request.setProperty("NumberOFShare", 0);

			datastore.put(request);
		} else {
			Entity request = new Entity("pagesposts", 1);

			request.setProperty("ownerID", owner);
			request.setProperty("pageID", "-1");
			request.setProperty("text", text);
			request.setProperty("creationTime", timestamp);
			request.setProperty("privacy", privacy);
			request.setProperty("hashTag", hashTag);
			request.setProperty("numberOfLike", 0);
			request.setProperty("NumberOFShare", 0);

			datastore.put(request);
		}

		return true;
	}

	/**
	 * This method will be used to increase shares counter by 1 to table
	 * pagesposts in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static boolean UpdateSharesCounter(String userID, String postID) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timestamp = dateFormat.format(cal.getTime()).toString();

		long ID = Long.parseLong(postID);
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("pagesposts");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : p.asIterable()) {
			if (entity.getKey().getId() == ID) {
				Entity msg = new Entity("pagesposts", entity.getKey().getId());
				msg.setProperty("ownerID", entity.getProperty("ownerID"));
				msg.setProperty("pageID", entity.getProperty("pageID"));
				msg.setProperty("text", entity.getProperty("text"));
				msg.setProperty("privacy", entity.getProperty("privacy"));
				msg.setProperty("hashTag", entity.getProperty("hashTag"));
				msg.setProperty("creationTime",
						entity.getProperty("creationTime"));
				msg.setProperty("numberOfLike",
						entity.getProperty("numberOfLike"));
				msg.setProperty("NumberOFShare",
						(long) entity.getProperty("NumberOFShare") + 1);
				data.put(msg);

				Entity request = new Entity("pagesposts", lis
						.get(lis.size() - 1).getKey().getId() + 1);

				request.setProperty("ownerID", userID);
				request.setProperty("pageID", -1);
				request.setProperty("text", entity.getProperty("text"));
				request.setProperty("creationTime", timestamp);
				request.setProperty("privacy", entity.getProperty("privacy"));
				request.setProperty("hashTag", entity.getProperty("hashTag"));
				request.setProperty("numberOfLike", 0);
				request.setProperty("NumberOFShare", 0);

				data.put(request);
			}
		}
		return true;
	}

	/**
	 * This method will be used to liker of post postssposts in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static HashSet<Long> getUserLikePost(String curentID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		HashSet<Long> temp = new HashSet<>();

		Query gaeQuery = new Query("postslikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("userID").toString().equals(curentID)) {
				temp.add(Long
						.parseLong(entity.getProperty("postID").toString()));
			}
		}
		return temp;
	}

	/**
	 * This method will be used to check if the current user is like the post or
	 * not in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static boolean isLikePost(String userID, String postID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("postslikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("userID").toString().equals(userID)
					&& entity.getProperty("postID").toString().equals(postID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method will be used to dislike of post in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public static void disLike(String userID, String postID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("postslikes");
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("userID").toString().equals(userID)
					&& entity.getProperty("postID").toString().equals(postID)) {
				data.delete(entity.getKey());
				break;
			}
		}
		updatelikeCounter(postID, -1);

	}

}
