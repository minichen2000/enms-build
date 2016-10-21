package com.nsb.enms.adapter.server.notification.entity;

import java.util.HashMap;

public class AttributeInfo extends HashMap<String, String>
{

    /**
     * 
     */
    private static final long serialVersionUID = 2373555488140468125L;

    public void put( String name, int value )
    {
        put( name, String.valueOf( value ) );
    }

    public void put( String name, long value )
    {
        put( name, String.valueOf( value ) );
    }

    public void put( String name, boolean value )
    {
        put( name, String.valueOf( value ) );
    }

    public void put( String name, double value )
    {
        put( name, String.valueOf( value ) );
    }

    public boolean getBool( String name )
    {
        return Boolean.valueOf( get( name ) );
    }

    public boolean getBool( String name, boolean defVal )
    {
        if( containsKey( name ) )
            return getBool( name );
        return defVal;
    }

    public double getDouble( String name )
    {
        return Double.valueOf( get( name ) );
    }

    public double getDouble( String name, double defVal )
    {
        if( containsKey( name ) )
            return getDouble( name );
        return defVal;
    }

    public long getLong( String name )
    {
        return Long.valueOf( get( name ) );
    }

    public long getLong( String name, long defVal )
    {
        if( containsKey( name ) )
            return getLong( name );
        return defVal;
    }

    public int getInt( String name )
    {
        return Integer.valueOf( get( name ) );
    }

    public int getInt( String name, int defVal )
    {
        if( containsKey( name ) )
            return getInt( name );
        return defVal;
    }
}
