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

import com.FCI.SWE.Models.Post;

@Path("/")
@Produces("text/html")
public class PostController {

	private String serviceUrl, urlParameters, retJson;
	/**
	 * 
	 * this method for creating post
	 **/
	
	@POST
	@Path("/createpost")
	@Produces(MediaType.TEXT_PLAIN)
	public String CreatePost(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("text") String text,
			@FormParam("privatee") String privatee,
			@FormParam("publice") String publice) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/CreatePostService";
		try {
			urlParameters = "text=" + text + "&current_user_id="
					+ current_user_id + "&privatee=" + privatee + "&publice="
					+ publice;

			retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return "Error , try again";
			if (object.get("Status").equals("OK"))
				return "Post Created Successfully";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * this method for view user timeline
	 **/
	
	@POST
	@Path("/timeline")
	@Produces("text/html")
	public Response timeline(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");

		serviceUrl = "http://localhost:8888/rest/TimeLineService";
		try {
			urlParameters = "&current_user_id=" + current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Post>> PassedMsg = new HashMap<String, Vector<Post>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Post> msg = new Vector<Post>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Post.parsePostInfo(object.toJSONString()));
				// System.out.println(Post.parsePostInfo(object.toJSONString()).getVa());
			}

			PassedMsg.put("PostsList", msg);
			return Response.ok(new Viewable("/jsp/timeline", PassedMsg))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * this method for getting posts from DB
	 **/
	
	@POST
	@Path("/posts")
	@Produces("text/html")
	public Response posts(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");

		serviceUrl = "http://localhost:8888/rest/PostsService";
		try {
			urlParameters = "&current_user_id=" + current_user_id;
			retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Post>> PassedMsg = new HashMap<String, Vector<Post>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Post> msg = new Vector<Post>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Post.parsePostInfo(object.toJSONString()));
			}

			PassedMsg.put("PostsList", msg);
			return Response.ok(new Viewable("/jsp/posts", PassedMsg)).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * this method for search about tag
	 **/
	
	@POST
	@Path("/searchtag")
	@Produces("text/html")
	public Response searchtag(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("tagname") String tagname) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");

		serviceUrl = "http://localhost:8888/rest/SearchTagService";
		try {
			urlParameters = "&current_user_id=" + current_user_id
					+ "&tagname=" + tagname;
			retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Post>> PassedMsg = new HashMap<String, Vector<Post>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Post> msg = new Vector<Post>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Post.parsePostInfo(object.toJSONString()));
			}

			PassedMsg.put("PostsList", msg);
			return Response.ok(new Viewable("/jsp/tags", PassedMsg)).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * this method for like post given its id
	 **/
	
	public void likepost(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("post_id") String post_id) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/LikePostService";

		urlParameters = "post_id=" + post_id + "&current_user_id="
				+ current_user_id;

		retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");

	}

	/**
	 * 
	 * this method for share post
	 **/
	
	@POST
	@Path("/sharepost")
	@Produces(MediaType.TEXT_PLAIN)
	public String sharepost(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("post_id") String post_id) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/SharePostService";
		try {
			urlParameters = "post_id=" + post_id + "&current_user_id="
					+ current_user_id;

			retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return "Error , try again";
			if (object.get("Status").equals("OK"))
				return "Post Created Successfully";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
