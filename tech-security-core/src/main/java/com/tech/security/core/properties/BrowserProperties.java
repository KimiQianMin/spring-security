package com.tech.security.core.properties;

public class BrowserProperties {

	private String loginPage = "/tech-signIn.html";

	private LoginType loginType = LoginType.REDIRECT;

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

}
