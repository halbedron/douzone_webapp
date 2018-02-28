package kr.co.douzone.boot.darby.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HeaderInfo {

	public JSONObject headerInfo(String headerStr, String bodyStr) {
		System.out.println("2");
		JSONObject json_return = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			Object headerObj = parser.parse(headerStr);
			JSONObject headerJson = (JSONObject) headerObj;
			json_return.put("header", headerJson);
			
			if(bodyStr != null) {
				Object bodyObj = parser.parse(bodyStr);
				JSONObject bodyJson = (JSONObject) bodyObj;
				json_return.put("body", bodyJson);
			} else {
				Map<String, String> bodyParam =  new HashMap<String, String>();
				json_return.put("body", bodyParam);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return json_return;
	}
}
