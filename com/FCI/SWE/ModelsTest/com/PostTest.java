package com.FCI.SWE.ModelsTest.com;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.FCI.SWE.Models.Post;

public class PostTest {

	@Test(dataProvider = "isLikePostTest")
	public void CreatePostTest(long result, String postId) {
		Post p = new Post("", 0, "post test", "", 0);
		p.CreatePost("post test", "0", "public");
		Assert.assertEquals(33, p.getId());
	}

	@DataProvider(name = "isLikePostTest")
	public static Object[][] isLikedTest() {
		Object[][] x = { { true, "1", "2" }, { false, "2", "2" } };
		return x;
	}

	@Test(dataProvider = "isLikePostTest")
	public void isLikePost(boolean result, String userId, String postId) {
		Assert.assertEquals(result, Post.isLikePost(userId, postId));
	}

}
