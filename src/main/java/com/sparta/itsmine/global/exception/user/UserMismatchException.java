package com.sparta.itsmine.global.exception.user;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;

public class UserMismatchException extends UserException {

	public UserMismatchException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
