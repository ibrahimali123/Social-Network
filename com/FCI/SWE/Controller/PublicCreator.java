package com.FCI.SWE.Controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PublicCreator extends PostCreator{

	
	public String create(HttpServletRequest req , String current_user_id ,
			String text , String privatee , String publice) {
		 
		current_user_id = (String) req.getSession().getAttribute("current_user_id"); 
		String serviceUrl = "http://localhost:8888/rest/CreatePostService";
		try { 
			String urlParameters = "text=" + text + "&current_user_id=" + current_user_id + "&privatee=" + privatee
					+"&publice=" + publice; 

			String retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
		}  catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}