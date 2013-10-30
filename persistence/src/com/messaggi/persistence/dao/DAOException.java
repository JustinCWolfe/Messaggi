package com.messaggi.persistence.dao;

public class DAOException extends Exception
{
    public enum ErrorCode { 
        FAIL_TO_INSERT, UPDATE_FAILED, SQL_ERROR, CLONE_ERROR;
    }
    
    private static final long serialVersionUID = 362769899858073402L;
    
    private ErrorCode errorCode;

    private String message;

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public DAOException(ErrorCode errorCode, String message)
    {
        this.errorCode = errorCode;
        this.message = message;
    }
}
