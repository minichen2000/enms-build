package com.nsb.enms.adapter.server.common.exception;

import com.nsb.enms.common.ErrorCode;

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
    
    public AdapterException( AdapterExceptionType type, ErrorCode errorCode )
    {
        super( errorCode.name() );
        type_ = type;
        errorCode_ = errorCode.name();
        errorReason_ = errorCode.getDescription();
    }
    
    public AdapterException( ErrorCode errorCode )
    {
        super( errorCode.name() );
        errorCode_ = errorCode.name();
        errorReason_ = errorCode.getDescription();
    }

    public AdapterExceptionType type_;

    public String errorReason_;
    
    public String errorCode_;
}
