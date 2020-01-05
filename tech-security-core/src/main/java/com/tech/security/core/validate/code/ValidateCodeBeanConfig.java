/**
 * 
 */
package com.tech.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tech.security.core.properties.SecurityProperties;
import com.tech.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.tech.security.core.validate.code.sms.SmsCodeSender;

/**
 * @author attpnxg1
 *
 */
@Configuration
public class ValidateCodeBeanConfig {

	@Autowired
	private SecurityProperties securityProperties;

	@Bean
	@ConditionalOnMissingBean(name = "imageCodeGenerator")
	public ValidateCodeGenerator imageCodeGenerator() {
		ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
		imageCodeGenerator.setSecurityProperties(securityProperties);
		return imageCodeGenerator;
	}
	
	@Bean
	@ConditionalOnMissingBean(name = "smsCodeSender")
	public SmsCodeSender smsCodeSender() {
		DefaultSmsCodeSender smsCodeSender = new DefaultSmsCodeSender();
		return smsCodeSender;
	}

}
