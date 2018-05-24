package keywords;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import Utility.AESCrypt;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getOtpNumber("live", "shop");
	}

	@Test
	public void test1() {
		getOtpNumber("live", "shop");
	}

	private static String getOtpNumber(String env, String app) {
		String encrypted;
		String body;
		String otp = "";
		try {

			RestAssured.baseURI = "https://paneluat.pay1.in";
			RestAssured.basePath = "/platform/apis/";

			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,false);

			SortedMap<String,String> sortedMap = new TreeMap<String,String>();
			Map<String,String> map = new HashMap<String,String>();

			map.put("method","getOTPforTest");
			if(app.equalsIgnoreCase("shop")) {
				map.put("app_name","recharge_app");
			}else if (app.equalsIgnoreCase("remit")) {
				map.put("app_name","dmt");
			}
			map.put("mobile","7101000521");

			sortedMap.putAll(map);
			System.out.println();
			encrypted = new AESCrypt(env).encrypt(om.writeValueAsString(sortedMap));

			Response response  = RestAssured.given()
					.param("req",encrypted)
					.when()
					.post()
					.then()
					.extract().response();

			System.out.println(response.asString());
			body = response.asString();

			if("failure".equals(new JsonPath(body).get("status"))) {
				System.out.println("Transaction Already Done");
			}else if("success".equals(new JsonPath(body).get("status"))) {
				otp = new JsonPath(body).get("otp");
				System.out.println(otp);
			}
			return otp;
		}
		catch (Exception e) {
			System.out.println("Error ");
			return null;
		}

	}

}
