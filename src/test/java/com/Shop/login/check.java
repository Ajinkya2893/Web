package com.Shop.login;

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

public class check {
	
	@Test
	public void test() {
		String encrypted;
		String body;
		String otp = "";
		try {
			RestAssured.baseURI = "https://panel.pay1.in";
			RestAssured.basePath = "/platform/apis/";

			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,false);

			SortedMap<String,String> sortedMap = new TreeMap<String,String>();
			Map<String,String> map = new HashMap<String,String>();

			map.put("method","getOTPforTest");
			if(("https://shop.pay1.in/").contains("shop")) {
				map.put("app_name","recharge_app");
			}else if (("https://remit.pay1.in/").contains("remit")) {
				map.put("app_name","dmt");
			}
			map.put("mobile","7101000521");

			sortedMap.putAll(map);
			System.out.println(om.writeValueAsString(sortedMap));
			encrypted = new AESCrypt("Live").encrypt(om.writeValueAsString(sortedMap));
			System.out.println(encrypted);
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
			
		}
		catch (Exception e) {
			System.out.println("Error ");
		}
	}

}
