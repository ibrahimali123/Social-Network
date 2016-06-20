package com.FCI.SWE.Models;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
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

public class Page {
	private long id;
	private long owner;
	private String PageName;
	private String creationTime;
	private long numberOFLike;

	public Page(long owner, String PageName, String creationTime,
			long numberOFLike) {
		this.owner = owner;
		this.PageName = PageName;
		this.creationTime = creationTime;
		this.numberOFLike = numberOFLike;
	}

	public Page() {
		// TODO Auto-generated constructor stub
	}

	private void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	private void setPageName(String PageName) {
		this.PageName = PageName;
	}

	public String getPageName() {
		return PageName;
	}

	private void setOwnerID(long owner) {
		this.owner = owner;
	}

	public long getOwnerID() {
		return owner;
	}

	private void setNumberOFLike(long numberOFLike) {
		this.numberOFLike = numberOFLike;
	}

	public long getNumberOFLike() {
		return numberOFLike;
	}

	private void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public static Page parsePageInfo(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			Page page = new Page();
			page.setId(Long.parseLong(object.get("id").toString()));
			page.setOwnerID(Long.parseLong(object.get("ownerID").toString()));
			page.setPageName(object.get("PageName").toString());
			page.setCreationTime(object.get("CreationTime").toString());
			page.setNumberOFLike(Long.parseLong(object.get("NumberOFLike")
					.toString()));
			return page;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public boolean createPage() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("pages", list.get(list.size() - 1)
					.getKey().getId() + 1);

			request.setProperty("ownerID", this.owner);
			request.setProperty("pageName", this.PageName);
			request.setProperty("creationTime", this.creationTime);
			request.setProperty("numberOfLike", this.numberOFLike);

			datastore.put(request);
		} else {
			Entity request = new Entity("pages", 1);

			request.setProperty("ownerID", this.owner);
			request.setProperty("pageName", this.PageName);
			request.setProperty("creationTime", this.creationTime);
			request.setProperty("numberOfLike", this.numberOFLike);

			datastore.put(request);
		}

