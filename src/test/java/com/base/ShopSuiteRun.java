package com.base;

import org.testng.annotations.AfterSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ShopSuiteRun {
	
	public ExtentReports rep;
	public ExtentTest test;

/*	@BeforeSuite
	public void beforeSuite() {
		File zip = new File(Constants.DesPath);
		if (!zip.isDirectory()) zip.delete();
		File report = new File(Constants.SrcPath);
		if (!report.isDirectory()) report.delete();
	}*/
	
	@AfterSuite
	public void afterSuite() {
		try {
			rep.flush();
			/*ZipUtil.pack(new File(Constants.SrcPath), new File(Constants.DesPath));
			new SendMail().sendMail();
	*/	} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
