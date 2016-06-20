package com.FCI.SWE.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

public abstract class PostCreator {
	public abstract String create(HttpServletRequest req , String current_user_id ,
			String text , String privatee , String publice);
}
