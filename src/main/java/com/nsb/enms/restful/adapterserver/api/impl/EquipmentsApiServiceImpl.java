package com.nsb.enms.restful.adapterserver.api.impl;

import com.nsb.enms.adapter.server.db.mgr.AdpEqusDbMgr;
import com.nsb.enms.restful.adapterserver.api.EquipmentsApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.AdpEquipment;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class EquipmentsApiServiceImpl extends EquipmentsApiService {
    
    private static final Logger log = LogManager.getLogger( EquipmentsApiServiceImpl.class );
    
    private AdpEqusDbMgr equsDbMgr = new AdpEqusDbMgr();
    
    @Override
    public Response getEquipmentById( String equId, SecurityContext securityContext )
            throws NotFoundException
    {
        AdpEquipment equipment = new AdpEquipment();
        try {
            equipment = equsDbMgr.getEquipmentById(equId);
        } catch (Exception e) {
            log.error("getEquipmentById", e);
            return Response.serverError().entity(e).build();
        }
        return Response.ok().entity(equipment).build();
    }
    @Override
    public Response getEquipmentsByNeId( String neId, SecurityContext securityContext )
            throws NotFoundException
    {
        List<AdpEquipment> equList = new ArrayList<AdpEquipment>();
        try {
            equList = equsDbMgr.getEquipmentsByNeId(neId);
        } catch (Exception e) {
            log.error("getEquipmentsByNeId", e);
            return Response.serverError().entity(e).build();
        }
        return Response.ok().entity(equList).build();
    }
}
