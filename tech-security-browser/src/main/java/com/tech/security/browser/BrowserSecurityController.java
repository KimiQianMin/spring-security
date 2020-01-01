package com.tech.security.browser;

import java.io.IOException;

//import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tech.security.core.properties.SecurityProperties;
import com.tech.security.core.support.SimpleResponse;

@RestController
public class BrowserSecurityController {
    
	Logger logger = LoggerFactory.getLogger(getClass());
    
	// 工具类？
    private RequestCache requestCache = new HttpSessionRequestCache();

    // spring的跳转策略
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    //@Autowired
    //private ProviderSignInUtils providerSignInUtils;

    /**
     * 当需要身份认证时跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    //@RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requirAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转认证的请求 - {}", targetUrl);
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }

//    @GetMapping("/social/user")
//    public SocialUserInfo getSocialUserInfo(javax.servlet.http.HttpServletRequest request) {
//        SocialUserInfo userInfo = new SocialUserInfo();
//        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
//        userInfo.setProviderId(connection.getKey().getProviderId());
//        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
//        userInfo.setNickname(connection.getDisplayName());
//        userInfo.setHeadimg(connection.getImageUrl());
//        return userInfo;
//    }
//
//    @GetMapping("/session/invalid")
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public SimpleResponse sessionInvalid() {
//        String message = "session失效";
//        return new SimpleResponse(message);
//    }
}
