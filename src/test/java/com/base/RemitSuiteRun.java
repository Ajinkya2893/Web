package com.base;

import java.io.File;

import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import Utility.Constants;

public class RemitSuiteRun {

	public ExtentReports rep;
	public ExtentTest test;

	@BeforeSuite
	public void beforeSuite() {
		File myFile = new File(Constants.SrcPath);
		if (!myFile.isDirectory()) myFile.delete();
		File Zipfolder = new File(Constants.DesPath);
		if (!Zipfolder.isDirectory()) Zipfolder.delete();
	}
	
	/*@AfterSuite
	public void afterSuite() {
		try {
			rep.flush();
			ZipUtil.pack(new File(Constants.SrcPath), new File(Constants.DesPath));
			new SendMail().sendMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
