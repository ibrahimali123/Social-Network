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

import com.FCI.SWE.Models.Page;
import com.FCI.SWE.Models.Post;

@Path("/")
@Produces("text/html")
public class PageController {

	private String serviceUrl, urlParameters, retJson;

	/**
	 * 
	 * this method for creating page
	 **/
	@POST
	@Path("/createpage")
	@Produces(MediaType.TEXT_PLAIN)
	public String CreatePage(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("pagename") String pagename) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/CreatePageService";
		try {
			String urlParameters = "pagename=" + pagename + "&current_user_id="
					+ current_user_id;
			String retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return "Error , try again";
			if (object.get("Status").equals("OK"))
				return "Page Created Successfully";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * this method for show user page
	 **/

	@POST
	@Path("/mypages")
	@Produces("text/html")
	public Response mypages(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/myPagesService";
		try {
			String urlParameters = "&current_user_id=" + current_user_id;
			String retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Page>> PassedMsg = new HashMap<String, Vector<Page>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Page> msg = new Vector<Page>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Page.parsePageInfo(object.toJSONString()));
			}
			PassedMsg.put("PageList", msg);
			return Response.ok(new Viewable("/jsp/mypages", PassedMsg)).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * this method for searching about any page
	 **/

	@POST
	@Path("/searchaboutpage")
	@Produces("text/html")
	public Response searchaboutpage(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("pagename") String pagename) {
		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		serviceUrl = "http://localhost:8888/rest/SearchAboutPageService";
		try {
			String urlParameters = "&current_user_id=" + current_user_id
					+ "&pagename=" + pagename;
			String retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			Map<String, Vector<Page>> PassedMsg = new HashMap<String, Vector<Page>>();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(retJson);
			Vector<Page> msg = new Vector<Page>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject object;
				object = (JSONObject) array.get(i);
				msg.add(Page.parsePageInfo(object.toJSONString()));
			}
			PassedMsg.put("PageList", msg);
			return Response.ok(new Viewable("/jsp/searchaboutpage", PassedMsg))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * this method for show or view page
	 **/

	@POST
	@Path("/showpage")
	@Produces("text/html")
	public Response showpage(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("pageid") String pageid) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		req.getSession(true).setAttribute("current_page_id", pageid);
		req.getSession(true).setAttribute("current_page_name",
				Page.getPageNameByID(pageid));

		if (Page.isLiked(current_user_id, pageid))
			req.getSession(true).setAttribute("like", "1");
		else
			req.getSession(true).setAttribute("like", "0");

		if (Page.getPageOwnerByID(pageid).equals(current_user_id))
			req.getSession(true).setAttribute("owner", "1");
		else
			req.getSession(true).setAttribute("owner", "0");

		serviceUrl = "http://localhost:8888/rest/ShowPageService";
		try {
			String urlParameters = "&current_user_id=" + current_user_id
					+ "&pageid=" + pageid;
			String retJson = Connection.connect(serviceUrl, urlParameters,
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
			PassedMsg.put("PagePostsList", msg);
			return Response.ok(new Viewable("/jsp/showpage", PassedMsg))
					.build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * this method for creating CreatePagePost
	 **/

	@POST
	@Path("/pagepost")
	@Produces(MediaType.TEXT_PLAIN)
	public String pagepost(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("text") String text,
			@FormParam("current_page_id") String current_page_id) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		current_page_id = (String) req.getSession().getAttribute(
				"current_page_id");

		serviceUrl = "http://localhost:8888/rest/CreatePagePostService";
		try {
			String urlParameters = "text=" + text + "&current_user_id="
					+ current_user_id + "&current_page_id=" + current_page_id;

			String retJson = Connection.connect(serviceUrl, urlParameters,
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
	 * this method for like Page
	 **/

	@POST
	@Path("/likepage")
	@Produces(MediaType.TEXT_PLAIN)
	public String likepage(@Context HttpServletRequest req,
			@FormParam("current_user_id") String current_user_id,
			@FormParam("current_page_id") String current_page_id) {

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		current_page_id = (String) req.getSession().getAttribute(
				"current_page_id");

		serviceUrl = "http://localhost:8888/rest/LikePageService";
		try {
			String urlParameters = "&current_user_id=" + current_user_id
					+ "&current_page_id=" + current_page_id;

			String retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return "Error , try again";
			if (object.get("Status").equals("OK"))
				return "Done Successfully";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}