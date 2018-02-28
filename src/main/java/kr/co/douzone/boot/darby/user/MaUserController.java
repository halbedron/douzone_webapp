package kr.co.douzone.boot.darby.user;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import eu.bitwalker.useragentutils.UserAgent;
import kr.co.douzone.boot.darby.controller.HeaderInfo;

@RestController
public class MaUserController {
	@Autowired
	MaUserService service;
	
	public String hederInfo;
	
	/*
	 *  헤더 인포 받아 오는 api
	 */
	@RequestMapping(path = "/api/getInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInfo(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		
		ModelAndView mav = new ModelAndView("/home");
		mav.addObject("userAgent", userAgent);
		return mav.getModelMap();
	}
	
	/*
	 * API 怨듯넻 肄붾뱶
	 * parameter : url, 
	 */
	public String commonApi(String urlParam, String headerStr, String bodyStr) {
		StringBuffer response = null;
		try {
			String url = "";
			if(urlParam == null || urlParam == "undefined") {
				url = "http://175.206.170.36:85/DuzonMobileCenter/_restapi/restCustomHost.aspx";
			} else {
				url = urlParam;
			}
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			
			HeaderInfo headerInfo = new HeaderInfo();
			JSONObject jsonParam = headerInfo.headerInfo(headerStr, bodyStr);
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			byte[] buf = jsonParam.toString().getBytes("UTF-8");
			wr.write(buf, 0, buf.length);
//			wr.writeBytes(jsonParam.toString());
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + jsonParam.toString());
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//			System.out.println("?"+in.readLine());
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response.toString();
		
	}
	
	/*
	 *  1. Host Url 받아 옴
	*/	
	@RequestMapping(value = "/api/getUrl", method = RequestMethod.GET, produces = "application/text; charset=utf8")
    @ResponseBody
    public String getUrl(@RequestParam("header") String header) {
		System.out.println(header);
		hederInfo = header;
		String response = commonApi(null, header, null);
		System.out.println("�꽌踰� host url" + response);
		return response;
    }
	
	/*	
	 * 2. API list 받아옴
	 */
	@RequestMapping(value = "/api/getApi", method = RequestMethod.GET, produces = "application/text; charset=utf8")
    @ResponseBody
    public String getApi(@RequestParam("hostUrl") String hostUrl) {
		System.out.println("get API : " + hostUrl);
		String response = commonApi(hostUrl + "/_gateway/requestApi.aspx", hederInfo, null);
		return response;
	}
	
	/*
	 * 3. session 받아오는 부분
	 */
	@RequestMapping(value = "/api/getSession", method = RequestMethod.GET, produces = "application/text; charset=utf8")
    @ResponseBody
    public String getSession(@RequestParam("certUrl") String certUrl, @RequestParam("body") String body) {
		System.out.println("get Session key + certUrl : " + certUrl);
		System.out.println("get Session key + bodyObj : " + body);
		String response = commonApi(certUrl, hederInfo, body);
		return response;
	}
	
	/*
	 * 4. user info 받아오는 부분
	 */
	@RequestMapping(value = "/api/getUserInfo", method = RequestMethod.GET, produces = "application/text; charset=utf8")
    @ResponseBody
    public String getUserInfo(ModelMap model
    		, @RequestParam("userUrl") String userUrl, @RequestParam("header") String header
    		, @RequestParam("body") String body) throws Exception {
		hederInfo = header;
		System.out.println("get User Info + userUrl : " + userUrl);
		System.out.println("get User Info+ body : " + body);
		String response = commonApi(userUrl, hederInfo, body);
		
		JSONObject jsonResponse = new JSONObject();
		JSONParser parser = new JSONParser();
		Object responseObj = parser.parse(response);
		jsonResponse = (JSONObject) responseObj;
//		String resultCode = (String) jsonResponse.get("resultCode");
		System.out.println("jsonResponse : " + jsonResponse.toString());
		return response.toString();
	}
	
}
