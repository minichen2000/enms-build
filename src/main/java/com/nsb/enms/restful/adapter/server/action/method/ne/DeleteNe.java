package com.nsb.enms.restful.adapter.server.action.method.ne;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.adapter.server.manager.Q3EmlImMgr;
import com.nsb.enms.restful.adapter.server.util.CommonConstants;

public class DeleteNe
{
    private final static Logger log = LogManager.getLogger( DeleteNe.class );

    private static String deleteNeScenario = ConfLoader.getInstance().getConf(
        ConfigKey.NE_DELETE_REQ, ConfigKey.DEFAULT_NE_DELETE_REQ );

    private static String stopSupervisionScenario = ConfLoader.getInstance()
            .getConf( ConfigKey.STOP_SUPERVISION_REQ,
                ConfigKey.DEFAULT_STOP_SUPERVISION_REQ );

    public boolean deleteNe( int groupId, int neId ) throws AdapterException
    {
        try
        {
            Process process = new ExecExternalScript().run(
                CommonConstants.TSTMGR_SCRIPT_TYPE, stopSupervisionScenario,
                groupId + "", neId + "" );

            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader( inputStream ) );
            String line;
            boolean flag = false;
            while( (line = br.readLine()) != null )
            {
                if( line.contains( "ActionReply received" ) )
                {
                    flag = true;
                }
            }
            br.close();

            if( process.waitFor() != 0 || !flag )
            {
                throw new AdapterException(
                        AdapterExceptionType.EXCPT_INTERNAL_ERROR,
                        "Stop supervision failed!!!" );
            }

            if( flag )
            {
                process = new ExecExternalScript().run(
                    CommonConstants.TSTMGR_SCRIPT_TYPE, deleteNeScenario,
                    groupId + "", neId + "" );
                inputStream = process.getInputStream();
                br = new BufferedReader( new InputStreamReader( inputStream ) );
                flag = false;
                while( (line = br.readLine()) != null )
                {
                    if( line.contains( "DeleteReply received" ) )
                    {
                        flag = true;
                    }
                }
                br.close();

                if( process.waitFor() != 0 || !flag )
                {
                    return false;
                }
                // Q3EmlImMgr.getInstance().removeNe( groupId, neId );
                return true;
            }
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
            throw new AdapterException(
                    AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage() );
        }
        return false;
    }
}
