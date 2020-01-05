/**
 * 
 */
package com.tech.security.core.validate.code.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author attpnxg1
 *
 */
public class DefaultSmsCodeSender implements SmsCodeSender {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void send(String mobile, String code) {
		logger.info("mobile - {}, code - {}", mobile, code);
	}

}
