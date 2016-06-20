package com.FCI.SWE.ModelsTest.com;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import com.FCI.SWE.Models.User;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class UserTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalTaskQueueTestConfig());

	@BeforeClass
	public void setUp() {
		helper.setUp();
	}

	@AfterClass
	public void tearDown() {
		helper.tearDown();
	}

	User user = new User("Ibrahim", "ibrahim@hotmail.com", "123");

	@Test
	public void getUser() {
		Assert.assertEquals("Ibrahim", user.getName());
	}
	
	/*** test get user name by id *************/
	@DataProvider(name = "getUserNameTest")
	public static Object[][] getUserStringTest() {
		return new Object[][] { { "ahmed", 1 }, { "ibrahim", 2 }, { "asd", 3 } };
	}

	@Test
	public void getUserNameByID() {
		Assert.assertEquals("ahmed", User.getUserNameByID("2"));
	}

	@Test
	public void getUserNameBy() {
		Assert.assertEquals("ahmed", User.getUserNameByID("2"));
	}

	@DataProvider(name = "getUserTest")
	public static Object[][] getUserTest() {
		Object[][] x = { { new User("ahmed", "ahmed.com", "123"), "ahmed",
				"ahmed.com" } };
		return x;
	}

	@Test
	public void getUser(User result, String userName, String passWord) {
		Assert.assertEquals(result, User.getUser(userName, passWord));
	}
}
