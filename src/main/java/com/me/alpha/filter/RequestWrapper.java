package com.me.alpha.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.stereotype.Component;


public class RequestWrapper extends HttpServletRequestWrapper {

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String paramName) {
		System.out.println("here is "+paramName);
        String value = super.getParameter(paramName);
        System.out.println(value);
        if(value!=null) {
        	value = sanitize(value);
        }
        return value;
    }
	
	public String sanitize(String input) {
        String result = input.replaceAll("[-.\\+*?\\[^\\]$%(){}=!<>|:\\\\]", "");
        return result;
    }
}
