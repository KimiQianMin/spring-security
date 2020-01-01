/**
 *
 */
package com.tech.security.core.social;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
//	@Autowired
//    private SecurityProperties securityProperties;
   
	@Autowired
    private DataSource dataSource;

    /**
     * 不存在则不使用默认注册用户，而是跳转到注册页完成注册或则绑定
     */
//    @Autowired(required = false)
//    private ConnectionSignUp connectionSignUp;

//    @Autowired(required = false)
//    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("psa_");
        //repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

//    @Override
//    public UserIdSource getUserIdSource() {
//        return new AuthenticationNameUserIdSource();
//    }
//
    @Bean
    public SpringSocialConfigurer techSocialSecurityConfig() {
        // 默认配置类，进行组件的组装
        // 包括了过滤器SocialAuthenticationFilter 添加到security过滤链中
    	logger.info("techSocialSecurityConfig()...");
        // MySpringSocialConfigurer springSocialConfigurer = new MySpringSocialConfigurer();
        SpringSocialConfigurer springSocialConfigurer = new SpringSocialConfigurer();
        
//        springSocialConfigurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
//        springSocialConfigurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return springSocialConfigurer;
    }
//
//    //https://docs.spring.io/spring-social/docs/1.1.x-SNAPSHOT/reference/htmlsingle/#creating-connections-with-connectcontroller
//    // 必须要添加一个处理器
//    // 后补：这个是提供查询社交账户信息服务，绑定服务，等
//    @Bean
//    public ConnectController connectController(
//            ConnectionFactoryLocator connectionFactoryLocator,
//            ConnectionRepository connectionRepository) {
//        return new ConnectController(connectionFactoryLocator, connectionRepository);
//    }
//
//    @Bean
//    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator,
//                                                   UsersConnectionRepository usersConnectionRepository) {
//        // ProviderSignInUtils 提供的工具类
//        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
//    }
}
