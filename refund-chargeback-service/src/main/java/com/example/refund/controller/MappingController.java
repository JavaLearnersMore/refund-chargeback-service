package com.example.refund.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MappingController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getRefundUI() {
		ModelAndView mvc = new ModelAndView();
		mvc.setViewName("refund");
		return mvc;
	}
}
