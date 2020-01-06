package com.tech.security.core.validate.code;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tech.security.core.properties.SecurityProperties;

@Component
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	@Autowired
	private AuthenticationFailureHandler defaultAuthenticationFailureHandler;

	@Autowired
	private SecurityProperties securityProperties;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("doFilterInternal is calling ... ");

		logger.info("request.getRequestURI() - {}", request.getRequestURI());
		logger.info("request.getMethod() - {}", request.getMethod());

		// String url = securityProperties.getCode().getImage().getUrl();
		String url = "/authentication/mobile";
		logger.info("url - {}", url);

		String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(url, ",");

		boolean action = false;
		for (String u : urls) {
			if (pathMatcher.match(u, request.getRequestURI())) {
				action = true;
				break;
			}
		}

		if (action) {
			try {
				validate(new ServletWebRequest(request));
			} catch (ValidateCodeException ex) {
				defaultAuthenticationFailureHandler.onAuthenticationFailure(request, response, ex);
				return;
			}
		}

//		if (StringUtils.equals("/authentication/form", request.getRequestURI())
//				&& StringUtils.equals(request.getMethod(), "POST")) {
//			logger.info("inside");
//
//			try {
//				validate(new ServletWebRequest(request));
//			} catch (ValidateCodeException ex) {
//				defaultAuthenticationFailureHandler.onAuthenticationFailure(request, response, ex);
//				return;
//			}
//		}

		filterChain.doFilter(request, response);
	}

	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		// 拿到自己的类型
		ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request,
				ValidateCodeController.SESSION_KEY_SMS);

		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");

		logger.info("codeInSession - {}", codeInSession);
		logger.info("codeInRequest - {}", codeInRequest);

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("验证码的值不能为空");
		}
		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在");
		}
		// if (codeInSession) {
		// validateCodeRepository.remove(request, type);
		// throw new ValidateCodeException("验证码已过期");
		// }
		if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException("验证码不匹配");
		}

		sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY_SMS);
	}

	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

	public AuthenticationFailureHandler getDefaultAuthenticationFailureHandler() {
		return defaultAuthenticationFailureHandler;
	}

	public void setDefaultAuthenticationFailureHandler(
			AuthenticationFailureHandler defaultAuthenticationFailureHandler) {
		this.defaultAuthenticationFailureHandler = defaultAuthenticationFailureHandler;
	}

}
