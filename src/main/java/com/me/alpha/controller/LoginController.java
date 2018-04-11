package com.me.alpha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	public String home() {
		String ret=null;
		

		return ret;
	}

}
