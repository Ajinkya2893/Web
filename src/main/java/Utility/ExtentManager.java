package Utility;

import java.io.File;
import java.util.Date;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {

	private static ExtentReports extent;
	//Creates a report object and a file; appends the current date and time to the file name.
	public static ExtentReports getInstance(String path) {
		if (extent == null) {
			Date d=new Date();
			String fileName=d.toString().replace(":", "_").replace(" ", "_")+".html";
			extent = new ExtentReports(path+fileName, true, DisplayOrder.NEWEST_FIRST );
			extent.loadConfig(new File(System.getProperty("user.dir")+"//ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "3.11.1").addSystemInfo(
					"Environment", "QA");
		}
		return extent;
	}
}