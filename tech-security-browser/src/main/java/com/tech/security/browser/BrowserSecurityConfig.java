package com.tech.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import com.tech.security.browser.authentication.MyAuthenticationFailureHandler;
import com.tech.security.browser.authentication.MyAuthenticationSuccessHandler;
import com.tech.security.core.authorize.AuthorizeConfigManager;
import com.tech.security.core.properties.SecurityProperties;
import com.tech.security.core.validate.code.ValidateCodeFilter;

/**
 * @author attpnxg1
 *
 */

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    
    @Autowired
    private SpringSocialConfigurer techSocialSecurityConfig;
    
    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
		
		//http.httpBasic()
		//http.formLogin()
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin()
			.loginPage("/authentication/require")
			.loginProcessingUrl("/authentication/form")
			.successHandler(myAuthenticationSuccessHandler)
			.failureHandler(myAuthenticationFailureHandler);
			
		http.apply(techSocialSecurityConfig)
			.and()
//			.authorizeRequests()
//				.antMatchers(
//						"/authentication/require", 
//						securityProperties.getBrowser().getLoginPage(),
//						"/code/image")
//					.permitAll()
//				.antMatchers(HttpMethod.GET,"/user/{id:\\d+}")
//					.hasRole("ADMIN1")
//			.anyRequest()
//			.authenticated()
//			.and()
			.csrf().disable();
		
		authorizeConfigManager.config(http.authorizeRequests());
		
	}

}
