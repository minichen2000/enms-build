package com.nsb.enms.adapter.server.business.heartbeat;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.util.Register2ControllerUtils;
import com.nsb.enms.restful.controllerclient.ApiException;
import com.nsb.enms.restful.controllerclient.api.CtlSystemApi;

public class HeartBeatManager
{
    private static final Logger log = LogManager
            .getLogger( HeartBeatManager.class );

    private CtlSystemApi systemApi = new CtlSystemApi();
    
    private static final int PERIOD = 10 * 1000;

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

        private void checkHeartBeat()
        {
            try
            {
                systemApi.checkHeartbeat();
                log.debug( "controller is in service" );
            }
            catch( ApiException e )
            {
                log.error( "controller is out of service" );
                //不做什么操作，只是不断尝试注册，记下相关日志。
               Register2ControllerUtils.register( PERIOD );             
            }
        }
    }
}
