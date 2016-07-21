package com.nsb.enms.restful.server.equipment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Path("")
public class GetEquipment {
	private static final Logger log = LogManager.getLogger(GetEquipment.class);

	@GET
	@Path("/equipments/ne/{neId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getEquipmentById(@PathParam("neid") String neId) {
		return StringUtils.EMPTY;
	}
}
