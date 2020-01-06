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
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.tech.security.core.properties.SecurityProperties;
import com.tech.security.core.validate.code.sms.SmsCodeSender;

@RestController
public class ValidateCodeController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	static final String SESSION_KEY_IMAGE = "SESSION_KEY_IMAGE_CODE";
	static final String SESSION_KEY_SMS = "SESSION_KEY_SMS_CODE";

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	@Autowired
	private ValidateCodeGenerator imageCodeGenerator;

	@Autowired
	private ValidateCodeGenerator smsCodeGenerator;

	@Autowired
	private SmsCodeSender smsCodeSender;

	@Autowired
	private SecurityProperties securityProperties;

	@GetMapping("/code/image")
	public void createCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.info("imageCodeGenerator createCode is calling... ");

		ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(request);
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_IMAGE, imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}

	@GetMapping("/code/sms")
	public void createCodeSMS(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletRequestBindingException {

		logger.info("smsCodeGenerator createCode is calling... ");

		ValidateCode validateCode = smsCodeGenerator.generate(request);
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_SMS, validateCode);
		String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");

		smsCodeSender.send(mobile, validateCode.getCode());
	}

}
