package com.nsb.enms.action.util;

public class IdGenUtil
{
    private static int id = 0;

    public synchronized static String getId()
    {
        id++;
        return id + "";
    }
}
