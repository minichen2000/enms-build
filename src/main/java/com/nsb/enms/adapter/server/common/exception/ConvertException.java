package com.nsb.enms.restful.adapter.server.common.exception;

public class ConvertException extends AdapterException
{
    private static final long serialVersionUID = -6668381592205174586L;

    public ConvertException()
    {
        super( AdapterExceptionType.EXCPT_CONVERT_ERROR, "" );
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