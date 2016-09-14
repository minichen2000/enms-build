package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.adapterserver.api.StatisticsApiService;
import com.nsb.enms.restful.dbclient.ApiException;
import com.nsb.enms.restful.dbclient.api.DbStatisticsApi;
import com.nsb.enms.restful.model.NeStatistics;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class StatisticsApiServiceImpl extends StatisticsApiService {
	private DbStatisticsApi statisticsApi = new DbStatisticsApi();

	@Override
	public Response getNeNumbersPerType(SecurityContext securityContext) throws NotFoundException {
		List<NeStatistics> neStatisticsList = new ArrayList<NeStatistics>();
		try {
			neStatisticsList = statisticsApi.getNeNumbersPerType();
		} catch (ApiException e) {
			e.printStackTrace();
			Response.serverError().entity(e).build();
		}
		return Response.ok().entity(neStatisticsList).build();
	}

	@Override
	public Response getSupportedNeTypes(SecurityContext securityContext) throws NotFoundException {
		List<NeStatistics> neStatisticsList = new ArrayList<NeStatistics>();
		try {
			neStatisticsList = statisticsApi.getSupportedNeTypes();
		} catch (ApiException e) {
			e.printStackTrace();
			Response.serverError().entity(e).build();
		}
		return Response.ok().entity(neStatisticsList).build();
	}

}
