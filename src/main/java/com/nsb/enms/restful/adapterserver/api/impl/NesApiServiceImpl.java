package com.nsb.enms.restful.adapterserver.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.NeEntity;
import com.nsb.enms.adapter.server.action.method.ne.CreateNe;
import com.nsb.enms.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.adapter.server.business.SyncTpThread;
import com.nsb.enms.adapter.server.common.TYPES;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.util.GenerateKeyOnNeUtils;
import com.nsb.enms.adapter.server.db.mgr.EquipmentsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.NesDbMgr;
import com.nsb.enms.adapter.server.db.mgr.TpsDbMgr;
import com.nsb.enms.adapter.server.db.mgr.XcsDbMgr;
import com.nsb.enms.restful.adapterserver.api.ApiResponseMessage;
import com.nsb.enms.restful.adapterserver.api.NesApiService;
import com.nsb.enms.restful.adapterserver.api.NotFoundException;
import com.nsb.enms.restful.model.adapter.Addresses;
import com.nsb.enms.restful.model.adapter.AdpNe;
import com.nsb.enms.restful.model.adapter.Q3Address;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-29T17:16:31.406+08:00")
public class NesApiServiceImpl extends NesApiService
{
    private final static Logger log = LogManager
            .getLogger( NesApiServiceImpl.class );

    private NesDbMgr nesDbMgr = new NesDbMgr();

    @Override
    public Response addNe( AdpNe body, SecurityContext securityContext )
            throws NotFoundException
    {
        String location = body.getLocationName();
        NeEntity entity = null;
        String id = "";
        try
        {
            Addresses address = body.getAddresses();
            id = address.getQ3Address().getAddress();
            entity = CreateNe.createNe( body.getVersion(), body.getNeType(),
                body.getUserLabel(), location, id );
        }
        catch( AdapterException e )
        {
            log.error( "create ne occur error", e );
            return Response.serverError().entity( e ).build();
        }

        if( null == entity )
        {
            return Response.serverError()
                    .entity( new ApiResponseMessage( ApiResponseMessage.ERROR,
                            "failed to create ne" ) )
                    .build();
        }

        log.debug( entity );

        String moi = entity.getMoi();
        String groupId = moi.split( "/" )[0].replaceAll( "neGroupId=",
            StringUtils.EMPTY );
        String neId = moi.split( "/" )[1].replaceAll( "networkElementId=",
            StringUtils.EMPTY );

        AdpNe ne = constructNe( entity, id );

        try
        {
            ne = nesDbMgr.addNe( ne );
        }
        catch( Exception e )
        {
            log.error( "addNe", e );
            return Response.serverError().entity( e ).build();
        }

        // new thread
        new SyncTpThread( Integer.valueOf( groupId ), Integer.valueOf( neId ),
                ne.getId() ).start();

        log.debug( "adapter----------------addNe----------end" );

        return Response.ok().entity( ne ).build();
    }

    private AdpNe constructNe( NeEntity entity, String id )
    {
        AdpNe ne = new AdpNe();
        ne.setId( id );
        ne.setKeyOnNe( GenerateKeyOnNeUtils.generateKeyOnNe( TYPES.NE, entity.getMoc(),
            entity.getMoi() ) );
        ne.setUserLabel( entity.getUserLabel() );
        ne.setVersion( entity.getNeRelease() );

        Addresses address = new Addresses();
        Q3Address q3Address = new Q3Address();
        q3Address.setAddress( entity.getNetworkAddress() );
        address.setQ3Address( q3Address );
        address.setTl1Address( new ArrayList<String>() );
        address.setSnmpAddress( null );
        ne.setAddresses( address );

        ne.setNeType( entity.getNeType() );
        ne.setOperationState( "disable" );
        ne.setAdminState( entity.getAdministrativeState() );
        return ne;
    }

    @Override
    public Response deleteNe( String neid, SecurityContext securityContext )
            throws NotFoundException
    {
        log.debug( "adapter------deleteNE" );
        try
        {
            AdpNe ne = nesDbMgr.getNeById( neid );
            log.debug( "ne = " + ne );

            String moi = StringUtils.EMPTY;
            moi = GenerateKeyOnNeUtils.getNeMoi( ne.getKeyOnNe() );

            if( StringUtils.isEmpty( moi ) )
            {
                return Response.serverError().build();
            }
            String groupId = moi.split( "/" )[0].replaceAll( "neGroupId=",
                StringUtils.EMPTY );
            String neId = moi.split( "/" )[1].replaceAll( "networkElementId=",
                StringUtils.EMPTY );

            log.debug( "groupId = " + groupId + ", neId = " + neId );

            DeleteNe.deleteNe( Integer.valueOf( groupId ),
                Integer.valueOf( neId ) );

            // delete db record, contains ne and tp
            nesDbMgr.deleteNe( neid );

            XcsDbMgr xcsDbMgr = new XcsDbMgr();
            xcsDbMgr.deleteXcsByNeId( neId );

            TpsDbMgr tpsDbMgr = new TpsDbMgr();
            tpsDbMgr.deleteTpsbyNeId( neId );

            EquipmentsDbMgr equipmentsDbMgr = new EquipmentsDbMgr();
            equipmentsDbMgr.deleteEquipmentsByNeId( neId );
        }
        catch( Exception e )
        {
            log.error( "deleteNE", e );
            return Response.serverError().entity( e ).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response getNeById( String neid, SecurityContext securityContext )
            throws NotFoundException
    {
        AdpNe ne = new AdpNe();
        try
        {
            ne = nesDbMgr.getNeById( neid );
        }
        catch( Exception e )
        {
            log.error( "getNeById", e );
            return Response.serverError().entity( e ).build();
        }
        return Response.ok().entity( ne ).build();
    }

    @Override
    public Response updateNe( AdpNe body, SecurityContext securityContext )
            throws NotFoundException
    {
        try
        {
            nesDbMgr.updateNe( body );
        }
        catch( Exception e )
        {
            log.error( "updateNe", e );
            return Response.serverError().entity( e ).build();
        }
        return Response.ok().build();
    }    

    @Override
    public Response getNes( SecurityContext securityContext )
            throws NotFoundException
    {
        log.debug( "adapter-------nesGet" );
        List<AdpNe> nes = new ArrayList<AdpNe>();
        try
        {
            nes = nesDbMgr.getNes();
        }
        catch( Exception e )
        {
            log.error( "nesGet", e );
            return Response.serverError().entity( e ).build();
        }
        return Response.ok().entity( nes ).build();
    }
}
