package com.nsb.enms.restful.adapter.server.util;

public class IdGenUtil
{
    private static int id = 0;

    public synchronized static String getId()
    {
        id++;
        return id + "";
    }
}
