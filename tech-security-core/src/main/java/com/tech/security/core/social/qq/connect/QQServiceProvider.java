/**
 * 
 */
package com.tech.security.core.social.qq.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

import com.tech.security.core.social.qq.api.QQ;
import com.tech.security.core.social.qq.api.impl.QQImpl;


/**
 * @author attpnxg1
 *
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

	public static final String authorizeUrl = "https://graph.qq.com/oauth2.0/authorize";
	public static final String accessTokenUrl = "https://graph.qq.com/oauth2.0/token";

	private String appId;

	/**
     * Create a new {@link OAuth2ServiceProvider}.
     */
    public QQServiceProvider(String appId, String secret) {
        // OAuth2Operations 有一个默认实现类，可以使用这个默认实现类
        // oauth2的一个流程服务
        super(new OAuth2Template(appId, secret, authorizeUrl, accessTokenUrl));
        this.appId = appId;
    }

	@Override
	public QQ getApi(String accessToken) {
		return new QQImpl(accessToken, appId);
	}
}
