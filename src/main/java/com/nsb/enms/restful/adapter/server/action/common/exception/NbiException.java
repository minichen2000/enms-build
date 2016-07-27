package com.nsb.enms.restful.adapter.server.action.common.exception;

public class NbiException extends Exception
{
    private static final long serialVersionUID = 479829418798909619L;

    public NbiException( NbiExceptionType type, String errorReason )
    {
        super( "Exception type:" + type + " and errorReason:" + errorReason );
        type_ = type;
        errorReason_ = errorReason;
    }

    public NbiException( NbiExceptionType type, String errorReason,
            Throwable cause )
    {
        super( cause );
        type_ = type;
        errorReason_ = errorReason;
    }

    public NbiExceptionType type_;

    public String errorReason_;
}
