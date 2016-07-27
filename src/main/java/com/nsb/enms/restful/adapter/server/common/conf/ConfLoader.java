package com.nsb.enms.restful.adapter.server.common.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.nsb.enms.restful.adapter.server.common.exception.NbiException;
import com.nsb.enms.restful.adapter.server.common.exception.NbiExceptionType;

public class ConfLoader
{
    private Properties conf = new Properties();

    private static ConfLoader confLoader = new ConfLoader();

    private ConfLoader()
    {
    }

    public static ConfLoader getInstance()
    {
        return confLoader;
    }

    public void loadConf( String confFile ) throws NbiException
    {
        loadOneConfFile( confFile );
        loadReferenceConfFiles( new File( confFile ).getParentFile() );
    }

    public String getConf( String name ) throws NbiException
    {
        String value = conf.getProperty( name );
        if( value == null )
            throw new NbiException( NbiExceptionType.EXCPT_CONF_NOT_FOUND,
                    "No such configuration: '" + name + "'" );
        return value.trim();
    }

    public String getConf( String name, String defaultValue )
    {
        return conf.getProperty( name, defaultValue );
    }

    public boolean containsKey( String name )
    {
        return conf.containsKey( name );
    }

    public int getInt( String name ) throws NbiException
    {
        String val = getConf( name );
        try
        {
            return Integer.parseInt( val );
        }
        catch( NumberFormatException e )
        {
            throw new NbiException( NbiExceptionType.EXCPT_CONF_NOT_FOUND,
                    "Illegal int format: '" + val + "' for: " + name, e );
        }
    }

    public int getInt( String name, int defaultValue )
    {
        try
        {
            return getInt( name );
        }
        catch( NbiException e )
        {
            return defaultValue;
        }
    }

    public boolean getBoolean( String name ) throws NbiException
    {
        String value = getConf( name );
        if( "TRUE".equalsIgnoreCase( value ) )
            return true;
        if( "FALSE".equalsIgnoreCase( value ) )
            return false;
        throw new NbiException( NbiExceptionType.EXCPT_CONF_NOT_FOUND,
                "Illegal boolean format: '" + value + "' for: " + name );
    }

    public boolean getBoolean( String name, boolean defaultValue )
    {
        try
        {
            return getBoolean( name );
        }
        catch( NbiException e )
        {
            return defaultValue;
        }
    }

    private void loadOneConfFile( String file ) throws NbiException
    {
        try
        {
            FileInputStream fin = new FileInputStream( file );
            conf.load( fin );
            fin.close();
        }
        catch( IOException e )
        {
            throw new NbiException( NbiExceptionType.EXCPT_IO_ERROR,
                    e.getMessage(), e );
        }
    }

    private void loadReferenceConfFiles( File dir ) throws NbiException
    {
        String referenceConfFiles = null;
        try
        {
            referenceConfFiles = getConf( ConfigKey.REFERENCE_CONF_FILES );
        }
        catch( NbiException e )
        {
            return;
        }
        String[] files = referenceConfFiles.split( "\\s*,\\s*" );
        for( String confFile : files )
        {
            loadOneConfFile( new File( dir, confFile ).getAbsolutePath() );
        }
    }
}