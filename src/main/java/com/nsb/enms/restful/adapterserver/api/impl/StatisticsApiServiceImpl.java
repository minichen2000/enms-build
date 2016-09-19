package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.db.mgr.StatisticsDbMgr;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.adapterserver.api.StatisticsApiService;
import com.nsb.enms.restful.model.adapter.NeStatistics;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class StatisticsApiServiceImpl extends StatisticsApiService {
	private final static Logger log = LogManager.getLogger(StatisticsApiServiceImpl.class);
	private StatisticsDbMgr statisticsDbMgr = new StatisticsDbMgr();

	@Override
	public Response getNeNumbersPerType(SecurityContext securityContext) throws NotFoundException {
		List<NeStatistics> neStatisticsList = new ArrayList<NeStatistics>();
		try {
			neStatisticsList = statisticsDbMgr.getNeNumbersPerType();
		} catch (Exception e) {
			log.error("getNeNumbersPerType", e);
			Response.serverError().entity(e).build();
		}
		return Response.ok().entity(neStatisticsList).build();
	}

	@Override
	public Response getSupportedNeTypes(SecurityContext securityContext) throws NotFoundException {
		List<NeStatistics> neStatisticsList = new ArrayList<NeStatistics>();
		try {
			neStatisticsList = statisticsDbMgr.getSupportedNeTypes();
		} catch (Exception e) {
			log.error("getSupportedNeTypes", e);
			Response.serverError().entity(e).build();
		}
		return Response.ok().entity(neStatisticsList).build();
	}

}
