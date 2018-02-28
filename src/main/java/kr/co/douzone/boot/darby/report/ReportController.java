package kr.co.douzone.boot.darby.report;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.net.SyslogOutputStream;
import kr.co.douzone.boot.darby.controller.ViewController;
import kr.co.douzone.boot.darby.shipping.ShppingManagementController;

@RestController
@EnableWebMvc
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	ShppingManagementController shipping = new ShppingManagementController();
	
	/* 종돈공급 내역서 */
	@RequestMapping(path = "/report/breeding_pig_supply_statement", method = RequestMethod.GET)
	public ModelAndView breedingPigSupplyStatement(Model model, RedirectAttributes redirect
			,@RequestParam(value = "CD_PARTNER", required = false, defaultValue = "") String CD_PARTNER
			,@RequestParam(value = "DT_SHIP", required = false, defaultValue = "") String DT_SHIP) {
		
		ModelAndView mav = new ModelAndView("/report/breeding_pig_supply_statement");
		mav.addObject("CD_PARTNER", CD_PARTNER);
		mav.addObject("DT_SHIP", DT_SHIP);
//		model.addAttribute("paramData", json_return.toString());
		return mav;
	}
	
	/* 청구서(정액) */
	@RequestMapping(path = "/report/flat_rate_account", method = RequestMethod.GET)
	public View flatRateAccount(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/report/flat_rate_account");
		return mav.getView();
	}
	
	/* 
	 * 계회대비 실적
	 * 그리드 뷰
	  */
	@RequestMapping(path = "/report/performance_to_plan", method = RequestMethod.GET)
	public View performanceToPlan(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/report/performance_to_plan");
		return mav.getView();
	}
	
	/* 
	 * 계회대비 실적
	 * 차트 뷰
	  */
	@RequestMapping(path = "/report/perfomance_chart_view", method = RequestMethod.GET)
	public ModelAndView perfomanceChartView(Model model, RedirectAttributes redirect
			,@RequestParam(value = "paramData", required = false, defaultValue = "") String paramData) {
		JSONObject json_return = new JSONObject();
		json_return = shipping.jsonPasing(paramData);
		
		ModelAndView mav = new ModelAndView("/report/perfomance_chart_view");
		mav.addObject("paramData", json_return.toString());
//		model.addAttribute("paramData", json_return.toString());
		return mav;
	}
	
	/* 
	 * 종돈공급내역서
	 * 이미지 업로드
	  */
	@RequestMapping(value = "/canv/supplyUploadProc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String supplyUploadProc(HttpServletRequest request, @RequestParam("strImg") String strImg,
			@RequestParam("cd_company") String cd_company, @RequestParam("dt_ship") String dt_ship,
			@RequestParam("cd_partner") String cd_partner) throws Throwable {
		
		System.out.println("page_supplyUpload > " + strImg);
		String uploadPath = "\\supply_statment\\";
		String folder = System.getProperty("catalina.home") + uploadPath;
		String fullpath = "";
		String[] strParts = strImg.split(",");
		String rstStrimg = strParts[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileNm = cd_company + dt_ship + cd_partner;

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
	 * 청구서
	 * 이미지 업로드
	  */
	@RequestMapping(value = "/canv/accountUploadProc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String accountUploadProc(HttpServletRequest request, @RequestParam("strImg") String strImg,
			@RequestParam("cd_company") String cd_company, @RequestParam("dt_start_end") String dt_start_end,
			@RequestParam("cd_partner") String cd_partner) throws Throwable {
		
		System.out.println("page_accountUpload > " + strImg);
		String uploadPath = "\\bill\\";
		String folder = System.getProperty("catalina.home") + uploadPath;
		String fullpath = "";
		String[] strParts = strImg.split(",");
		String rstStrimg = strParts[1];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileNm = cd_company + dt_start_end + cd_partner;

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
	
}
