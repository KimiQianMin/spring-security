package com.tech.security.core.validate.code;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.tech.security.core.properties.SecurityProperties;

@RestController
public class ValidateCodeController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Autowired
	private ValidateCodeGenerator imageCodeGenerator;
	
	@Autowired
	private SecurityProperties securityProperties;

	@GetMapping("/code/image")
	public void createCode(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		logger.info("createCode is calling... ");

		ImageCode imageCode = imageCodeGenerator.createImageCode(request);
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}

	public ValidateCodeGenerator getImageCodeGenerator() {
		return imageCodeGenerator;
	}

	public void setImageCodeGenerator(ValidateCodeGenerator imageCodeGenerator) {
		this.imageCodeGenerator = imageCodeGenerator;
	}

}
