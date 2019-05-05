package com.likuncheng.base;


import org.springframework.stereotype.Component;

import com.likuncheng.constants.Constants;


@Component
public class BaseApiService {
	
	
	public ResponseBase setResultError(Integer code,String msg) {
		return setResult(code, msg, null);
	}
	
	public ResponseBase setResultError(String msg) {
	// 返回错误，可以传msg
		return setResult(Constants.HTTP_RES_CODE_500, msg, null);
	}

	// 返回成功，可以传data值
	public ResponseBase setResultSuccessData(Object data) {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
	}
	
	public ResponseBase setResultSuccessData(Object data,String msg) {
		return setResult(Constants.HTTP_RES_CODE_200, msg, data);
	}

	// 返回成功，沒有data值
	public ResponseBase setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
	}

	// 返回成功，沒有data值
	public ResponseBase setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200, msg, null);
	}

	public ResponseBase setResult(Integer code, String msg, Object data) {
		return new ResponseBase(code, msg, data);
	}

}
