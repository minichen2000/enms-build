package com.nsb.enms.restful.server.tp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("")
public class GetCtp {
	private static final Logger log = LogManager.getLogger(GetCtp.class);

	@GET
	@Path("/ctps/tp/{tpId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCtpByTpId(@PathParam("tpId") String tpId) {
		return StringUtils.EMPTY;
	}

	@GET
	@Path("/ctps/potential/tp/{tpId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPotentialCtpByTpId(@PathParam("tpId") String tpId) {
		return StringUtils.EMPTY;
	}
}
