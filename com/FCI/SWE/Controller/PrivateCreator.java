package com.FCI.SWE.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PrivateCreator extends PostCreator {

	@Override
	public String create(HttpServletRequest req, String current_user_id,
			String text, String privatee, String publice) {

		// TODO Auto-generated method stub

		current_user_id = (String) req.getSession().getAttribute(
				"current_user_id");
		String serviceUrl = "http://localhost:8888/rest/CreatePostService";
		try {
			String urlParameters = "text=" + text + "&current_user_id="
					+ current_user_id + "&privatee=" + privatee + "&publice="
					+ publice;

			String retJson = Connection.connect(serviceUrl, urlParameters,
					"POST", "application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return "Error , try again";
			if (object.get("Status").equals("OK"))
				return "Post Created Successfully";
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
