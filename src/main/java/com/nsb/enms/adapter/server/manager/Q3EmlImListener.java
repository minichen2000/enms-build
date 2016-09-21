package com.nsb.enms.adapter.server.manager;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.conf.ConfLoader;

public class Q3EmlImListener extends TimerTask
{
    private static final Logger log = LogManager
            .getLogger( Q3EmlImListener.class );

    private static int count = 0;

    private static final int MAX_COUNT = ConfLoader.getInstance()
            .getInt( "MAX_EMLIM_MONITOR_NUM", 5 );

    private int groupId;

    public Q3EmlImListener( int groupId )
    {
        this.groupId = groupId;
    }

    @Override
    public void run()
    {
        monitorEmlIm();
    }

    private void monitorEmlIm()
    {
        try
        {
            boolean flag = CheckQ3EmlImApp.checkQ3EmlIm( groupId );

            if( !flag && count == MAX_COUNT )
            {
                count = 0;
                // todo
                // adapter把所有网元communicationState置为unreachable，并发通知,记日志，后退出。
                log.error( "The emlim with groupId=" + groupId + " died!!!" );
                System.exit( 1 );
            }
            else if( !flag && count < MAX_COUNT )
            {
                count++;
                monitorEmlIm();
            }
        }
        catch( Exception e )
        {
            log.error( "Q3EmlImListener", e );
        }
    }
}
