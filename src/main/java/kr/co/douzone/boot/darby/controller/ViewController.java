package kr.co.douzone.boot.darby.controller;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@EnableWebMvc
public class ViewController extends WebMvcConfigurerAdapter {
	
	 private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

	    @RequestMapping("/")
	    public String home(Device device, HttpServletRequest request) {
	    	Device deviceFromRequest = DeviceUtils.getCurrentDevice(request);

	        if (device.isMobile()) {
	        	logger.info("Hello mobile user!");
	        } else if (device.isTablet()) {
	        	logger.info("Hello tablet user!");
	        } else {
	        	logger.info("Hello desktop user!");
	        }

	        logger.info("device: " + device);
	        logger.info("device form request: " + deviceFromRequest);
	        logger.info("device platform: " + device.getDevicePlatform());

//	        ModelAndView mav = new ModelAndView("/home_01");
//			return mav.getView();
	        return device.toString();
	    }
	
	@RequestMapping(path = "/home", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView hello(RedirectAttributes redirect, Model model
			,@RequestParam(value = "web", required = false, defaultValue = "") String web) {
		ModelAndView mav = new ModelAndView("/home");
		if(web != "") {
//			model.addAttribute("web", "web");
			mav.addObject("web", "web");
		}
		return mav;
	}
	
	@RequestMapping(path = "/menu", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView menuList(HttpServletRequest request, Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/menu_list");
//		model.addAttribute("userObj", request.getAttribute("userObj"));
		mav.addObject("userObj", request.getAttribute("userObj"));
		return mav;
	}
	
	
}