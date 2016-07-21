package com.nsb.enms.restful.server.tp;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Path("")
public class GetTp {
	private static final Logger log = LogManager.getLogger(GetTp.class);

	@GET
	@Path("/tps/ne/{neId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTpByNeId(@PathParam("neId") String neId) {
		return StringUtils.EMPTY;
	}

	@GET
	@Path("/tps/{tpId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTpByPortId(@PathParam("tpId") String tpId) {
		return StringUtils.EMPTY;
	}
}
