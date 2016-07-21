package com.nsb.enms.restful.server.ne;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("")
public class DeleteNe {
	private static final Logger log = LogManager.getLogger(DeleteNe.class);

	@DELETE
	@Path("/nes/{neId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteNeById(@PathParam("neId") String neId) {
		System.out.println(neId);
		return StringUtils.EMPTY;
	}
}
