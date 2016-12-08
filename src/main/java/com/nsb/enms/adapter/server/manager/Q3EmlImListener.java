package com.nsb.enms.adapter.server.manager;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Q3EmlImListener extends TimerTask
{
    private static final Logger log = LogManager
            .getLogger( Q3EmlImListener.class );

    private int groupId;

    public Q3EmlImListener( int groupId )
    {
        this.groupId = groupId;
    }

    @Override
    public void run()
    {
        boolean flag = CheckQ3EmlImApp.checkQ3EmlIm( groupId );
        if (!flag)
        {
            log.debug( "Connect to emlim_" + groupId + " failed, start to check it loop." );
            CheckQ3EmlImApp.check( groupId );
        }
        log.debug( "Connect to emlim_" + groupId + " success, no action need." );
    }
}
