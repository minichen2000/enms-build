package com.nsb.enms.restful.adapterserver.api.factories;

import com.nsb.enms.restful.adapterserver.api.EnmsApiService;
import com.nsb.enms.restful.adapterserver.api.impl.EnmsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-09T15:45:26.159+08:00")
public class EnmsApiServiceFactory {
	private final static EnmsApiService service = new EnmsApiServiceImpl();

	public static EnmsApiService getEnmsApi() {
		return service;
	}
}
