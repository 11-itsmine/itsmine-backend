package com.sparta.itsmine.global.exception.user;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;

public class UserDeletedException extends UserException{

	public UserDeletedException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
