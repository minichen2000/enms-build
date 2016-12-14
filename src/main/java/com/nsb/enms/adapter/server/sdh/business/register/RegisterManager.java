package com.nsb.enms.adapter.server.sdh.business.register;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.controllerclient.ApiException;
import com.nsb.enms.restful.controllerclient.api.CtlSystemApi;
import com.nsb.enms.restful.model.controller.Host;

public class RegisterManager
{
    private static final Logger log = LogManager
            .getLogger( RegisterManager.class );

    private CtlSystemApi systemApi = new CtlSystemApi();

    public boolean register2Controller()
    {
        Host host = constructAdapterHost();

        try
        {
            systemApi.registerHost( host );
        }
        catch( Exception e )
        {
            log.error( "register2Controller", e );
            return false;
        }
        return true;
    }

    public void unRegister2Controller()
    {
        Host host = constructAdapterHost();

        try
        {
            systemApi.unRegisterHost( host );
        }
        catch( ApiException e )
        {
            log.error( "unRegister2Controller", e );
        }
    }

    private Host constructAdapterHost()
    {
        Host host = new Host();
        host.setType( "Adapter" );
        String id = ConfLoader.getInstance().getConf( ConfigKey.ADP_ID,
            "adapter_" + System.currentTimeMillis() );
        host.setId( id );
        String ip = ConfLoader.getInstance().getConf( ConfigKey.ADP_IP,
            "127.0.0.1" );
        int port = ConfLoader.getInstance().getInt( ConfigKey.ADP_PORT,
            ConfigKey.DEFAULT_ADP_PORT );
        host.setIpAddress( ip + ":" + port );
        String q3Address = ConfLoader.getInstance()
                .getConf( ConfigKey.ADP_ADDRESS, "" );
        host.setQ3Address( q3Address );
        host.setRemark( "" );
        return host;
    }
}
