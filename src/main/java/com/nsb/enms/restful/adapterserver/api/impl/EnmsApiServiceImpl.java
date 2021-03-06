package com.nsb.enms.restful.adapterserver.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapterserver.api.EnmsApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-09T15:45:26.159+08:00")
public class EnmsApiServiceImpl extends EnmsApiService {
	private static final Logger log = LogManager.getLogger(EnmsApiServiceImpl.class);

	@Override
	public Response ping(SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity("ok").build();
	}

	/*
	 * @Override public Response unAssignNe(Ne body, SecurityContext
	 * securityContext) throws NotFoundException { // do some magic! return
	 * Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK,
	 * "magic!")).build(); }
	 * 
	 * @Override public Response assignNes(NeAssignment body, SecurityContext
	 * securityContext) throws NotFoundException { for (Ne ne :
	 * body.getNeList()) { //StartSuppervision suppervision = new
	 * StartSuppervision(); try { NeExtraInfo extInfo = ne.getExtraInfo();
	 * String moi = StringUtils.EMPTY; moi = extInfo.getMoi();
	 * 
	 * String groupId = moi.split("/")[0].replaceAll("neGroupId=",
	 * StringUtils.EMPTY); String neId =
	 * moi.split("/")[1].replaceAll("networkElementId=", StringUtils.EMPTY);
	 * 
	 * boolean isSuccess =
	 * StartSuppervision.startSuppervision(Integer.valueOf(groupId),
	 * Integer.valueOf(neId)); log.debug("isSuccess = " + isSuccess); if
	 * (!isSuccess) { return Response.serverError().entity(
	 * "faild to suppervision").build(); } } catch (AdapterException e) {
	 * log.error("assignNE", e); return
	 * Response.serverError().entity(e).build(); } }
	 * 
	 * return Response.ok().build(); }
	 * 
	 * @Override public Response getNeAssignment(SecurityContext
	 * securityContext) throws NotFoundException { return null; }
	 */
}
