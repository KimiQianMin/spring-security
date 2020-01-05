package com.tech.security.core.validate.code;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeGenerator {

	ValidateCode generate(HttpServletRequest request) throws IOException;

}
