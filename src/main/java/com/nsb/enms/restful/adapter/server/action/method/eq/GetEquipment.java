package com.nsb.enms.restful.adapter.server.action.method.eq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.adapter.server.util.CommonConstants;
import com.nsb.enms.restful.adapter.server.util.ParseUtil;

public class GetEquipment
{
    private static final Logger log = LogManager
            .getLogger( GetEquipment.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.EQ_GET_REQ, ConfigKey.DEFAULT_EQ_GET_REQ );

    public List<EquipmentEntity> getEquipment( int groupId, int neId )
            throws AdapterException
    {
        try
        {
            Process process = new ExecExternalScript().run(
                CommonConstants.TSTMGR_SCRIPT_TYPE, SCENARIO, groupId + "",
                neId + "" );

            InputStream inputStream = process.getInputStream();
            List<EquipmentEntity> eqList = new LinkedList<EquipmentEntity>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );

            String line = null;

            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "GetReply received" ) >= 0 )
                {
                    EquipmentEntity equipmentEntity = new EquipmentEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.indexOf( "managedObjectClass" ) >= 0 )
                        {
                            String moc = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            equipmentEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtil
                                    .parseAttrWithMultiValue( line );
                            equipmentEntity.setMoi( moi );
                            continue;
                        }

                        if( line.startsWith( "equipmentActual" ) )
                        {
                            equipmentEntity.setEquipmentActual(
                                ParseUtil.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "equipmentExpected" ) )
                        {
                            equipmentEntity.setEquipmentExpected(
                                ParseUtil.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "version" ) )
                        {
                            equipmentEntity
                                    .setVersion( ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "availabilityStatus" ) )
                        {
                            equipmentEntity.setAvailabilityStatus(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "alarmStatus" ) )
                        {
                            equipmentEntity.setAlarmStatus(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "locationName" ) )
                        {
                            equipmentEntity.setLocationName(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "operationalState" ) )
                        {
                            equipmentEntity.setOperationalState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            equipmentEntity.setAdministrativeState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "-----------------" ) )
                        {
                            eqList.add( equipmentEntity );
                            break;
                        }
                    }
                }
            }
            br.close();

            if( process.waitFor() != 0 || eqList.size() < 1 )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Get equipment failed!!!" );
            }
            return eqList;
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}
