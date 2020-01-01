package com.tech.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.tech.security.app.jwt.TechJwtTokenEnhancer;
import com.tech.security.core.properties.SecurityProperties;

@Configuration
public class TokenStoreConfig {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	@ConditionalOnProperty(prefix = "tech.security.oauth2", name = "storeType", havingValue = "jwt")
	public TokenStore redisTokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Configuration
	@ConditionalOnProperty(prefix = "tech.security.oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
	public static class JwtTokenConfig {

		@Autowired
		private SecurityProperties securityProperties;

		@Bean
		public TokenStore jwtTokenStore() {
			return new JwtTokenStore(jwtAccessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
			accessTokenConverter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
			return accessTokenConverter;
		}
		
		@Bean
		@ConditionalOnMissingBean(name = "jwtTokenEnhancer")
		public TokenEnhancer jwtTokenEnhancer(){
			return new TechJwtTokenEnhancer();
			
		}
		
	}

}
