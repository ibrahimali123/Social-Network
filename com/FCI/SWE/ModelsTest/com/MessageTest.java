package com.FCI.SWE.ModelsTest.com;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.FCI.SWE.Models.Message;

public class MessageTest {

	@DataProvider(name = "getMsgByID")
	public static Object[][] getUserStringTest() {
		Object[][] x = { { "heloo ahmed ", 1 }, { "test 2", 2 },
				{ "ezaik 3aml a", 3 } };
		return x;
	}

	@Test(dataProvider = "getMsgByID")
	public void getMsgByID(boolean result, long id) {
		Assert.assertEquals(result, Message.getMsgByID(id));
	}

	@DataProvider(name = "seenMsg")
	public static Object[][] seenMsgTest() {
		Object[][] x = { { true, "1", "2" }, { false, "2", "3" },
				{ true, "2", "5" } };
		return x;
	}

	@Test(dataProvider = "seenMsg")
	public void seenMsg(boolean result, String msgID, long myID) {
		Assert.assertEquals(result, Message.seenMsg(msgID, myID));
	}

}
