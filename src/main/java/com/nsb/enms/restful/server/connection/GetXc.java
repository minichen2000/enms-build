package com.nsb.enms.restful.server.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

@Path("")
public class GetXc {
	private static final Logger log = LogManager.getLogger(GetXc.class);

	@GET
	@Path("/xcs/ne/{neId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getXcsByNeId(@PathParam("neId") String neId) {
		return StringUtils.EMPTY;
	}
}
