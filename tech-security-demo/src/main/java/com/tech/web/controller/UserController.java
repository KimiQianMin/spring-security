/**
 * 
 */
package com.tech.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.tech.dto.User;
import com.tech.dto.UserQueryCondition;
import com.tech.exception.UserNotExistException;
import com.tech.security.core.properties.SecurityProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author attpnxg1
 *
 */
@RestController
@RequestMapping("/user")
@EnableSwagger2
public class UserController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SecurityProperties securityProperties;

	@PutMapping("/{id:\\d+}")
	public User update(@Valid @RequestBody User user, BindingResult bingdingResult) {

		if (bingdingResult.hasErrors()) {
			bingdingResult.getAllErrors().stream().forEach(error -> {
				FieldError fieldError = (FieldError) error;
				String message = fieldError.getField() + " " + error.getDefaultMessage();
				System.out.println(message);
			});
		}

		System.out.println(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

		user.setId("1");
		return user;
	}

	@PostMapping
	public User create(@Valid @RequestBody User user, BindingResult bingdingResult) {

		if (bingdingResult.hasErrors()) {
			bingdingResult.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
		}

		System.out.println(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

		user.setId("1");
		return user;
	}

	// @RequestMapping(value = "/user", method = RequestMethod.GET)
	// public List<User> quest(@RequestParam(name = "userName", required =
	// false, defaultValue = "Kimi") String userName) {
	@GetMapping
	@JsonView(User.UserSimpleView.class)
	@ApiOperation(value="User Query Service")
	public List<User> query(@ApiParam(value="Condition of User Query") UserQueryCondition condition) {

		System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}

	// @RequestMapping(value = "/user/{id:\\d+}", method = RequestMethod.GET)
	@GetMapping("/{id:\\d+}")
	@JsonView(User.UserDetailView.class)
	public User getInfo(@PathVariable String id) {

		System.out.println("getInfo is starting...");

		User user = new User();
		user.setUserName("Kimi");
		return user;
	}
	
	@GetMapping("/getGroupInfo")
	public String getGroupInfo(){
		return "getGroupInfo()";
	}

	@DeleteMapping("/{id:\\d+}")
	public void delete(@PathVariable String id) {
		// System.out.println(id);

		//throw new UserNotExistException("id");
	}

	// @GetMapping("/me")
	// public Object getCurrentUser(Authentication userDetails) {
	// return userDetails;
	// }

	@GetMapping("/me")
	public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetails, Authentication authentication,
			HttpServletRequest request) throws UnsupportedEncodingException {
		// Authentication authentication1 =
		// SecurityContextHolder.getContext().getAuthentication();
		// Authorization : bearer
		// eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55IjoiaW1vb2MiLCJ1c2VyX25hbWUiOiJhZG1pbiIsImp0aSI6ImRjYzVmODIwLWUwNmYtNDYyNi1hYmMyLTAyZTljZjdkZjhmOCIsImNsaWVudF9pZCI6Im15aWQiLCJzY29wZSI6WyJhbGwiXX0.nYFBXcLBN3WNef0sooNxS0s6CaEleDGfjZh7xtTEqf4
		// 增加了jwt之后，获取传递过来的token
		// 当然这里只是其中一种的 token的传递方法，自己要根据具体情况分析

		String authorization = request.getHeader("Authorization");
		String token = StringUtils.substringAfter(authorization, "bearer ");
		logger.info("jwt token", token);
		String jwtSigningKey = securityProperties.getOauth2().getJwtSigningKey();
		// 生成的时候使用的是
		// org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
		// 源码里面把signingkey变成utf8了
		// JwtAccessTokenConverter类，解析出来是一个map
		// 所以这个自带的JwtAccessTokenConverter对象也是可以直接用来解析的
		byte[] bytes = jwtSigningKey.getBytes("utf-8");
		Claims payload = Jwts.parser().setSigningKey(bytes).parseClaimsJws(token).getBody();

		String company = (String) payload.get("company");

		logger.info("company - {}", company);

		return authentication;
	}

}
