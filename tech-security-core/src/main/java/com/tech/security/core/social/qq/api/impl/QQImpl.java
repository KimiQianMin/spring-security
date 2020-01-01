package com.tech.security.core.social.qq.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.security.core.social.qq.api.QQ;
import com.tech.security.core.social.qq.api.QQUserInfo;

public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

	Logger logger = LoggerFactory.getLogger(getClass());

	public final static String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

	public final static String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

	private String appId;

	private String openid;

	private ObjectMapper objectMapper = new ObjectMapper();

	public QQImpl(String accessToken, String appId) {
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

		this.appId = appId;

		String url = String.format(URL_GET_OPENID, accessToken);
		String result = super.getRestTemplate().getForObject(url, String.class);

		logger.info("result - {}", result);

		this.openid = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
	}

	@Override
	public QQUserInfo getUserInfo() {

		String url = String.format(URL_GET_USER_INFO, appId, openid);

		String result = getRestTemplate().getForObject(url, String.class);

		logger.info(result);

		try {
			return objectMapper.readValue(result, QQUserInfo.class);
		} catch (Exception e) {
			throw new RuntimeException("getUserInfo have problem");
		}
	}

}
