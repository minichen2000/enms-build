package com.nsb.enms.restful.server.ne;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class PostNe {

	@POST
	@Path("/nes")
	@Produces(MediaType.APPLICATION_JSON)
	public void addNes(String nes) {
		System.out.println(nes);
	}
}
