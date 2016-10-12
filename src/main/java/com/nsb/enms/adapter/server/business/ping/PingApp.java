package com.nsb.enms.adapter.server.business.ping;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.manager.Q3EmlImMgr;
import com.nsb.enms.restful.controllerclient.ApiException;
import com.nsb.enms.restful.controllerclient.api.CtlSystemApi;

public class PingApp
{
    private static final Logger log = LogManager.getLogger( PingApp.class );

    private CtlSystemApi systemApi = new CtlSystemApi();

    private static final int MAX_COUNT = ConfLoader.getInstance().getInt(
        ConfigKey.ADP_PING_MAX_NUM, ConfigKey.DEFAULT_ADP_PING_MAX_NUM );

    private static int count = 0;

    public void checkPing()
    {
        Timer timer = new Timer();
        long time = ConfLoader.getInstance().getInt(
            ConfigKey.ADP_PING_INTERVAL, ConfigKey.DEFAULT_ADP_PING_INTERVAL );
        timer.scheduleAtFixedRate( new Task(), time, time );
    }

    class Task extends TimerTask
    {

        @Override
        public void run()
        {
            ping();
        }

        private void ping()
        {
            try
            {
                systemApi.ping();
                count = 0;
                log.debug( "controller is in service" );
            }
            catch( ApiException e )
            {
                log.error( "controller is out of service" );
                count++;
                if( count < MAX_COUNT )
                {
                    ping();
                }
                else
                {
                    Q3EmlImMgr.instance().destroy();
                }
            }
        }
    }
}
