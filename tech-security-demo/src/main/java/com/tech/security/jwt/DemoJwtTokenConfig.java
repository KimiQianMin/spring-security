package com.tech.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

@Configuration
public class DemoJwtTokenConfig {
	
	@Bean
	public TokenEnhancer jwtTokenEnhancer(){
		return new TechJwtTokenEnhancer();
	}

}
