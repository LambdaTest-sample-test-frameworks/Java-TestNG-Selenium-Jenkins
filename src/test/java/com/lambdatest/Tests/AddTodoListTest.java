package com.lambdatest.Tests;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.lambdatest.Pages.ToDo;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;

public class AddTodoListTest extends TestBase {

	@Test(dataProvider = "browsersDetails")
	public void addTodoListTest(String param)
			throws Exception {

		String[] envDeatails = param.split(",");
		Reporter.log("param" + param);
		String os = envDeatails[1];
		String version = envDeatails[2];
		String browser = envDeatails[0];
		String resValue = envDeatails[3];

		// create webdriver session
		this.setUp(browser, version, os, resValue);
		WebDriver driver = this.getWebDriver();

		// Visiting Application page
		ToDo page = ToDo.visitPage(driver);

		// Click on First Item
		page.clickOnFirstItem();

		// Click on Second Item
		page.clickOnSecondItem();

		// Add new item is list
		page.addNewItem("Yey, Let's add it to list");

		// Verify Added item
		page.verifyAddeItem();
	}
}