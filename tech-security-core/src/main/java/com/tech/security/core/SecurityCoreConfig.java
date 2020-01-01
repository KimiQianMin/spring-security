package com.tech.security.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.tech.security.core.properties.SecurityProperties;

/**
 * 让SecurityProperties这个配置类生效 EnableConfigurationProperties 的作用是标明加载哪一个类
 * 这效果和直接在目标类上写上@Configuration效果一样
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
}
