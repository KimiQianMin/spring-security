/**
 * 
 */
package com.tech.dto;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;
import com.tech.validator.MyConstraint;

/**
 * @author attpnxg1
 *
 */
public class User {

	public interface UserSimpleView {
	};

	public interface UserDetailView extends UserSimpleView {
	};

	private String id;

	@MyConstraint(message = "this is testing for userName validation")
	private String userName;

	@NotBlank(message = "password can't be empty")
	private String password;

	@Past(message = "birthday should be past day")
	private Date birthday;

	@JsonView(UserSimpleView.class)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonView(UserSimpleView.class)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonView(UserDetailView.class)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonView(UserSimpleView.class)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
