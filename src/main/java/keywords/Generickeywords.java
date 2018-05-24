package keywords;

import java.net.URL;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.Utility;

public class Generickeywords extends Utility{
	public static String end ="";
	WebElement element ;

	public Generickeywords(ExtentTest test, WebDriver driver, String path){
		super(test, driver, path);
	}
	/*All the generic functions related to the test should be created in this class file. 
	 * Example clickOn: this function will click on the desired
	 element mentioned/ pass in the function*/

	/******************************Opening Browser functions******************************/

	public String openBrowser(String browserType){
		test.log(LogStatus.INFO, "Starting "+browserType+ " Browser");
		try {
			if(prop.getProperty("grid").equalsIgnoreCase("Y")){
				DesiredCapabilities cap=null;
				if(browserType.equals("Mozilla")){
					cap = DesiredCapabilities.firefox();
					cap.setBrowserName("firefox");
					cap.setJavascriptEnabled(true);
					cap.setPlatform(org.openqa.selenium.Platform.LINUX);

				}else if(browserType.equalsIgnoreCase("Chrome")){
					cap = DesiredCapabilities.chrome();
					cap.setBrowserName("chrome");
					cap.setPlatform(org.openqa.selenium.Platform.LINUX);
				}
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap); 
				test.log(LogStatus.PASS, "Successfully launched GRID Server");
			} 
			else{		
				if(browserType!=null){
					if(browserType.equalsIgnoreCase("Mozilla")){
						FirefoxBinary firefoxBinary = new FirefoxBinary();
					    firefoxBinary.addCommandLineOptions("--headless");
						System.setProperty("webdriver.gecko.driver",Constants.GeckoDriver_path);
						System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
						System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
					    FirefoxOptions firefoxOptions = new FirefoxOptions();
					    firefoxOptions.setBinary(firefoxBinary);
						driver = new FirefoxDriver(firefoxOptions);
						msg = "Successfully created a instance of Mozilla ";test.log(LogStatus.INFO, msg);
					}else if(browserType.equalsIgnoreCase("Chrome")){
						System.setProperty("webdriver.chrome.driver", Constants.ChromeDriver_path);
						ChromeOptions ChromeOptions = new ChromeOptions();
						//ChromeOptions.addArguments("--headless", "window-size=1024,768", "--no-sandbox");
						driver = new ChromeDriver(ChromeOptions);
						msg = "Successfully created a instance of chrome ";test.log(LogStatus.INFO, msg);
					}else if(browserType.equalsIgnoreCase("ie")){
						System.setProperty("webdriver.ie.driver", "F:\\drivers\\IEDriverServer.exe");
						driver =  new InternetExplorerDriver();
						msg= "Successfully created a instance of IE ";test.log(LogStatus.INFO, msg);
					}
				}else{
					System.out.println("browserType is not mentioned in the excelsheet or properties"); 
					test.log(LogStatus.ERROR, "browserType is not mentioned in the excelsheet or properties");}
			}
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			return msg + Constants.PASS;
		}catch (Exception e) {
			e.printStackTrace();
			msg ="Unable to launch browser";
			return msg + Constants.FAIL;
		}
	}

	public String closeBrowser() {
		try {
			test.log(LogStatus.INFO, "Closing the Browser");
			driver.quit();
			System.out.println("Closing the browser");
			return Constants.PASS;
		}catch(Exception e) {
			e.printStackTrace();
			takeScreenShot("Unable to close the browser");
			return Constants.FAIL;
		}
	}

	public String navigateTo(String url){
		try {

			if(url!=null){
				driver.get(prop.getProperty("Live_url"));
				msg = "Successfully navigated to "+url+" ";
				test.log(LogStatus.PASS, msg);
				return msg + Constants.PASS;
			}
			else {
				msg = "Unable to navigate to Navigate to " + url +" ";
				test.log(LogStatus.ERROR, msg);
				takeScreenShot(msg);
				return msg + Constants.FAIL;
			}
		}catch(Exception e) {
			e.printStackTrace ();
			msg = "Caught in Catch" + url;
			test.log(LogStatus.ERROR, msg);
			takeScreenShot(msg);
			return msg + Constants.FAIL;
		}
	}

	/******************************Working on Browser functions ******************************/

	public String clickOn(String objectToBeClicked){
		try{
			if(!(objectToBeClicked==null)){
				if(isElementPresent(objectToBeClicked)){
					getElement(objectToBeClicked).click();
					msg = "Clicked on "+ objectToBeClicked +" ";
					test.log(LogStatus.PASS, msg);
					return msg + Constants.PASS;
				}else{
					msg = "Element not present to click"+ objectToBeClicked +" ";
					test.log(LogStatus.WARNING, msg);
					return msg+Constants.WARNING;
				}
			}else{
				msg="No such object is present to click "+objectToBeClicked +" ";
				test.log(LogStatus.ERROR, msg);
				takeScreenShot(msg);
				return msg + Constants.FAIL;
			}	
		}catch(Exception e) {
			msg="No such object is present to click "+objectToBeClicked +" ";
			test.log(LogStatus.ERROR, msg);
			takeScreenShot(msg);
			return msg + Constants.FAIL;
		}
	}

	public String typeOn(String objectToClick, String value){
		try{
			test.log(LogStatus.INFO, "Entering values in the text Field");
			if(isElementPresent(objectToClick)) {
				getElement(objectToClick).click();getElement(objectToClick).sendKeys(value);
				msg = "Entered Value in the Field ";
				test.log(LogStatus.PASS, msg + value);
				return msg + Constants.PASS;
			}else {
				msg = "Element does not exists on the page ";
				test.log(LogStatus.ERROR, msg + value);
				takeScreenShot(msg);
				return msg + Constants.ERROR;
			}
		}
		catch (Exception e){
			e.printStackTrace();
			msg = "Error While entering the Values ";  
			test.log(LogStatus.ERROR, msg);
			return msg + Constants.ERROR;
		}
	}

	public String verifyTitle(Hashtable<String, String>testdata){
		try{
			test.log(LogStatus.INFO, "Verifying the page tittle");
			if((driver.getTitle()).equals(testdata.get("LandingPage_Tittle"))){
				msg = "Tittle matched landed on the Expected Page ";
				test.log(LogStatus.PASS, msg);
				return msg + Constants.PASS;
			}
			else {
				msg = "Tittle does not matched ";
				test.log(LogStatus.ERROR, msg);
				takeScreenShot(msg);
				return msg + Constants.ERROR;
			}
		}
		catch (Exception e) { 
			msg ="Issue while fetching the page tittle ";
			takeScreenShot(msg); test.log(LogStatus.FAIL, msg);
			return msg + Constants.FAIL;
		}
	}

	public String clickOnCheckBox(String objecToClick){
		try {
			if(objecToClick!=null){
				element= getElement(objecToClick);
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				test.log(LogStatus.PASS, "Clicked on ELement "+objecToClick + " ");
				executor.executeScript("arguments[0].click();", element);
				return Constants.PASS;
			}else{
				test.log(LogStatus.ERROR, "Unable to clicked on "+objecToClick +" ");
				return Constants.ERROR;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			msg = "Unable to click on checkBox ";
			test.log(LogStatus.FAIL, msg);
			return msg + Constants.FAIL;
		}
	}

	public String getText(String objecToFetch) {
		try{
			if(objecToFetch!=null) {
				element= getElement(objecToFetch);
				String text = element.getText();
				if(text.equals("")) {
					System.out.println(text);
					msg = "No Value Fetched " + Constants.ERROR;
					test.log(LogStatus.ERROR, msg + text);
				}else {
					System.out.println(text);
					msg = "Successfully Fetched the value " + Constants.PASS;
					test.log(LogStatus.PASS, msg + text);

				}
				return msg;
			}else{
				msg = "Unable to fetch the object ";
				test.log(LogStatus.FAIL, msg);
				return msg + Constants.FAIL;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			msg = "Error While fecthing the values ";
			test.log(LogStatus.FAIL, msg);
			return msg + Constants.FAIL;
		}
	}
}
