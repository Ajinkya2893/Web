package com.Shop.Complaint;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseClass;
import com.base.ShopSuiteRun;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import keywords.Keyword;

public class DthReport extends ShopSuiteRun implements BaseClass{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String result;
	public String msg ;
	protected Keyword app;
	protected Excel_Reader xls ;
	public String testName;
	protected String objectRepoPath ;

	private DthReport() {
		testName = "TC_DthReport";
		xls = new Excel_Reader(Constants.SHOP); // Loading the Excel Sheet
		rep = ExtentManager.getInstance(Constants.SHOP_REPORT_PATH);
		objectRepoPath = Constants.ShopProperties_file_path;
	}

	@Override@BeforeTest
	public void testSkip() {
		test  = rep.startTest(testName);
		if (DataUtils.isSkip(xls, testName)){
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Skip");
			test.log(LogStatus.SKIP, "Skipping the test as runmode is NO in the Excel Sheet");
			rep.endTest(test);
			rep.flush();
			skip = true;
			throw new SkipException("Skipping test case" + testName + " as runmode set to NO in excel");
		} 
	}

	@Test(dataProvider="getData")
	public void RemitTransMoney(Hashtable<String,String> data) {
		try {
			result = execute(data, test, app, xls, testName, objectRepoPath);
			count++;
			if(result.endsWith(Constants.PASS)){
				msg="Test got Successfully Passed";
				fail=false;
			}else if(result.endsWith(Constants.ERROR)){
				msg="Some Error got caused";
				fail=true;
			}else if(result.endsWith(Constants.FAIL)) {
				fail=true;
				msg="Failed to excute the test";
			}else if(result.endsWith(Constants.SKIP)) {
				skip=true;
				msg="Test Skipped as the Runmode is N in Data Sheet";
			}
		}catch(Exception e){
			e.printStackTrace();
			fail = true;
			msg = "Unable to run the test";
		}
	}

	@Override@AfterMethod
	public void reporter() {
		if(fail){
			DataUtils.reportDataSetResult(xls, testName, (DataUtils.getRnum(xls,testName))+count, "Fail");
			DataUtils.reportDataSetActualResult(xls, testName, DataUtils.getRnum(xls,testName)+count, msg);
		}else{
			isTestPass=true;
			DataUtils.reportDataSetResult(xls, testName, (DataUtils.getRnum(xls,testName))+count, "Pass");
			DataUtils.reportDataSetActualResult(xls, testName, (DataUtils.getRnum(xls,testName))+count, msg);
		}
		if(skip)
			DataUtils.reportDataSetResult(xls, testName, DataUtils.getRnum(xls,testName)+count, "Skip");
		skip=false;
		fail=true;
	}

	@Override@AfterTest
	public void reportTestResult() {
		if(skip)
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Fail");
		if(isTestPass)
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Pass");
		else
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Skip");
		rep.endTest(test);
		isTestPass = false;
		count=-1;
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, testName);
	}
}