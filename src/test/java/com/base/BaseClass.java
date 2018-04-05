package com.base;

import java.util.Hashtable;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import keywords.Keyword;

public interface BaseClass {

	default String execute(
			Hashtable<String,String> data,
			ExtentTest test,
			Keyword app,
			Excel_Reader xls ,
			String testName,
			String path) 
	{
		String msg = null;
		try {
			if (DataUtils.isSkip(xls, testName) || data.get("RunMode").equalsIgnoreCase("N")){
				msg = "Skipping the test as runmode is N "+Constants.SKIP;
				test.log(LogStatus.SKIP, msg);
			}else {
				test.log(LogStatus.INFO, DataUtils.testDescription(xls, testName));
				app = new Keyword(test);
				test.log(LogStatus.INFO, "Executing the keywords");
				msg = app.executeKeywords(testName, xls, data, path);
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Unable to execute any test from the Interface "+Constants.FAIL;
			return msg;
		}
	}
	public void testSkip();
	public void reporter();
	public void reportTestResult();
}
