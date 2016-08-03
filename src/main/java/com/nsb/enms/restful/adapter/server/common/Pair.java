package com.nsb.enms.restful.adapter.server.common;

import java.io.Serializable;

public class Pair<K, V> implements Serializable
{
    private static final long serialVersionUID = -7003536441367732878L;

    private Object first;

    private Object second;

    public Pair()
    {
    }

    public Pair( Object obj1, Object obj2 )
    {
        first = obj1;
        second = obj2;
    }

    public Object getFirst()
    {
        return first;
    }

    public void setFirst( Object obj1 )
    {
        first = obj1;
    }

    public Object getSecond()
    {
        return second;
    }

    public void setSecond( Object obj2 )
    {
        second = obj2;
    }

    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = prime * result + (first != null ? first.hashCode() : 0);
        result = prime * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    public boolean equals( Object obj )
    {
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( getClass() != obj.getClass() )
            return false;
        Pair other = (Pair) obj;
        if( first == null )
        {
            if( other.first != null )
                return false;
        }
        else if( !first.equals( other.first ) )
            return false;
        if( second == null )
        {
            if( other.second != null )
                return false;
        }
        else if( !second.equals( other.second ) )
            return false;
        return true;
    }

    public String toString()
    {
        return (new StringBuilder()).append( "Pair-[" )
                .append( first.toString() ).append( ", " )
                .append( second.toString() ).append( ']' ).toString();
    }
}