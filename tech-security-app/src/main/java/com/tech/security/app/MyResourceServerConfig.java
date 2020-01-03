package com.tech.security.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.tech.security.app.authentication.MyAuthenticationFailureHandler;
import com.tech.security.app.authentication.MyAuthenticationSuccessHandler;
import com.tech.security.core.properties.SecurityProperties;

@Configuration
@EnableResourceServer
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {
    
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//http.httpBasic()
		//http.formLogin()
		http.formLogin()
			.loginPage("/authentication/require")
			.loginProcessingUrl("/authentication/form")
			.successHandler(myAuthenticationSuccessHandler)
			.failureHandler(myAuthenticationFailureHandler)
			.and()
			.authorizeRequests()
			.antMatchers("/authentication/require", 
						securityProperties.getBrowser().getLoginPage(),
						"/code/image").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf().disable();
	}
	
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    private SecurityProperties securityProperties;
//
//    @Autowired(required = false)
//    public TokenStore tokenStore;
//
//    @Autowired(required = false)
//    // 只有当使用jwt的时候才会有该对象
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//    /**
//     * @see TokenStoreConfig
//     */
//    @Autowired(required = false)
//    private TokenEnhancer jwtTokenEnhancer;
//
//    public MyAuthorizationServerConfig(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
//    }

//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        InMemoryClientDetailsServiceBuilder inMemory = clients.inMemory();
//        OAuth2ClientProperties[] clientsInCustom = securityProperties.getOauth2().getClients();
//        for (OAuth2ClientProperties p : clientsInCustom) {
//            inMemory.withClient(p.getClientId())
//                    .secret(p.getClientSecret())
//                    .redirectUris(p.getRedirectUris())
//                    .authorizedGrantTypes(p.getAuthorizedGrantTypes())
//                    .accessTokenValiditySeconds(p.getAccessTokenValiditySeconds())
//                    .scopes(p.getScopes());
//        }
//        logger.info(Arrays.toString(clientsInCustom));
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(this.authenticationManager);
//        endpoints.tokenStore(tokenStore);
//        /**
//         * 私有方法，但是在里面调用了accessTokenEnhancer.enhance所以这里使用链
//         * @see DefaultTokenServices#createAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication, org.springframework.security.oauth2.common.OAuth2RefreshToken)
//         */
//        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
//            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//            List<TokenEnhancer> enhancers = new ArrayList<>();
//            enhancers.add(jwtTokenEnhancer);
//            enhancers.add(jwtAccessTokenConverter);
//            enhancerChain.setTokenEnhancers(enhancers);
//            // 一个处理链，先添加，再转换
//            endpoints
//                    .tokenEnhancer(enhancerChain)
//                    .accessTokenConverter(jwtAccessTokenConverter);
//        }
//    }

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        // 这里使用什么密码需要 根据上面配置client信息里面的密码类型决定
//        // 目前上面配置的是无加密的密码
//        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
//    }
}