		return true;

	}

	public static Page getPage(long id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == id) {
				Page returnedPage = new Page(Long.parseLong(entity.getProperty(
						"ownerID").toString()), entity.getProperty("pageName")
						.toString(), entity.getProperty("creationTime")
						.toString(), Long.parseLong(entity.getProperty(
						"numberOfLike").toString()));
				returnedPage.setId(entity.getKey().getId());
				return returnedPage;
			}
		}

		return null;
	}

	public static Vector<Page> getMyPages(String id) {
		long ID = Long.parseLong(id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<Page> temp = new Vector<Page>();

		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("ownerID").toString().equals(id)) {
				Page p = new Page();
				p = Page.getPage(entity.getKey().getId());
				temp.add(p);
			}
		}
		return temp;
	}
	
	public static boolean UpdateLikesCounter(String pageID) {
		long ID = Long.parseLong(pageID);
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query gaQuery = new Query("pages");
		PreparedQuery p = data.prepare(gaQuery);
		List<Entity> lis = p.asList(FetchOptions.Builder.withDefaults());
		
		for (Entity entity : p.asIterable()) {
			if (entity.getKey().getId() == ID) {				
				Entity msg = new Entity("pages", entity.getKey()
						.getId());
				msg.setProperty("ownerID", entity.getProperty("ownerID"));
				msg.setProperty("pageName", entity.getProperty("pageName"));
				msg.setProperty("creationTime", entity.getProperty("creationTime"));
				msg.setProperty("numberOfLike", (long)entity.getProperty("numberOfLike") + 1);
				data.put(msg);
			}
		}
		return true;
	}
	
	public static boolean CreatePostOnPage(String text , String ownerID ,String pageID) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timestamp = dateFormat.format(cal.getTime()).toString();
		
		long owner = Long.parseLong(ownerID);
		long page = Long.parseLong(pageID);
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("pagesposts", list.get(list.size() - 1)
					.getKey().getId() + 1);

			request.setProperty("ownerID", owner);
			request.setProperty("pageID", page);
			request.setProperty("text", text);
			request.setProperty("creationTime", timestamp);
			request.setProperty("privacy", "");
			request.setProperty("hashTag", "");
			request.setProperty("numberOfLike", 0);
			request.setProperty("NumberOFShare", 0);
			
			datastore.put(request);
		} else {
			Entity request = new Entity("pagesposts", 1);

			request.setProperty("ownerID", owner);
			request.setProperty("pageID", page);
			request.setProperty("text", text);
			request.setProperty("creationTime", timestamp);
			request.setProperty("privacy", "");
			request.setProperty("hashTag", "");
			request.setProperty("numberOfLike", 0);
			request.setProperty("NumberOFShare", 0);

			datastore.put(request);
		}

		return true;
	}
    public static boolean likePage(String userID ,String pageID) {
		long owner = Long.parseLong(userID) , page = Long.parseLong(pageID);
		
		if (isLiked(userID,pageID))  return false;
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pageslikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		if (list.size() != 0) {
			Entity request = new Entity("pageslikes", list.get(list.size() - 1)
					.getKey().getId() + 1);

			request.setProperty("userID", owner);
			request.setProperty("pageID", page);

			datastore.put(request);
		} else {
			Entity request = new Entity("pageslikes", 1);

			request.setProperty("userID", owner);
			request.setProperty("pageID", page);

			datastore.put(request);
		}
		UpdateLikesCounter(pageID);
		return true;
	}
	
    public static Vector<Page> searchAboutPage(String pageName) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<Page> temp = new Vector<Page>();

		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("pageName").toString().contains(pageName)
		          || pageName.contains(entity.getProperty("pageName").toString())) {
				Page p = new Page();
				p = Page.getPage(entity.getKey().getId());
				temp.add(p);
			}
		}
		return temp;
	}
    
    public static boolean isLiked(String userID , String pageID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pageslikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("userID").toString().equals(userID) &&
					entity.getProperty("pageID").toString().equals(pageID)) {
				return true;
			}
		}
		return false;
	}
    
	public static Post getPost(long id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == id) {
				Post returnedPost = new Post(entity.getProperty(
						"ownerID").toString(),Long.parseLong(entity.getProperty(
								"pageID").toString()), entity.getProperty("text")
						.toString(), entity.getProperty("creationTime")
						.toString(), Long.parseLong(entity.getProperty(
						"numberOfLike").toString()));
				returnedPost.setprivacy(entity.getProperty("privacy")
						.toString());
				returnedPost.setHashTag(entity.getProperty("hashTag")
						.toString());
				returnedPost.setNumberOFShare(Long.parseLong(entity.getProperty("NumberOFShare")
						.toString()));
				returnedPost.setId(entity.getKey().getId());
				
				if(entity.getProperty("pageID").toString().equals("-1"))
					returnedPost.setOwnerID(User.getUserNameByID(entity.getProperty("ownerID").toString()));
				
				if(!entity.getProperty("pageID").toString().equals("-1"))
					returnedPost.setOwnerID(Page.getPageNameByID(entity.getProperty("pageID").toString()));
				
				return returnedPost;
			}
		}

		return null;
	}
    public static Vector<Post> getUserTimeLine(String ownerID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<Post> temp = new Vector<Post>();

		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("ownerID").toString().equals(ownerID)
					&& entity.getProperty("pageID").toString().equals("-1")) {
				Post p = new Post();
				p = Page.getPost(entity.getKey().getId());
				temp.add(p);
			}
		}
		return temp;
	}
    
    public static Vector<Post> getTagsPosts(String tag) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<Post> temp = new Vector<Post>();

		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("hashTag").toString().contains(tag)) {
				Post p = new Post();
				p = Page.getPost(entity.getKey().getId());
				temp.add(p);
			}
		}
		return temp;
	}
    
    public static Vector<Post> getUserHomePosts(String ownerID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<Post> temp = new Vector<Post>();

		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("ownerID").toString().equals(ownerID)
					|| isLiked(ownerID, entity.getProperty("pageID").toString())) {
				Post p = new Post();
				p = Page.getPost(entity.getKey().getId());
				temp.add(p);
			}
			else if ( !entity.getProperty("ownerID").toString().equals(ownerID) &&
					FriendShip.isFriend(ownerID, entity.getProperty("ownerID").toString())
					&& entity.getProperty("privacy").toString().equals("public")) {
				Post p = new Post();
				p = Page.getPost(entity.getKey().getId());
				temp.add(p);
			}
			
		}
		return temp;
	}
    
    public static Vector<Post> getPagePosts(String page) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<Post> temp = new Vector<Post>();

		Query gaeQuery = new Query("pagesposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("pageID").toString().equals(page)) {
				Post p = new Post();
				p = Page.getPost(entity.getKey().getId());
				temp.add(p);
			}
		}
		return temp;
	}
    public static String getPageNameByID(String id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == Long.parseLong(id)) {
				String p = entity.getProperty("pageName").toString();
				return p;
			}
		}
		return null;
	}
    public static String getPageOwnerByID(String id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == Long.parseLong(id)) {
				String p = entity.getProperty("ownerID").toString();
				return p;
			}
		}
		return null;
	}
}
