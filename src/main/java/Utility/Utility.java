package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Utility {

	protected static WebDriver driver;
	protected Properties prop;
	protected ExtentTest test;
	protected String msg;

	public Utility(ExtentTest test,WebDriver driver, String path){
		try {
			this.test=test;
			Utility.driver=driver;
			prop = new Properties();
			FileInputStream fs = new FileInputStream(path);
			prop.load(fs);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/***************************Validation keywords*********************************/
	public String verifyText(String locatorKey,String expectedText){
		try {
			WebElement e = getElement(locatorKey);
			String actualText = e.getText();
			System.out.println(actualText +"====="+ expectedText);
			if(actualText.equals(expectedText)) {
				test.log(LogStatus.PASS, "Text are verifed ");
				return msg = "Text is verified " + Constants.PASS;
			}
			else {
				msg = "Text is not verified "; test.log(LogStatus.ERROR, msg);
				return msg + Constants.FAIL;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			msg = "Text is not verified "; test.log(LogStatus.FAIL, msg);
			return msg + Constants.FAIL;
		}
	}

	/************************Utility Functions********************************/
	public WebElement getElement(String locatorKey){
		WebElement e = null;
		try{
			if(locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		}catch(Exception ex){
			Assert.fail("Failure in Element Extraction - "+ locatorKey);
		}
		return e;
	}

	public boolean isElementPresent(String locatorKey){
		List<WebElement> e = null;

		if(locatorKey.endsWith("_id"))
			e = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElements(By.name(prop.getProperty(locatorKey)));
		if (e.size() != 0) return true;
		else return false;
	}
	/******************************Reporting functions******************************/
	public String takeScreenShot(String message){
		Date d = new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ","_")+".png";
		String path=Constants.SCREENSHOT_PATH + screenshotFile;
		try {
			if(message.endsWith(Constants.PASS)){
				File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(srcFile, new File(path));
				test.log(LogStatus.PASS, message+" "+test.addScreenCapture(path));
				msg = message;
			}else if(message.endsWith(Constants.ERROR)){
				File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(srcFile, new File(path));
				test.log(LogStatus.ERROR, message+" "+test.addScreenCapture(path));
				msg = message;
			}else if(message.endsWith(Constants.FAIL)){
				File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(srcFile, new File(path));
				test.log(LogStatus.FAIL, message+" "+test.addScreenCapture(path));
				msg = message;
			}else if(message.endsWith(Constants.INFO)){
				File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(srcFile, new File(path));
				test.log(LogStatus.INFO, message+" "+test.addScreenCapture(path));
				msg ="Successfully taken the Screenshot " + Constants.INFO;
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return msg = "Unable to take the screenshot " + Constants.FAIL;
		}
	}
	
	public String getOtpNumber() {
		String encrypted;
		String body;
		String otp = "";
		try {
			RestAssured.baseURI = "https://panel.pay1.in";
			RestAssured.basePath = "/platform/apis/";

			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,false);

			SortedMap<String,String> sortedMap = new TreeMap<String,String>();
			Map<String,String> map = new HashMap<String,String>();

			map.put("method","getOTPforTest");
			if(prop.getProperty("Live_url").contains("shop")) {
				map.put("app_name","recharge_app");
			}else if (prop.getProperty("Live_url").contains("remit")) {
				map.put("app_name","dmt");
			}
			map.put("mobile","7101000521");

			sortedMap.putAll(map);
			encrypted = new AESCrypt("Live").encrypt(om.writeValueAsString(sortedMap));

			Response response  = RestAssured.given()
					.param("req",encrypted)
					.when()
					.post()
					.then()
					.extract().response();

			System.out.println(response.asString());
			body = response.asString();

			if("failure".equals(new JsonPath(body).get("status"))) {
				System.out.println("Transaction Already Done");
			}else if("success".equals(new JsonPath(body).get("status"))) {
				otp = new JsonPath(body).get("otp");
				System.out.println(otp);
			}
			return otp;
		}
		catch (Exception e) {
			System.out.println("Error ");
			return null;
		}
	}

}
