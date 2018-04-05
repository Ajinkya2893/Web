package keywords;

import static org.testng.Assert.assertThrows;

import java.util.Hashtable;

import com.relevantcodes.extentreports.ExtentTest;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;

public class Keyword {

	ExtentTest test;
	Appkeywords app;
	public String msg;

	public Keyword(ExtentTest test) {
		this.test=test;
	}

	/*Execute keyword function runs in a loop and iterate through each column from the excel sheet*/
	public String executeKeywords(
			String testUnderExecution,
			Excel_Reader xls,
			Hashtable<String,String> testData,
			String path) throws InterruptedException{


		app = new Appkeywords(test, path);

		int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
		String result="";
		String testResult="";
		for(int rNum=2;rNum<=rows;rNum++){
			String tcid  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testUnderExecution)){
				String keyword  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				String object  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COL, rNum);
				String key  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COL, rNum);
				String data = testData.get(key);
				if(keyword.equals("openBrowser"))
					result=app.openBrowser(data);
				else if(keyword.equals("navigateTo"))
					result=app.navigateTo(object);
				else if(keyword.equals("click"))
					result=app.clickOn(object);
				else if(keyword.equals("type"))
					result = app.typeOn(object,data);
				else if(keyword.equals("VerifyTitle"))
					result = app.verifyTitle(testData);
				else if(keyword.equals("wait"))
					result = app.wait(object);
				else if(keyword.equals("getLogin"))
					result = app.getLogin(testData);
				else if(keyword.equals("closeBrowser"))
					result = app.closeBrowser();
				else if(keyword.equals("verifyText"))
					result = app.verifyText(object, data);
				else if(keyword.equals("checkboxClick"))
					result = app.clickOnCheckBox(object);
				else if(keyword.equals("takeScreenShot"))
					result = app.takeScreenShot(object);
				else if(keyword.equals("Otp"))
					result = app.AddSenderOTP(object, testData);
				else if(keyword.equals("loginOtp"))
					result = app.Loginotp();
				else if(keyword.equals("GetText"))
					result = app.getText(object);
				else if(keyword.equals("AcceptAlert"))
					result = app.SwitchAlert(object);
				else if(keyword.equals("getAlertText"))
					result = app.getAlertText();
				else if(keyword.equals("mobileTransactionDetails"))
					result = app.getMobileTransactionDetails(object, testData);

				if(result != null) {
					testResult = resultKeyword(xls, result, rNum);
				}
			}
		}
		return testResult;
	}

	public Appkeywords getGenericKeyWords(){
		return app;
	}

	public void expectedResults(String testUnderExecution,
			Excel_Reader xls,
			Hashtable<String,String> testData){
	}

	public String resultKeyword(
			Excel_Reader xls,
			String result,
			int rNum) {
		try {
			if(result.endsWith(Constants.PASS)){
				new DataUtils();
				DataUtils.reportFinalData(xls, Constants.KEYWORDS_SHEET, rNum, result);
				msg=result;
			}else if(result.endsWith(Constants.ERROR)){
				DataUtils.reportFinalData(xls, Constants.KEYWORDS_SHEET, rNum, result);
				msg=result;
			}else if(result.endsWith(Constants.FAIL)) {
				DataUtils.reportFinalData(xls, Constants.KEYWORDS_SHEET, rNum, result);
				msg=result;
				assertThrows(null);
			}
			return msg;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Issue while updating the test result in Keywords Sheet");
			return msg ;
		}
	}
}

