package com.nsb.enms.adapter.server.business.heartbeat;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.register.RegisterManager;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.controllerclient.ApiException;
import com.nsb.enms.restful.controllerclient.api.CtlSystemApi;

public class HeartBeatManager
{
    private static final Logger log = LogManager
            .getLogger( HeartBeatManager.class );

    private CtlSystemApi systemApi = new CtlSystemApi();

    private static int count = 0;

    private static boolean flag = true;

    private final static int MAX_COUNT = ConfLoader.getInstance()
            .getInt( "MAX_HB_NUM", 5 );

    public void checkHeartbeat()
    {
        Timer timer = new Timer();
        long time = ConfLoader.getInstance().getInt(
            ConfigKey.ADP_HEARTBEAT_INTERVAL,
            ConfigKey.DEFAULT_ADP_HEARTBEAT_INTERVAL );
        timer.scheduleAtFixedRate( new Task(), time, time );
    }

    class Task extends TimerTask
    {

        @Override
        public void run()
        {
            checkHeartBeat();
        }

        public void checkHeartBeat()
        {
            try
            {
                systemApi.checkHeartbeat();
                count = 0;
                if( !flag )
                {
                    RegisterManager registerManager = new RegisterManager();
                    registerManager.register2Controller();
                    flag = true;
                }
                log.debug( "controller is in service" );
            }
            catch( ApiException e )
            {
                log.error( "controller is out of service" );
                if( count < MAX_COUNT )
                {
                    count++;
                    flag = false;
                    checkHeartBeat();
                }
            }
        }
    }
}
