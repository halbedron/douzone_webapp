package kr.co.douzone.boot.darby.sales;

import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.douzone.boot.darby.shipping.ShppingManagementController;

@RestController
@EnableWebMvc
public class SalesManagementController extends WebMvcConfigurerAdapter {
	
	ShppingManagementController shipping = new ShppingManagementController();
	
	@RequestMapping(path = "/sales/partner_detail", method = RequestMethod.GET)
	public ModelAndView customerInformationDetail(Model model, RedirectAttributes redirect
			, @RequestParam(value = "partner", required = false, defaultValue = "") String partner) {
		ModelAndView mav = new ModelAndView("/sales/partner_detail");
		mav.addObject("partner", partner);
//		model.addAttribute("partner", partner);
		return mav;
	}
	
	@RequestMapping(path = "/sales/partner", method = RequestMethod.GET)
	public View customerInformation(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/partner");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/shipping_related", method = RequestMethod.GET)
	public View shippingRelated(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/shipping_related");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/dealing_by_customer", method = RequestMethod.GET)
	public View transactionDetailsByCustomer(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/dealing_by_customer");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/claim_management", method = RequestMethod.GET)
	public View claimManagement(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/claim_management");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/claim_management_account", method = RequestMethod.GET)
	public ModelAndView claimManagement(ModelMap model, RedirectAttributes redirect
			, @RequestParam(value = "partner", required = false, defaultValue = "") String partner) {
		model.addAttribute("partner", partner);
		ModelAndView mav = new ModelAndView("/sales/claim_management", model);
		return mav;
	}
	
	@RequestMapping(path = "/sales/credit_sales_status", method = RequestMethod.GET)
	public ModelAndView creditSalesStatus(Model model, RedirectAttributes redirect
			,@RequestParam(value = "rowData", required = false, defaultValue = "") String rowData) {
		JSONObject json_return = new JSONObject();
		json_return = shipping.jsonPasing(rowData);
		
		ModelAndView mav = new ModelAndView("/sales/credit_sales_status");
		mav.addObject("rowData", json_return.toString());
		return mav;
	}
	
	@RequestMapping(path = "/sales/accounts_receivable_status_detail", method = RequestMethod.GET)
	public ModelAndView accountsReceivableStatusDetail(Model model, RedirectAttributes redirect
			,@RequestParam(value = "accountDetail", required = false, defaultValue = "") String accountDetail) {
		JSONObject json_return = new JSONObject();
		json_return = shipping.jsonPasing(accountDetail);
		
		ModelAndView mav = new ModelAndView("/sales/accounts_receivable_status_detail");
		mav.addObject("accountDetail", json_return.toString());
//		model.addAttribute("accountDetail", json_return.toString());
		return mav;
	}
	
	@RequestMapping(path = "/sales/accounts_receivable_status", method = RequestMethod.GET)
	public View accountsReceivableStatus(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/accounts_receivable_status");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/collection_status", method = RequestMethod.GET)
	public View collectionStatus(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/collection_status");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/pig_farm_price_information", method = RequestMethod.GET)
	public View pigFarmPriceInformation(Model model, RedirectAttributes redirect) {
		ModelAndView mav = new ModelAndView("/sales/pig_farm_price_information");
		return mav.getView();
	}
	
	@RequestMapping(path = "/sales/map_view", method = RequestMethod.GET)
	public ModelAndView mapView(Model model, RedirectAttributes redirect
			, @RequestParam(value = "addr") String addr
			, @RequestParam(value = "lat") String lat
			, @RequestParam(value = "lng") String lng) {
		
		ModelAndView mav = new ModelAndView("/sales/map_view");
		mav.addObject("addr", addr);
		mav.addObject("lat", lat);
		mav.addObject("lng", lng);
//		model.addAttribute("addr", addr);
//		model.addAttribute("lat", lat);
//		model.addAttribute("lng", lng);
		return mav;
		
	}
	
}
