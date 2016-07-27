package com.nsb.enms.restful.adapter.server.action.common.exception;

public class ConvertException extends NbiException
{
    private static final long serialVersionUID = -6668381592205174586L;

    public ConvertException()
    {
        super( NbiExceptionType.EXCPT_CONVERT_ERROR, "" );
    }

    public ConvertException( String msg )
    {
        super( NbiExceptionType.EXCPT_CONVERT_ERROR, msg );
    }

    public ConvertException( String msg, Throwable cause )
    {
        super( NbiExceptionType.EXCPT_CONVERT_ERROR, msg, cause );
    }
}