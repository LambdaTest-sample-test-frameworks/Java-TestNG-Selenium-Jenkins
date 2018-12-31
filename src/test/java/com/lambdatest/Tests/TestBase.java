package com.lambdatest.Tests;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TestBase {

	public String buildTag = System.getenv("LT_BUILD");
	public String username = System.getenv("LT_USERNAME");
	public String accesskey = System.getenv("LT_APIKEY");
	public  String gridURL = System.getenv("LT_GRID_URL");

	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

	@DataProvider(name = "browsersDetails", parallel = true)
	public static Iterator<Object[]> ltBrowserDataProvider(Method testMethod) {
		String jsonText = System.getenv("LT_BROWSERS");

		ArrayList<Object> lt_browser = new ArrayList<Object>();
		ArrayList<Object> lt_operating_system = new ArrayList<Object>();
		ArrayList<Object> lt_browserVersion = new ArrayList<Object>();
		ArrayList<Object> lt_resolution = new ArrayList<Object>();

		
		JSONArray allCities = new JSONArray(jsonText);
        for (int j = 0; j < allCities.length(); j ++){
            JSONObject cityObject = allCities.getJSONObject(j);
            
            if (! cityObject.getString("browserName").isEmpty()) {
            	lt_browser.add(cityObject.getString("browserName"));
            }
            
            if (! cityObject.getString("operatingSystem").isEmpty()) {
            	lt_operating_system.add(cityObject.getString("operatingSystem"));
            }
            
            if (! cityObject.getString("browserVersion").isEmpty()) {
            	lt_browserVersion.add(cityObject.getString("browserVersion"));
            }
            
            if (! cityObject.getString("resolution").isEmpty()) {
            	lt_resolution.add(cityObject.getString("resolution"));
            }
        }
        Object[][] arrMulti = new Object[lt_browser.size()][1];
        
        for (int l =0 ; l<lt_browser.size();l++) {
        	
            	arrMulti[l][0]=lt_browser.get(l)+","+lt_operating_system.get(l)+","+lt_browserVersion.get(l)+","+lt_resolution.get(l);
            	
        }
      
        List<Object[]> capabilitiesData = new ArrayList<Object[]>();
        
       // Object[][] data = new Object[arrMulti.length][1];
        
        for (int i = 0; i < arrMulti.length; i++) {
			for (int j = 0; j < 1; j++) {
				
				capabilitiesData.add(new Object[] { arrMulti[i][j] });
				
			}
        }
        return capabilitiesData.iterator();
        
	}

	public WebDriver getWebDriver() {
		return webDriver.get();
	}

	protected void setUp(String browser, String version, String os, String res )
			throws MalformedURLException, Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// set desired capabilities to launch appropriate browser on Lambdatest
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, version);
		capabilities.setCapability(CapabilityType.PLATFORM, os);
		capabilities.setCapability("screen_resolution", res);
		
		
		Reporter.log("os"+os);
		Reporter.log("version"+version);
		Reporter.log("browser"+browser);
		Reporter.log("resValue"+res);

		if (buildTag == null) {
			capabilities.setCapability("build", "TestNG-Java-Jenkins-Parallel");
		}


		gridURL = System.getenv("LT_GRID_URL");

		// Launch remote browser and set it as the current thread
		webDriver.set(new RemoteWebDriver(new URL(gridURL), capabilities));

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		// ((JavascriptExecutor) webDriver.get()).executeScript("Lambdatest:job-result="
		// + (result.isSuccess() ? "passed" : "failed"));
		if (!(webDriver.get()==null))
			webDriver.get().quit();
	}
}
