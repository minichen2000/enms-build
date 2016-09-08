package com.nsb.enms.restful.adapter.server.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.api.NotFoundException;
import com.nsb.enms.restful.adapter.server.api.TerminationsApiService;
import com.nsb.enms.restful.adapter.server.business.sync.TerminateTpMgr;
import com.nsb.enms.restful.db.client.model.TP;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T16:19:02.183+08:00")
public class TerminationsApiServiceImpl extends TerminationsApiService {
	private final static Logger log = LogManager.getLogger(TerminationsApiServiceImpl.class);

	@Override
	public Response terminateCTP(String ctpid, SecurityContext securityContext) throws NotFoundException {
		List<TP> tpList = new ArrayList<TP>();
		TerminateTpMgr mgr = new TerminateTpMgr(ctpid);
		log.debug("start to terminate tp by ctpId = {}", ctpid);
		tpList = mgr.run();
		log.debug("successed to terminate tp by ctpId = {}", ctpid);
		return Response.ok().entity(tpList).build();
	}
}
