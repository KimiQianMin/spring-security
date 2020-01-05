/**
 * 
 */
package com.tech.code;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.tech.security.core.validate.code.ImageCode;
import com.tech.security.core.validate.code.ValidateCodeGenerator;

/**
 * @author attpnxg1
 *
 */

//app will read this imageCodeGenerator bean if configured, otherwise will read the default from code project
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

	@Override
	public ImageCode generate(HttpServletRequest request) throws IOException {
		System.out.println("demo createImageCode ...");
		return null;
	}

}
