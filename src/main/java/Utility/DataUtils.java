package Utility;

import java.util.Hashtable;

public class DataUtils {

	public String sheetName;

	public static Object[][] getData(Excel_Reader xls, String testName){
		String sheetName="Data";
		// reads data for only testCaseName

		int testStartRowNum=1;
		while(!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)){
			testStartRowNum++;
		}
		int colStartRowNum=testStartRowNum+1;
		int dataStartRowNum=testStartRowNum+2;

		// calculate rows of data
		int rows=0;
		while(!xls.getCellData(sheetName, 0, dataStartRowNum+rows).equals("")){
			rows++;
		}

		//calculate total cols
		int cols=0;
		while(!xls.getCellData(sheetName, cols, colStartRowNum).equals("")){
			cols++;
		}
		Object[][] data = new Object[rows][1];
		//read the data
		int dataRow=0;
		Hashtable<String,String> table=null;
		for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
			table = new Hashtable<String,String>();
			for(int cNum=0;cNum<cols;cNum++){
				String key=xls.getCellData(sheetName,cNum,colStartRowNum);
				String value= xls.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
				// 0,0 0,1 0,2
				//1,0 1,1
			}
			data[dataRow][0] =table;
			dataRow++;
		}
		return data;
	}
	// true - N
	// false - Y
	//Checks the test is runnable or not 
	public static boolean isSkip(Excel_Reader xls, String testName){
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);

		for(int rNum=2;rNum<=rows;rNum++){
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testName)){
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				if(runmode.equals("Y"))
					return false;
				else
					return true;
			}
		}

		return true;//default

	}

	public static String testDescription(Excel_Reader xls, String testName){

		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);

		for(int rNum=2;rNum<=rows;rNum++){
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testName)){
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				if(runmode.equals("Y")){
					String desc = xls.getCellData(Constants.TESTCASES_SHEET, Constants.DESCRIPTION_COL, rNum);
					return desc;
				}
			}
		}
		return null;
	}

	public static int getRnum(Excel_Reader xls, String testName) {
		String sheetName="Data";
		int testStartRowNum=1;
		while(!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName))
			testStartRowNum++;
		int colStartRowNum=testStartRowNum+1;
		return colStartRowNum;
	}

	//return the row number for the test 
	public static int getRowNum(Excel_Reader xls, String id){
		for(int i = 2; i <= xls.getRowCount("TestCases"); i++){
			String TC_ID = xls.getCellData("TestCases", "TCID", i);

			if(TC_ID.equals(id)){
				xls=null;
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unused")
	public static int findRowCol(Excel_Reader xls, String testName, String res) {
		String sheetName="Data";
		int testStartRowNum=1;
		while(!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)){
			testStartRowNum++;
		}
		int colStartRowNum=testStartRowNum+1;
		int dataStartRowNum=testStartRowNum+2;

		int rows=0;
		while(!xls.getCellData(sheetName, 0, dataStartRowNum+rows).equals("")){
			rows++;
		}
		int cols=0;
		while(!xls.getCellData(sheetName, cols, colStartRowNum).equals("")){
			cols++;
		}
		/*@SuppressWarnings("unused")
		int dataRow=0;*/
		int col = 0;
		for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
			for(int cNum=0;cNum<cols;cNum++){
				String key=xls.getCellData(sheetName,cNum,colStartRowNum);
				if (key.equals(res)) {
					//test(xls,sheetName, cNum, colStartRowNum, "test");
					col = cNum;
					break;
				}
			}break;
		}	
		return col;
	}

	//update results for a particular dataset
	/**Setting the cell data for the specified sheet and column & row with the data **/
	public static void reportDataSetResult(Excel_Reader xls, String testName, int rowNum, String result){
		int col = findRowCol(xls, testName, Constants.RESULTS_COL);
		xls.setCellData(Constants.DATA_SHEET, col , rowNum, result);
	}

	public static void reportFinalData(Excel_Reader xls, String sheetName, int rowNum, String result){
		xls.setCellData(sheetName, Constants.RESULTS_COL, rowNum, result);
	}

	//update actual result for a particular dataset
	/**Setting the cell data for the specified sheet and column & row with the data **/
	public static void reportDataSetActualResult(Excel_Reader xls, String testCaseName, int rowNum, String act_result){
		int col = findRowCol(xls, testCaseName, Constants.ACTUALRESULTS_COL);
		xls.setCellData(Constants.DATA_SHEET, col , rowNum, act_result);
	}

	//update page name for a particular test case
	public static void reportMismatchValue(Excel_Reader xls, String testCaseName, int rowNum, String mismatch_data){
		xls.setCellData(testCaseName, "Error", rowNum, mismatch_data);
	}

	public static void reportError(Excel_Reader xls, String testCaseName, int rowNum, String error){
		xls.setCellData(testCaseName, "Error", rowNum, error);
	}

	//update the Extra time slot
	public static void ExtraTimeSlot(Excel_Reader xls, String testCaseName, int rowNum, String mismatch_data){
		xls.setCellData(testCaseName, "Extra TimeSlot", rowNum, mismatch_data);
	}

	//update issuers list in Drop Down List column for a particular test case
	public static void reportDropDownListName(Excel_Reader xls, String testCaseName, int rowNum, String dropdownlistname){
		xls.setCellData(testCaseName, "Drop Down List", rowNum, dropdownlistname);
	}
}
