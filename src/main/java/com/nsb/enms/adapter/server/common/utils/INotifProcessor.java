package com.nsb.enms.adapter.server.common.utils;

import com.nsb.enms.adapter.server.common.exception.AdapterException;

public interface INotifProcessor<E>
{
    public void process( E event ) throws AdapterException;
}