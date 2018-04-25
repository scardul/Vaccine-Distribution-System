package com.me.alpha.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ImageController {

	
	@RequestMapping(value = "/alpha-logo.png", method = RequestMethod.GET)
	public void uploadImage(HttpServletResponse response, HttpServletRequest request) {
		System.out.println("inside image cont");
		

		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		try {
			Path file = Paths.get("C:\\Users\\shard\\Desktop\\alpha-logo.png");
			byte[] data = Files.readAllBytes(file);
			System.out.println(file);
			response.getOutputStream().write(data);
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}