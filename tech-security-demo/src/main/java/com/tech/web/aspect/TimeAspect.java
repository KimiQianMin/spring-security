package com.tech.web.aspect;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

//@Component
//@Aspect
public class TimeAspect {

	// 环绕通知 还有其他类型的注解
	// 这里的表达式在官网可以学习怎么使用
	// https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop
	@Around("execution(* com.tech.web.controller.UserController.*(..))")
	public Object doAccessCheck(ProceedingJoinPoint point) throws Throwable {
		System.out.println("time aspect start");
		Instant start = Instant.now();

		System.out.println(ReflectionToStringBuilder.toString(point, ToStringStyle.MULTI_LINE_STYLE));

		Object[] args = point.getArgs();
		for (Object arg : args) {
			System.out.println(arg);
		}
		Object proceed = point.proceed(); // 类似于调用过滤器链一样
		System.out.println("TimeAspect 耗时：" + Duration.between(start, Instant.now()).toMillis());
		return proceed;
	}
}
