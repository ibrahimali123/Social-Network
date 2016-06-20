package com.FCI.SWE.Controller;

import javax.servlet.http.HttpServletRequest;

public abstract class CreatePost {
	private PostCreator post;
	public abstract void create(PostCreator p , HttpServletRequest req , String current_user_id ,
			String text ,String privatee , String publice);
}
