package com.nsb.enms.restful.server.connection;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class PostXc {

	@POST
	@Path("/xcs")
	@Produces(MediaType.APPLICATION_JSON)
	public void addConnections(String connections) {
	}
}
