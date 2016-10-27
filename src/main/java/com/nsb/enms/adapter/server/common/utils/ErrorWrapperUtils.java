package com.nsb.enms.adapter.server.common.utils;

import javax.ws.rs.core.Response;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpErrorInfo;

public class ErrorWrapperUtils {
	public static Response failDbOperation() {
		AdpErrorInfo errorInfo = new AdpErrorInfo();
		errorInfo.setCode(ErrorCode.FAIL_DB_OPERATION.getErrorCode());
		errorInfo.setMessage(ErrorCode.FAIL_DB_OPERATION.getMessage());
		return Response.serverError().entity(errorInfo).build();
	}

	public static Response adapterException(AdapterException e) {
		AdpErrorInfo errorInfo = new AdpErrorInfo();
		errorInfo.setCode(e.errorCode_);
		errorInfo.setMessage(e.errorReason_);
		return Response.serverError().entity(errorInfo).build();
	}
}
