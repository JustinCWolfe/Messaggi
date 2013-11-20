package com.messaggi.web.service;

public class WebServiceException extends Exception
{
    public enum ErrorCode {
        INVALID_REQUEST, FAILED_TO_CREATE, FAILED_TO_READ, FAILED_TO_UPDATE, FAILED_TO_DELETE;
    }

    private static final long serialVersionUID = 6845110677489399269L;

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

    public WebServiceException(ErrorCode errorCode, String message)
    {
        this.errorCode = errorCode;
        this.message = message;
    }
}

