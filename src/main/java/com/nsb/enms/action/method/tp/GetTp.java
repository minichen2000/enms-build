package com.nsb.enms.action.method.tp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.action.common.CommonConstants;
import com.nsb.enms.action.common.conf.ConfLoader;
import com.nsb.enms.action.common.conf.ConfigKey;
import com.nsb.enms.action.entity.TpEntity;
import com.nsb.enms.action.method.ExecExternalScript;
import com.nsb.enms.action.util.JsonUtils;
import com.nsb.enms.action.util.ParseUtil;

public class GetTp
{
    private static final Logger log = LogManager.getLogger( GetTp.class );

    private static final String SCENARIO = ConfLoader.getInstance()
            .getConf( ConfigKey.PORT_GET_REQ, CommonConstants.PORT_GET_REQ );

    public String getTp( String groupId, String neId )
    {
        try
        {
            log.debug(
                "start to getTp with groupId:" + groupId + ", neId:" + neId );
            Process process = new ExecExternalScript().run( SCENARIO, groupId,
                neId );
            InputStream inputStream = process.getInputStream();
            List<TpEntity> tpList = new LinkedList<TpEntity>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line = null;
            while( (line = br.readLine()) != null )
            {
                if( line.indexOf( "GetReply received" ) >= 0 )
                {
                    TpEntity portEntity = new TpEntity();
                    while( (line = br.readLine()) != null )
                    {
                        line = line.trim();
                        if( line.startsWith( "managedObjectClass" ) )
                        {
                            String moc = ParseUtil
                                    .parseAttrWithSingleValue( line );
                            portEntity.setMoc( moc );
                            continue;
                        }
                        if( line.startsWith( "managedObjectInstance" ) )
                        {
                            String moi = ParseUtil
                                    .parseAttrWithMultiValue( line );
                            portEntity.setMoi( moi );
                        }

                        if( line.startsWith( "userLabel" ) )
                        {
                            portEntity.setUserLabel(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "stmLevel" ) )
                        {
                            portEntity.setStmLevel( Integer
                                    .parseInt( ParseUtil.parseAttr( line ) ) );
                            continue;
                        }

                        if( line.startsWith( "alarmStatus" ) )
                        {
                            portEntity.setAlarmStatus(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "administrativeState" ) )
                        {
                            portEntity.setAdministrativeState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "supportedByObjectList" ) )
                        {
                            portEntity.setSupportedByObjectList(
                                ParseUtil.parseAttrWithMultiValue( line ) );
                            continue;
                        }

                        if( line.startsWith( "operationalState" ) )
                        {
                            portEntity.setOperationalState(
                                ParseUtil.parseAttr( line ) );
                            continue;
                        }

                        if( line.startsWith( "-----------------" ) )
                        {
                            tpList.add( portEntity );
                            break;
                        }
                    }
                }
            }
            br.close();

            if( process.waitFor() != 0 || tpList.size() < 1 )
            {
                log.error(
                    "Get tp failed, groupId:" + groupId + ", neId:" + neId );
            }
            return JsonUtils.toJson( tpList );
        }
        catch( IOException e )
        {
            log.error( e.getMessage(), e );
        }
        catch( InterruptedException e )
        {
            log.error( e.getMessage(), e );
        }

        return null;
    }
}
