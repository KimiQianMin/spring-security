package com.tech.security.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.tech.security.core.properties.OAuth2ClientProperties;
import com.tech.security.core.properties.SecurityProperties;

@Configuration
@EnableAuthorizationServer
public class MyAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired(required = false)
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	@Autowired(required = false)
	private TokenEnhancer jwtTokenEnhancer;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.tokenStore(tokenStore);
		
		if(jwtAccessTokenConverter != null && jwtTokenEnhancer != null){
			
			TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
			
			List<TokenEnhancer> enhancers = new ArrayList<>();
			enhancers.add(jwtTokenEnhancer);
			enhancers.add(jwtAccessTokenConverter);
			
			tokenEnhancerChain.setTokenEnhancers(enhancers);
			
			endpoints.tokenEnhancer(tokenEnhancerChain);
			
			//endpoints.accessTokenConverter(jwtAccessTokenConverter);
		}
		
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
		
		logger.info("securityProperties.getoAuth2().getClients().length >>>" + securityProperties.getOauth2().getClients().length);
		for(OAuth2ClientProperties config : securityProperties.getOauth2().getClients()){
			logger.info("config.toString() >>>" + config);
		}
		
		if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())){
			for(OAuth2ClientProperties config : securityProperties.getOauth2().getClients()){
				builder.withClient(config.getClientId())
				.secret(config.getClientSecret())
				.accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())
				.authorizedGrantTypes(config.getAuthorizedGrantTypes())
				.scopes(config.getScopes());
			}
		}
	}
	
	
	// @Autowired
	// private SecurityProperties securityProperties;
	//
	// @Autowired(required = false)
	// public TokenStore tokenStore;
	//
	// @Autowired(required = false)
	// // 只有当使用jwt的时候才会有该对象
	// private JwtAccessTokenConverter jwtAccessTokenConverter;
	// /**
	// * @see TokenStoreConfig
	// */
	// @Autowired(required = false)
	// private TokenEnhancer jwtTokenEnhancer;
	//
	// public MyAuthorizationServerConfig(
	// AuthenticationConfiguration authenticationConfiguration) throws Exception
	// {
	// this.authenticationManager =
	// authenticationConfiguration.getAuthenticationManager();
	// }

	// @Override
	// public void configure(ClientDetailsServiceConfigurer clients) throws
	// Exception {
	// InMemoryClientDetailsServiceBuilder inMemory = clients.inMemory();
	// OAuth2ClientProperties[] clientsInCustom =
	// securityProperties.getOauth2().getClients();
	// for (OAuth2ClientProperties p : clientsInCustom) {
	// inMemory.withClient(p.getClientId())
	// .secret(p.getClientSecret())
	// .redirectUris(p.getRedirectUris())
	// .authorizedGrantTypes(p.getAuthorizedGrantTypes())
	// .accessTokenValiditySeconds(p.getAccessTokenValiditySeconds())
	// .scopes(p.getScopes());
	// }
	// logger.info(Arrays.toString(clientsInCustom));
	// }
	//
	// @Override
	// public void configure(AuthorizationServerEndpointsConfigurer endpoints)
	// throws Exception {
	// endpoints.authenticationManager(this.authenticationManager);
	// endpoints.tokenStore(tokenStore);
	// /**
	// * 私有方法，但是在里面调用了accessTokenEnhancer.enhance所以这里使用链
	// * @see
	// DefaultTokenServices#createAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication,
	// org.springframework.security.oauth2.common.OAuth2RefreshToken)
	// */
	// if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
	// TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
	// List<TokenEnhancer> enhancers = new ArrayList<>();
	// enhancers.add(jwtTokenEnhancer);
	// enhancers.add(jwtAccessTokenConverter);
	// enhancerChain.setTokenEnhancers(enhancers);
	// // 一个处理链，先添加，再转换
	// endpoints
	// .tokenEnhancer(enhancerChain)
	// .accessTokenConverter(jwtAccessTokenConverter);
	// }
	// }

	// @Override
	// public void configure(AuthorizationServerSecurityConfigurer security)
	// throws Exception {
	// // 这里使用什么密码需要 根据上面配置client信息里面的密码类型决定
	// // 目前上面配置的是无加密的密码
	// security.passwordEncoder(NoOpPasswordEncoder.getInstance());
	// }
}
