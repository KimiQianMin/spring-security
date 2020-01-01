package com.tech.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tech.exception.UserNotExistException;

@ControllerAdvice
public class ControllerExceptionHandler {

	/**
	 * customized the error message for exception
	 */
	@ExceptionHandler(UserNotExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> handleUserNotExistException(UserNotExistException ux) {
		Map<String, Object> resullt = new HashMap<>();
		resullt.put("id", ux.getId());
		resullt.put("message", ux.getMessage());
		return resullt;
	}
}
