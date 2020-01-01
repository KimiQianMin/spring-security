package com.tech.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//@Component
public class TimeFilter implements Filter {
	// 初始化
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("TimeFilter init");
	}

	// 执行
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("time filter start");
		long start = new Date().getTime();
		chain.doFilter(request, response);
		System.out.println("time filter 耗时：" + (new Date().getTime() - start));
		System.out.println("time filter finish");
	}

	// 销毁
	@Override
	public void destroy() {
		System.out.println("TimeFilter destroy");
	}
}