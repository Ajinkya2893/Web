package keywords;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.testng.SkipException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.AESCrypt;
import Utility.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Appkeywords extends Generickeywords {

	public boolean gotLogin = false;
	/*All the application related keywords functions has to be created in this class file. Example getLogin: this will help the user
	to get logged in the application */

	public Appkeywords(ExtentTest test, String path) {
		super(test, driver, path);
	}

	public String Login(String username, String Password){
		try {
			takeScreenShot("Aji");
			if(getElement("loginPanel_xpath").isDisplayed()) {
				getElement("username_id").sendKeys(username);test.log(LogStatus.INFO, "Entering Username "+username);
				getElement("password_id").sendKeys(Password); test.log(LogStatus.INFO, "Entering Password "+Password);
				getElement("loginButton_xpath").click(); test.log(LogStatus.INFO, "Clicking on Login button");
				Thread.sleep(5000);
				msg = "Successfully entered Login details " + Constants.PASS; test.log(LogStatus.PASS, msg);
				gotLogin = true;
				if(gotLogin) 
					msg = Loginotp();
				takeScreenShot(msg + Constants.PASS);
			}else if (getElement("loginButtonheader_xpath").isDisplayed()) {
				getElement("loginButtonheader_xpath").click();
				Thread.sleep(2000);
				Login(username, Password);
			}
			if(getElement("checkLogin_xpath").getText().equalsIgnoreCase("Login"))
				throw new SkipException("Logging Failed");

			return msg;
		}
		catch (Exception e){
			e.printStackTrace();
			msg = "Unable to enter Login details ";
			test.log(LogStatus.ERROR, msg);
			takeScreenShot(msg + Constants.ERROR);
			return msg+Constants.FAIL;
		}
	}

	public String getLogin(Hashtable<String, String>data){
		test.log(LogStatus.INFO, "Entering the defaults Login details");
		return Login(data.get("Username"), data.get("Password"));
	}

	public String Loginotp() {
		try {
			if(getElement("otp_Panel_xpath").isDisplayed()){
				getElement("otp_id").sendKeys(getOtpNumber());  test.log(LogStatus.INFO, "Entered the Value");
				getElement("otpButton_xpath").click(); test.log(LogStatus.PASS, "Successfully entered the OTP");
				gotLogin = false;
				msg = "Entered the Values in the Field ";
				return msg + Constants.PASS;
			}
			msg ="NO Otp Panel Displayed ";test.log(LogStatus.INFO, msg);
			return msg + Constants.PASS;
		}catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("NO OTP PANEL ");
			msg ="Unable to get the Otp panel ";
			return msg + Constants.FAIL;
		}
	}

	public String AddSenderOTP(String OtpNumber, Hashtable<String, String>data) {
		try {
			if(getElement("addSender_Name_id").isDisplayed()) {
				getElement("addSender_Name_id").sendKeys(data.get("SenderName"));
				@SuppressWarnings("resource")
				Scanner reader = new Scanner(System.in);  // Reading from System.in
				System.out.println("Enter a number: ");
				int n = reader.nextInt(); // Scans the next token of the input as an int.
				//once finished
				reader.reset();
				getElement(OtpNumber).sendKeys(Integer.toString(n)); test.log(LogStatus.INFO, "Entered the Value");
				getElement("addSenderConfirmBtn_xpath").click();
				msg = "Entered the Values in the Field " + Constants.PASS;
			}else if(getElement(OtpNumber).isDisplayed()) {
				@SuppressWarnings("resource")
				Scanner reader = new Scanner(System.in);  // Reading from System.in
				System.out.println("Enter a number: ");
				int n = reader.nextInt(); // Scans the next token of the input as an int.
				//once finished
				reader.reset();
				getElement(OtpNumber).sendKeys(Integer.toString(n)); test.log(LogStatus.INFO, "Entered the Value");
				getElement("addSenderConfirmBtn_xpath").click();
				msg = "Entered the Values in the Field "+ Constants.PASS;
			}
			msg ="NO Otp Panel Displayed ";test.log(LogStatus.INFO, msg);
			return msg ;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("NO OTP PANEL ");
			return Constants.FAIL;
		}
	}

	public String wait(String timeToWait) {
		try {
			Thread.sleep(Integer.parseInt(timeToWait));
			msg = "Waited for 20 sec ";
			return msg + Constants.PASS;
		}catch (Exception e) {
			e.printStackTrace();
			msg = "Unable to wait ";
			return msg + Constants.FAIL;
		}
	}

	public String SwitchAlert(String AlertOption) {
		try {
			if(AlertOption.equals("Accept")) {
				driver.switchTo().alert().accept();
				msg = "Accepting the Alert Option ";test.log(LogStatus.INFO, msg);
			}
			else if(AlertOption.equals("Dismiss")) {
				driver.switchTo().alert().dismiss();
				msg = "DIsmissing the Alert Option ";test.log(LogStatus.INFO, msg);
			}
			else if(AlertOption.equals("Ok")) {
				driver.switchTo().alert().accept();
				msg = "DIsmissing the Alert Option ";test.log(LogStatus.INFO, msg);
			}
			else if(AlertOption.equals("Cancel")) {
				driver.switchTo().alert().dismiss();
				msg = "DIsmissing the Alert Option ";test.log(LogStatus.INFO, msg);
			}
			System.out.println("Already added");
			return msg+Constants.PASS;
		}catch (Exception e) {
			e.getStackTrace();
			msg ="Unable to access Alert ";
			return msg+Constants.ERROR;
		}
	}

	public String getAlertText() {
		try {
			msg = driver.switchTo().alert().getText();
			test.log(LogStatus.INFO, msg);
			System.out.println(msg);
			msg = "Fetched the alert text ";
			return msg+Constants.PASS;
		}catch (Exception e) {
			e.getStackTrace();
			msg ="Unable to access Alert ";
			return msg+Constants.ERROR;
		}
	}

	public String getMobileTransactionDetails(String locatorKey, Hashtable<String, String> data) {
		try {
			if((driver.findElement(By.xpath("//*[@id='report_mob_transactions_table_body']/tr[1]/td[3]")).getText()).equals(data.get("Mobile")))
				System.out.println("Number Matched");
			if((driver.findElement(By.xpath("//*[@id='report_mob_transactions_table_body']/tr[1]/td[4]"))).getText().contains(data.get("Amount")))
				System.out.println("Amount Matched");

			return Constants.PASS;
		}catch(Exception e){
			e.printStackTrace();
			return Constants.FAIL;
		}
	}

	public String getOtpNumber(String env, String app) {
		String encrypted;
		String body;
		String otp = "";
		try {

			RestAssured.baseURI = "https://paneluat.pay1.in";
			RestAssured.basePath = "/platform/apis/";

			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,false);

			SortedMap<String,String> sortedMap = new TreeMap<String,String>();
			Map<String,String> map = new HashMap<String,String>();

			map.put("method","getOTPforTest");
			if(app.equalsIgnoreCase("shop")) {
				map.put("app_name","recharge_app");
			}else if (app.equalsIgnoreCase("remit")) {
				map.put("app_name","dmt");
			}
			map.put("mobile","7101000521");

			sortedMap.putAll(map);
			System.out.println();
			encrypted = new AESCrypt(env).encrypt(om.writeValueAsString(sortedMap));

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