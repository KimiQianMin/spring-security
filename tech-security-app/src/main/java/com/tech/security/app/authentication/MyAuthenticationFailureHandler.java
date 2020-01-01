package com.tech.security.app.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.security.core.properties.LoginType;
import com.tech.security.core.properties.SecurityProperties;
import com.tech.security.core.support.SimpleResponse;

@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		logger.info("登录失败");

		if (securityProperties.getBrowser().getLoginType() == LoginType.JSON) {
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			logger.error("登录失败:", exception);
			response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
		} else {
			// 在这里失败跳转不回去了。而且异常信息也没有打印出来。父类默认打印了死的一句话
			// 在这里就不往上面扔了,这里就先当做 defaultFailureUrl 不存在吧
			// 模拟打印异常信息
			// response.setContentType("text/html;charset=UTF-8");
			// response.sendError(HttpStatus.UNAUTHORIZED.value(),
			// exception.getLocalizedMessage());

			// 源来是这个配置的问题。在视频中看到 在更改上面json分支的异常
			// 只打印异常信息，不中的视频中上面时候把类型给更换了
			// 害的我一直怀疑是版本不同，处理逻辑不同了
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}
