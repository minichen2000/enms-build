package com.nsb.enms.adapter.server.common;

import java.io.Serializable;

public class Pair<T1, T2> implements Serializable
{
    private static final long serialVersionUID = -7003536441367732878L;

    private T1 first;

    private T2 second;

    public Pair()
    {
    }

    public Pair( T1 obj1, T2 obj2 )
    {
        this.first = obj1;
        this.second = obj2;
    }

    public T1 getFirst()
    {
        return first;
    }

    public void setFirst( T1 obj1 )
    {
        this.first = obj1;
    }

    public T2 getSecond()
    {
        return second;
    }

    public void setSecond( T2 obj2 )
    {
        this.second = obj2;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( getClass() != obj.getClass() )
            return false;
        @SuppressWarnings("unchecked")
        Pair<T1, T2> other = (Pair<T1, T2>) obj;
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

    @Override
    public String toString()
    {
        return "Pair-[" + first.toString() + ", " + second.toString() + ']';
    }
}