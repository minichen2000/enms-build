package com.nsb.enms.restful.server.equipment;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class PostEquipment {

	@POST
	@Path("/equipments")
	@Produces(MediaType.APPLICATION_JSON)
	public void addEquipments(String equipments) {
	}
}
