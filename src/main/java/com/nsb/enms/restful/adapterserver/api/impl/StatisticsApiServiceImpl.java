package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.nsb.enms.adapter.server.api.NotFoundException;
import com.nsb.enms.adapter.server.api.StatisticsApiService;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.StatisticsApi;
import com.nsb.enms.restful.db.client.model.NESTATISTICS;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class StatisticsApiServiceImpl extends StatisticsApiService {
	private StatisticsApi statisticsApi = new StatisticsApi();

	@Override
	public Response statisticsNesGet(SecurityContext securityContext) throws NotFoundException {
		List<NESTATISTICS> neStatisticsList = new ArrayList<NESTATISTICS>();
		try {
			neStatisticsList = statisticsApi.statisticsNesGet();
		} catch (ApiException e) {
			e.printStackTrace();
			Response.serverError().entity(e).build();
		}
		return Response.ok().entity(neStatisticsList).build();
	}

	@Override
	public Response statisticsSupportedNeTypesGet(SecurityContext securityContext) throws NotFoundException {
		List<NESTATISTICS> neStatisticsList = new ArrayList<NESTATISTICS>();
		try {
			neStatisticsList = statisticsApi.statisticsSupportedNeTypesGet();
		} catch (ApiException e) {
			e.printStackTrace();
			Response.serverError().entity(e).build();
		}
		return Response.ok().entity(neStatisticsList).build();
	}
}
