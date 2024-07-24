package com.sparta.itsmine.global.exception.user;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class UserNotDeletedException extends UserException{

	public UserNotDeletedException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
