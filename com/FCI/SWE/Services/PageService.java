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
import com.FCI.SWE.Models.Page;
import com.FCI.SWE.Models.Post;
import com.FCI.SWE.Models.User;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class PageService {

	@POST
	@Path("/CreatePageService")
	public String CreatePageService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("pagename") String pagename) {

		JSONObject object = new JSONObject();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timestamp = dateFormat.format(cal.getTime()).toString();

		Page m = new Page(Long.parseLong(CurrentUserID), pagename, timestamp, 0);

		if (m.createPage())
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}

	@POST
	@Path("/CreatePagePostService")
	public String CreatePagePostService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("text") String text,
			@FormParam("current_page_id") String current_page_id) {

		JSONObject object = new JSONObject();
		if (Page.CreatePostOnPage(text, CurrentUserID, current_page_id))
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}

	@POST
	@Path("/myPagesService")
	public String myPagesService(
			@FormParam("current_user_id") String CurrentUserID) {
		Vector<Page> msg = Page.getMyPages(CurrentUserID);

		JSONArray returnedJson = new JSONArray();
		for (Page p : msg) {
			JSONObject object = new JSONObject();
			object.put("ownerID", p.getOwnerID());
			object.put("PageName", p.getPageName());
			object.put("CreationTime", p.getCreationTime());
			object.put("NumberOFLike", p.getNumberOFLike());
			object.put("id", p.getId());
			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}

	@POST
	@Path("/SearchAboutPageService")
	public String SearchAboutPageService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("pagename") String pagename) {
		Vector<Page> page = Page.searchAboutPage(pagename);

		JSONArray returnedJson = new JSONArray();
		for (Page p : page) {
			JSONObject object = new JSONObject();
			object.put("ownerID", p.getOwnerID());
			object.put("PageName", p.getPageName());
			object.put("CreationTime", p.getCreationTime());
			object.put("NumberOFLike", p.getNumberOFLike());
			object.put("id", p.getId());
			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}

	@POST
	@Path("/ShowPageService")
	public String ShowPageService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("pageid") String pageid) {
		Vector<Post> page = Page.getPagePosts(pageid);

		JSONArray returnedJson = new JSONArray();
		for (Post p : page) {
			JSONObject object = new JSONObject();
			object.put("ownerID", p.getOwnerID());
			object.put("pageID", p.getpageID());
			object.put("text", p.gettext());
			object.put("privacy", p.getprivacy());
			object.put("hashTag", p.getHashTag());
			object.put("CreationTime", p.getCreationTime());
			object.put("NumberOFLike", p.getNumberOFLike());
			object.put("NumberOFShare", p.getNumberOFShare());
			object.put("id", p.getId());
			returnedJson.add(object);
		}

		return returnedJson.toJSONString();
	}

	@POST
	@Path("/LikePageService")
	public String LikePageService(
			@FormParam("current_user_id") String CurrentUserID,
			@FormParam("current_page_id") String pageid) {

		JSONObject object = new JSONObject();
		if (Page.likePage(CurrentUserID, pageid))
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");

		return object.toString();
	}
}