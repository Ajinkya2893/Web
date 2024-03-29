package com.remit.SenderDetails;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseClass;
import com.base.RemitSuiteRun;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import keywords.Keyword;

public class AddBeneficiaryDetails extends RemitSuiteRun implements BaseClass{

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

	private AddBeneficiaryDetails() {
		testName = "TC_AddBeneficiaryDetails";
		xls = new Excel_Reader(Constants.Remit); // Loading the Excel Sheet
		rep = ExtentManager.getInstance(Constants.REMIT_REPORT_PATH);
		objectRepoPath = Constants.RemitProperties_file_path;
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
	public void RemitAddBen(Hashtable<String,String> data) {
		try {
			count++;
			result = execute(data, test, app, xls, testName, objectRepoPath);
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
				fail = false;
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
		fail=true;
	}

	@Override@AfterTest
	public void reportTestResult() {
		if(skip)
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Skip");
		if(isTestPass)
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Pass");
		else
			DataUtils.reportFinalData(xls, "TestCases",DataUtils.getRowNum(xls,testName), "Fail");
		rep.endTest(test);
		skip=false;
		isTestPass = false;
		count=-1;
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, testName);
	}
}