package keywords;

import java.util.Hashtable;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;

public class Appkeywords extends Generickeywords {

	public boolean gotLogin = true;
	public boolean passFlag = true;
	public boolean transferFlag = true;

	public String beforeRetailerBalance;
	public String beforeUserBalance;
	public String afterRetailerBalance;
	public String afterUserBalance;

	/*All the application related keywords functions has to be created in this class file. Example getLogin: this will help the user
	to get logged in the application */

	public Appkeywords(ExtentTest test, String path) {
		super(test, driver, path);
	}

	public String Login(String username, String Password){
		try {
			//takeScreenShot("Aji");
			Thread.sleep(2000);
			if(getElement("loginPanel_xpath").isDisplayed()) {
				getElement("username_id").sendKeys(username);test.log(LogStatus.INFO, "Entering Username "+username);
				getElement("password_id").sendKeys(Password); test.log(LogStatus.INFO, "Entering Password "+Password);
				getElement("loginButton_xpath").click(); test.log(LogStatus.INFO, "Clicking on Login button");
				Thread.sleep(5000);
				msg = "Successfully entered Login details ";

				if(gotLogin) //checking the otp screen is displayed or not
					msg = Loginotp(username);

				if(passFlag) //checking the 3months change password is asked or not
					//msg = changePin(Password);
					/**
					 * Remit change password is not introduce so skipping
					 * */
					takeScreenShot(msg);

			}else if (getElement("loginButtonheader_xpath").isDisplayed()) { //if the Login Module pop up is not displayed
				getElement("loginButtonheader_xpath").click();
				Thread.sleep(2000);
				Login(username, Password);
			}
		}
		catch (Exception e){
			e.printStackTrace();
			msg = "Unable to enter Login details ";
			test.log(LogStatus.ERROR, msg);
			takeScreenShot(msg + Constants.ERROR);
		}

		return msg;
	}

	public String getLogin(Hashtable<String, String>data){
		//test.log(LogStatus.INFO, "Entering the defaults Login details");
		return Login(data.get("Username"), data.get("Password"));
	}

