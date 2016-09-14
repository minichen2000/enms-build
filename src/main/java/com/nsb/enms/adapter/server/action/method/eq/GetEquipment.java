package com.nsb.enms.adapter.server.action.method.eq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.adapter.server.common.util.ParseUtils;

public class GetEquipment
{
    private static final Logger log = LogManager
            .getLogger( GetEquipment.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.GET_EQ_REQ, ConfigKey.DEFAULT_GET_EQ_REQ );

    public static List<EquipmentEntity> getEquipment( int groupId, int neId )
            throws AdapterException
    {
        try
        {
            Process process = new ExecExternalScript().run(
                ExternalScriptType.TSTMGR, SCENARIO, String.valueOf( groupId ),
                String.valueOf( neId ) );

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
                            String moc = ParseUtils
                                    .parseAttrWithSingleValue( line );
                            equipmentEntity.setMoc( moc );
                            continue;
                        }

                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtils
                                    .parseAttrWithMultiValue( line );
                            equipmentEntity.setMoi( moi );
                            continue;
                        }

                        if( line.startsWith( "equipmentActual" ) )
                        {
                            equipmentEntity.setEquipmentActual(
                                ParseUtils.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "equipmentExpected" ) )
                        {
                            equipmentEntity.setEquipmentExpected(
                                ParseUtils.parseAttr1( line ) );
                            continue;
                        }

                        if( line.startsWith( "version" ) )
                        {
                            equipmentEntity
                                    .setVersion( ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "availabilityStatus" ) )
                        {
                            equipmentEntity.setAvailabilityStatus(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "alarmStatus" ) )
                        {
                            equipmentEntity.setAlarmStatus(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "locationName" ) )
                        {
                            equipmentEntity.setLocationName(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "operationalState" ) )
                        {
                            equipmentEntity.setOperationalState(
                                ParseUtils.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            equipmentEntity.setAdministrativeState(
                                ParseUtils.parseAttr( line ) );
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
            log.error( "getEquipment", e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
    }
}