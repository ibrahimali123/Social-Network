package com.FCI.SWE.Models;

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

public class User {
	private long id;
	public String name;
	public String email;
	private String password;

	private static User currentActiveUser;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public User() {

	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;

	}

	private void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getIdString() {
		return Long.toString(id);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}

	public static User getCurrentActiveUser() {
		return currentActiveUser;
	}

	public static void logout() {
		currentActiveUser = null;
	}

	/**
	 * 
	 * This static method will form User class using json format contains user
	 * data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	public static User parseUserInfo(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			User user = new User();
			user.setName(object.get("name").toString());
			user.setEmail(object.get("email").toString());
			user.setId(Long.parseLong(object.get("id").toString()));

			return user;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 
	 * This static method will form User class using json format contains user
	 * data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	public static User getUser(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			currentActiveUser = new User(object.get("name").toString(), object
					.get("email").toString(), object.get("password").toString());
			System.out.println(object.get("id").toString());
			currentActiveUser
					.setId(Long.parseLong(object.get("id").toString()));
			return currentActiveUser;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * This static method will form User class using user name and password This
	 * method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static User getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			// System.out.println(entity.getProperty("name").toString());
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				User returnedUser = new User(entity.getProperty("name")
						.toString(), entity.getProperty("email").toString(),
						entity.getProperty("password").toString());
				// System.out.println(entity.toString());

				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	/**
	 * This method will get userName given user id from datastore
	 * 
	 * @return UserName if found or null
	 */
	public static String getUserNameByID(String id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == Long.parseLong(id)) {
				String returnedUser = entity.getProperty("name").toString();
				return returnedUser;
			}
		}
		return null;
	}

	/**
	 * This method will get user object from datastore
	 * 
	 * @return User if found or null
	 */
	public static User getUser(long id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == id) {
				User returnedUser = new User(entity.getProperty("name")
						.toString(), entity.getProperty("email").toString(),
						entity.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			for (Entity entity : pq.asIterable()) {
				if (entity.getProperty("email").toString().equals(this.email)) {
					return false;
				}
			}

			Entity employee = new Entity("users", list.get(list.size() - 1)
					.getKey().getId() + 1);

			employee.setProperty("name", this.name);
			employee.setProperty("email", this.email);
			employee.setProperty("password", this.password);

			datastore.put(employee);
		} else {
			Entity employee = new Entity("users", 1);

			employee.setProperty("name", this.name);
			employee.setProperty("email", this.email);
			employee.setProperty("password", this.password);

			datastore.put(employee);
		}

		return true;

	}

}
