package com.nsb.enms.adapter.server.common.exception;

public class AdapterException extends Exception
{
    private static final long serialVersionUID = 479829418798909619L;

    public AdapterException( AdapterExceptionType type, String errorReason )
    {
        super( "Exception type:" + type + " and errorReason:" + errorReason );
        type_ = type;
        errorReason_ = errorReason;
    }

    public AdapterException( AdapterExceptionType type, String errorReason,
            Throwable cause )
    {
        super( cause );
        type_ = type;
        errorReason_ = errorReason;
    }

    public AdapterExceptionType type_;

    public String errorReason_;
}
