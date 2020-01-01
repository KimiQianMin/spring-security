package com.tech.web.interceptor;

import java.time.Duration;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TimeInterceptor implements HandlerInterceptor {
	// 进入方法前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute("startTime", Instant.now());
		HandlerMethod method = (HandlerMethod) handler;
		System.out.println("preHandle " + method.getBean().getClass().getName());
		System.out.println("preHandle " + method.getMethod().getName());
		System.out.println("preHandle " + method.getMethodParameters());

		System.out.println(ReflectionToStringBuilder.toString(method, ToStringStyle.MULTI_LINE_STYLE));

		// false will stop access to controller
		return true;
	}

	// 进入方法后
	// 如果方法异常，则不会进入该节点
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Instant startTime = (Instant) request.getAttribute("startTime");
		System.out.println("postHandle 耗时" + Duration.between(startTime, Instant.now()).toMillis());
	}

	// 请求后：无论如何都会走该节点
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		System.out.println("afterCompletion");
		Instant startTime = (Instant) request.getAttribute("startTime");
		System.out.println("afterCompletion 耗时" + Duration.between(startTime, Instant.now()).toMillis());
		// 注意这里的异常，如果异常被全局异常处理器ControllerExceptionHandler消费掉了的话，这里的异常信息的null
		System.out.println("afterCompletion ex " + ex);
	}
}
