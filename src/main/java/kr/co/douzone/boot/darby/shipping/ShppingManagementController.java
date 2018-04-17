package kr.co.douzone.boot.darby.shipping;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64.Decoder;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.CoreConstants;
import kr.co.douzone.boot.darby.controller.ViewController;
import kr.co.douzone.boot.darby.user.MaUserController;

@RestController
@EnableWebMvc
public class ShppingManagementController extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	@Autowired
	ShppingManagementService service;

	/*
	 * 출하 계획
	 */
	@RequestMapping(path = "/breedingPig/shipping_plan", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView shippingPlan(Model model, RedirectAttributes redirect
			,@RequestParam(value = "date", required = false, defaultValue = "") String date) {
		ModelAndView mav = new ModelAndView("/breedingPig/shipping_plan");
		Date today = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ymd = sdf.format(today);
		if(date.equals("")) {
			mav.addObject("date", ymd);
//			model.addAttribute("date", ymd);
		} else {
			mav.addObject("date", date);
//			model.addAttribute("date", date);
		}
		return mav;
	}

	/*
	 * 출하 등록
	 */
	@RequestMapping(path = "/breedingPig/shipping_regist", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView shippingRegistGet(ModelMap model,
			@RequestParam(value = "rowData", required = false, defaultValue = "") String rowData,
			RedirectAttributes redirect) {
		JSONObject json_return = new JSONObject();
		json_return = jsonPasing(rowData);

		ModelAndView mav = new ModelAndView("/breedingPig/shipping_regist");
		mav.addObject("jsonObj", json_return.toString());
//		model.addAttribute("jsonObj", json_return.toString());
		return mav;
	}

	/*
	 * 사인 등록
	 */
	@RequestMapping(path = "/breedingPig/sign_regist", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView signRegist(ModelMap model, RedirectAttributes redirect
			, @RequestParam(value = "rowData", required = false, defaultValue = "") String rowData) {
		JSONObject json_return = new JSONObject();
		json_return = jsonPasing(rowData);
		JSONObject pasing = (JSONObject) json_return.get("paramJson");
		String sign = pasing.get("IMG_SIGN_FILE").toString();
		System.out.println("이미지 : " +  sign);
		
		ModelAndView mav = new ModelAndView("/breedingPig/sign_regist");
		mav.addObject("jsonObj", json_return.toString());
//		model.addAttribute("jsonObj", json_return.toString());
		mav.addObject("sign", "http://1.244.192.47:85/ERP-U/Upload/ship_sign/"+sign);
//		model.addAttribute("sign", "http://1.244.192.47:85/ERP-U/Upload/ship_sign/"+sign);
		return mav;
	}

	/*
	 * 종돈 입식 확인서
	 */
	@RequestMapping(path = "/breedingPig/pig_confirmation_form", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView outputForm(ModelMap model, RedirectAttributes redirect
			, @RequestParam(value = "data", required = false, defaultValue = "") String data) {
		JSONObject json_return = new JSONObject();
		json_return = jsonPasing(data);
		System.out.println(json_return);
		JSONObject paramJson =(JSONObject) json_return.get("paramJson");
		System.out.println(paramJson.get("img_sign_file"));

		String url = "http://1.244.192.47:85/ERP-U/Upload/ship_sign/" + paramJson.get("img_sign_file");
		System.out.println(url);
		String imageData = service.getByteArrayFromImageURL(url);

		ModelAndView mav = new ModelAndView("/breedingPig/pig_confirmation_form");
		mav.addObject("jsonObj", json_return.toString());
		mav.addObject("imageData", imageData);
//		model.addAttribute("jsonObj", json_return.toString());
//		model.addAttribute("imageData", imageData);
		return mav;
	}

	@ResponseBody
	@RequestMapping(path = "/breedingPig/pig_confirmation_form", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public ModelMap output(ModelMap model, @RequestParam String jsonObj, RedirectAttributes redirect) {
		JSONObject json_return = new JSONObject();
		json_return = jsonPasing(jsonObj);

		ModelAndView mav = new ModelAndView("breedingPig/pig_confirmation_form");
		mav.addObject("jsonObj", json_return.toString());
//		model.addAttribute("jsonObj", json_return.toString());
		return mav.getModelMap();
	}

	/*
	 * 이각 확인
	 */
	@RequestMapping(path = "/breedingPig/earTagged_check", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public View earTaggedCheck(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/breedingPig/earTagged_check");
		return mav.getView();
	}

	/*
	 * rest api 받아옴
	 * 모든 api는 여기로 헤더와 바디 맞추어 넣어 보냄
	 */
	@RequestMapping(value = "/api/getRest", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUserInfo(HttpServletResponse res, ModelMap model, @RequestParam("restUrl") String restUrl,
			@RequestParam("header") String header, @RequestParam("body") String body) throws Exception {
		MaUserController user = new MaUserController();
		System.out.println("get User Info + userUrl : " + restUrl);
		System.out.println("get User Info+ headerInfo : " + header);
		System.out.println("get User Info+ body : " + body);
		String response = user.commonApi(restUrl, header, body);
		res.setCharacterEncoding("UTF-8");
		return response;
	}

	/*
	 * 사인 등록
	 * 이미지 저장
	 */
	@RequestMapping(value = "/canv/canvasUploadProc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String canvasUploadProc(HttpServletRequest request, @RequestParam("strImg") String strImg,
			@RequestParam("cd_company") String cd_company, @RequestParam("no_ship_plan") String no_ship_plan,
			@RequestParam("cd_partner") String cd_partner) throws Throwable {
		
		System.out.println("page_canvasUpload > " + strImg);
		String uploadPath = "\\ship_sign\\";
		String folder = System.getProperty("catalina.home") + uploadPath;
		String fullpath = "";
		String[] strParts = strImg.split(",");
		String rstStrimg = strParts[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileNm = cd_company + no_ship_plan + cd_partner;

		BufferedImage image = null;
		byte[] byteImg;

		Decoder decoder = java.util.Base64.getDecoder();
		byteImg = decoder.decode(rstStrimg);

		ByteArrayInputStream bis = new ByteArrayInputStream(byteImg);
		image = ImageIO.read(bis);
		bis.close();
		fullpath = folder + fileNm + ".png";
		File folderObj = new File(folder);
		if (!folderObj.isDirectory())
			folderObj.mkdir();
		File outputFile = new File(fullpath);
		if (outputFile.exists())
			outputFile.delete();
		ImageIO.write(image, "png", outputFile);

		return fullpath;
	}
	
	/*
	 * 종돈 입식 확인서
	 * 이미지 저장
	 * 화면 다 뜬 후 저장하고 불러올 때는 서버에서 불러온다.
	 */
	@RequestMapping(value = "/canv/confirmationUploadProc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String confirmationUploadProc(HttpServletRequest request, @RequestParam("strImg") String strImg,
			@RequestParam("cd_company") String cd_company, @RequestParam("no_ship_plan") String no_ship_plan,
			@RequestParam("cd_partner") String cd_partner) throws Throwable {
		
		System.out.println("page_confirmationUpload > " + strImg);
		String uploadPath = "\\ship_confirmation\\";
		String fileNm = cd_company + no_ship_plan + cd_partner;
		String folder = System.getProperty("catalina.home") + uploadPath;
		String fullpath = "";
		String[] strParts = strImg.split(",");
		
		String rstStrimg = strParts[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		BufferedImage image = new BufferedImage( 900, 2500, BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics = image.createGraphics();

		graphics.setPaint ( new Color ( 255, 255, 255 ) );
		graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() );
		byte[] byteImg;

		Decoder decoder = java.util.Base64.getDecoder();
		byteImg = decoder.decode(rstStrimg);

		ByteArrayInputStream bis = new ByteArrayInputStream(byteImg);
		image = ImageIO.read(bis);
		bis.close();
		fullpath = folder + fileNm + ".png";
		File folderObj = new File(folder);
		if (!folderObj.isDirectory())
			folderObj.mkdir();
		File outputFile = new File(fullpath);
		if (outputFile.exists())
			outputFile.delete();
		
		ImageIO.write(image, "png", outputFile);

		return fullpath;
	}
	
	/*
	 * 스트링으로 받아온 데이터를 
	 * JSON으로 파싱하는 공통 부분
	 */
	public JSONObject jsonPasing(String data) {
		JSONObject json_return = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject paramObj = (JSONObject)parser.parse(data);
			json_return.put("paramJson", paramObj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return json_return;
	}
	
}
