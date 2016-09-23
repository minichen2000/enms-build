package com.nsb.enms.adapter.server.business.register;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.business.register.RegisterManager;

public class Register2ControllerUtils
{
    private static final Logger log = LogManager.getLogger( Register2ControllerUtils.class );
    public static void register(long period)
    {
        final RegisterManager register = new RegisterManager();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            public void run()
            {
                log.debug( "start to register to Controller" );
                boolean isOk = register.register2Controller();
                log.debug( "the result of registering to Controller is :{}",
                    isOk );
                if( isOk )
                {
                    timer.cancel();
                }
            }
        }, 0, period );
    }
}
