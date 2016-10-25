package com.nsb.enms.adapter.server.common.exception;

import com.nsb.enms.common.ErrorCode;

public class ConvertException extends AdapterException
{
    private static final long serialVersionUID = -6668381592205174586L;

    public ConvertException()
    {
        super( ErrorCode.FAIL_OBJ_NOT_EXIST );
    }

    public ConvertException( String msg )
    {
        super( AdapterExceptionType.EXCPT_CONVERT_ERROR, msg );
    }

    public ConvertException( String msg, Throwable cause )
    {
        super( AdapterExceptionType.EXCPT_CONVERT_ERROR, msg, cause );
    }
}