package com.FCI.SWE.ModelsTest.com;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.FCI.SWE.Models.Page;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class PageTest {

	@DataProvider(name = "getPageNameByID")
	public static Object[][] getPageNameByIDTest() {
		Object[][] x = { { "elahly", 1 }, { "hossam8aly", 2 },
				{ "bank el qahera", 3 } };
		return x;
	}

	@Test(dataProvider = "getPageNameByID")
	public void getPageNameByID(String result, long id) {
		Assert.assertEquals(result, Page.getPageNameByID(String.valueOf(id)));
	}

	@DataProvider(name = "getPageOwnerByID")
	public static Object[][] getPageOwnerByIDTest() {
		Object[][] x = { { "ahmed", 1 }, { "ibrahim", 2 }, { "asd", 3 } };
		return x;
	}

	@Test(dataProvider = "getPageOwnerByID")
	public void getPageOwnerByID(String result, long id) {
		Assert.assertEquals(result, Page.getPageOwnerByID(String.valueOf(id)));
	}

	@DataProvider(name = "isLiked")
	public static Object[][] isLikedTest() {
		Object[][] x = { { true, "1", "2" }, { false, "2", "2" } };
		return x;
	}

	@Test(dataProvider = "isLiked")
	public void isLiked(boolean Result, String userId, String pageId) {
		Assert.assertEquals(Result, Page.isLiked(userId, pageId));
	}

}
