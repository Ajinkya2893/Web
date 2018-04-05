package Utility;

public class Constants {

	//Driver paths 
	public static final String ChromeDriver_path = System.getProperty("user.dir")+"//Drivers//ChromeDriver//chromedriver";
	public static final String GeckoDriver_path = System.getProperty("user.dir")+"/Drivers/GeckoDriver/geckodriver";
	public static final String InternetExplorer_Driver_path ="";
	
	//Properties file path
	public static final String RemitProperties_file_path = System.getProperty("user.dir")+"//src//test//resources//objectRepoRemit//remit.properties";
	public static final String ShopProperties_file_path = System.getProperty("user.dir")+"//src//test//resources//objectRepoShop//shop.properties";
	
	//ExcelSheets paths 
	public static final String Remit = System.getProperty("user.dir")+"//ExcelSheets//REMIT//TC_Remit.xlsx";
	public static final String SHOP = System.getProperty("user.dir")+"//ExcelSheets//SHOP//Shop.xlsx";
	public static final String SCREENSHOT_PATH = System.getProperty("user.dir")+"//Reports//Screenshots//";
	public static final String SHOP_REPORT_PATH = System.getProperty("user.dir")+"//Reports//SHOP//";
	public static final String REMIT_REPORT_PATH = System.getProperty("user.dir")+"//Reports//REMIT//";
	public static final String SrcPath = System.getProperty("user.dir")+"/Reports/";
	public static final String DesPath = System.getProperty("user.dir")+"/ZipFolder/Pay1Web.zip";
	
	//All the Excel Related Values
	public static final String KEYWORDS_SHEET = "Keywords";
	public static final String TESTCASES_SHEET = "TestCases";
	public static final String DATA_SHEET = "Data";
	
	public static final String TCID_COL = "TCID";
	public static final String KEYWORD_COL = "Keywords";
	public static final String OBJECT_COL = "Object";
	public static final String DATA_COL = "Data";
	public static final String RESULTS_COL = "Result";
	public static final String ACTUALRESULTS_COL = "Actual Result";

	public static final String RUNMODE_COL = "RunMode";
	public static final String DESCRIPTION_COL = "Description";
	
	
	//Logs Status
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	public static final String WARNING = "WARNING";
	public static final String ERROR = "ERROR";
	public static final String SKIP = "SKIP";
	public static final String INFO = "INFO";
	
}
