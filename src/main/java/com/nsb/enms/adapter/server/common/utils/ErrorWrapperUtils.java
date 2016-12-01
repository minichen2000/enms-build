package com.nsb.enms.adapter.server.common.utils;

import javax.ws.rs.core.Response;

import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.restful.model.adapter.AdpErrorInfo;

public class ErrorWrapperUtils {
	public static Response failDbOperation() {
		AdpErrorInfo errorInfo = new AdpErrorInfo();
		errorInfo.setCode(ErrorCode.FAIL_DB_OPERATION.name());
		errorInfo.setMessage(ErrorCode.FAIL_DB_OPERATION.getDescription());
		return Response.serverError().entity(errorInfo).build();
	}

	public static Response adapterException(AdapterException e) {
		AdpErrorInfo errorInfo = new AdpErrorInfo();
		errorInfo.setCode(e.errorCode.name());
		errorInfo.setMessage(e.errorReason_);
		return Response.serverError().entity(errorInfo).build();
	}

	public static Response constructErrorInfo(ErrorCode errorCode) {
		AdpErrorInfo errorInfo = new AdpErrorInfo();
		errorInfo.setCode(errorCode.name());
		errorInfo.setMessage(errorCode.getDescription());
		return Response.serverError().entity(errorInfo).build();
	}
}