	public String Loginotp(String number) {
		try {
			if(getElement("otp_Panel_xpath").isDisplayed()){
				//getElement("otp_id").sendKeys(getOtpNumber(number));  test.log(LogStatus.INFO, "Entered the Value");
				getElement("otp_id").sendKeys("123654");  test.log(LogStatus.INFO, "Entered the Value");
				getElement("otpButton_xpath").click(); test.log(LogStatus.PASS, "Successfully entered the OTP");
				gotLogin = false;
				msg = "Entered the Values in the Field ";
				return msg + Constants.PASS;
			}
			msg ="NO Otp Panel Displayed ";
			return msg + Constants.PASS;
		}catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("NO OTP PANEL ");
			msg ="Unable to get the OTP panel ";
			return msg + Constants.FAIL;
		}
	}

	public String AddSenderOTP(String OtpNumber, Hashtable<String, String>data) {
		try {
			if(getElement("addSender_Name_id").isDisplayed()) {
				//getElement("addSender_Name_id").sendKeys(data.get("SenderName"));
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
			else if(AlertOption.equalsIgnoreCase("Ok")) {
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

	public String checkTransactionStatus(String type, Hashtable<String, String> data) {
		try {
			if(type.equalsIgnoreCase("PrepaidRecharge")) {
				getElement("mobileReport_xpath").click();test.log(LogStatus.INFO, "Prepaid Mobile Recharge Verification");
				if((getElement("prepmobileNumber_xpath").getText()).equals(data.get("Mobile")))
					test.log(LogStatus.PASS, "Mobile Number intact");
				if((getElement("prepamount_xpath")).getText().contains(data.get("Amount")))
					test.log(LogStatus.PASS, "Amount intact");

			}else if(type.equalsIgnoreCase("PostRecharge")) {
				getElement("postpaidReport_xpath").click();test.log(LogStatus.INFO, "PostPaid Mobile Recharge Verification");
				getElement("mobSearch_id").click();
				if((getElement("postmobileNumber_xpath").getText()).equals(data.get("Mobile")))
					test.log(LogStatus.PASS, "Mobile Number intact");
				if((getElement("postamount_xpath")).getText().contains(data.get("Amount")))
					test.log(LogStatus.PASS, "Amount intact");

			}else if(type.equalsIgnoreCase("DthRecharge")) {
				getElement("dthReport_xpath").click();test.log(LogStatus.INFO, "DTH Recharge Verification");
				getElement("dthSearch_id").click();
				if((getElement("dthnureport_xpath").getText()).equals(data.get("Mobile")))
					test.log(LogStatus.PASS, "Mobile Number intact");
				if((getElement("dthamureport_xpath")).getText().contains(data.get("Amount")))
					test.log(LogStatus.PASS, "Amount intact");
			}
			return Constants.PASS;
		}
		catch(Exception e){
			e.printStackTrace();
			return Constants.FAIL;
		}
	}

	public String changePin(String Pin) {
		try {
			if(getElement("passwordModule_xpath").isDisplayed()) {
				getElement("existingPin_id").sendKeys("Pin"); test.log(LogStatus.INFO, "Entered the Old Pin");
				getElement("enterPin_id").sendKeys("12345"); test.log(LogStatus.INFO, "Entered the New Pin");
				getElement("confirmPin_id").sendKeys("12345"); test.log(LogStatus.INFO, "Entered the Confirm Pin");
				getElement("updatePinButton_xpath").click(); test.log(LogStatus.INFO, "Click on the Update Pin Button");
				passFlag = false;

				msg = "Entered the Old Password and Updated the new Password ";
				return msg + Constants.PASS;
			}
			msg = "Password Module Pop up is not displayed";
			return msg + Constants.PASS;

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Something went wrong finding the password ";
			return Constants.FAIL;
		}
	}

	public String verifyBalanceDeduct(Hashtable<String, String> data) {
		try {
			if(data.get("Mode")!=null) {
				if(transferFlag) {
					beforeRetailerBalance = getElement("Userbal_xpath").getText();
					beforeUserBalance = (getElement("senderBalance_xpath").getText()).replaceAll("[^0-9]", "");


					System.out.println(Double.parseDouble(beforeRetailerBalance));
					//	System.out.println(beforeRetailerBalance.replaceAl("[^0-9]", ""));
					System.out.println(beforeUserBalance.replaceAll("[^0-9]", ""));

					test.log(LogStatus.INFO, "Selecting "+data.get("Mode")+" as the transfer mode");

					if(data.get("Mode").equalsIgnoreCase("IMPS")) 
						getElement("impsradiobutton_xpath").click();  //Clicking on IMPS 
					else if (data.get("Mode").equalsIgnoreCase("NEFT"))
						getElement("neftradiobutton_xpath").click(); //Clicking on NEFT
					transferFlag = false;
					//Setting the flag as false for the second round
					
					msg = "Successfully selected the mode ";
					return msg + Constants.PASS;
				}else {
					afterRetailerBalance = getElement("Userbal_xpath").getText();
					afterUserBalance = (getElement("senderBalance_xpath").getText().replaceAll("[^0-9]", ""));

					System.out.println(Double.parseDouble(afterRetailerBalance));
					System.out.println(afterUserBalance.replaceAll("[^0-9]", ""));

					double RB = Double.parseDouble(beforeRetailerBalance) - Double.parseDouble(afterRetailerBalance);
					int UB = Integer.parseInt(beforeUserBalance) - Integer.parseInt(afterUserBalance);

					System.out.println(RB);

					Assert.assertEquals(Integer.toString(UB), data.get("Amount"));
					test.log(LogStatus.PASS, "Successfully verify the amount sent");
					System.out.println(UB);

					//Assert.assertArrayEquals(Double.parseDouble(beforeRetailerBalance),(Double.parseDouble(afterRetailerBalance))-(Double.parseDouble(data.get("Amount"))), delta);
					transferFlag = true;
					msg = "Successfully verified the sent amount ";
					return msg + Constants.PASS;
				}
			}else {
				msg = "Mode set in the excel is not correct ";
				test.log(LogStatus.FAIL, msg);
				transferFlag = false;
				return msg +Constants.FAIL;
			}
		} catch (NumberFormatException e) {
			transferFlag = false;
			Assert.fail("Unable to verify the amount transfer");
			e.printStackTrace(); 
			return msg +Constants.FAIL;
		}
	}

	public String confirmTransaction() {
		try {
			if(getElement("transferConfirmPanel_xpath").isDisplayed()) {
				getElement("confirmBtn_xpath").click();
				Thread.sleep(5000);
				driver.switchTo().alert().accept();
				Thread.sleep(5000);
				takeScreenShot("Confirmation ");
			}
			return Constants.PASS;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.FAIL;
		}
	}

	/*public String getOtpNumber(String env, String app) {
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
	}*/
}