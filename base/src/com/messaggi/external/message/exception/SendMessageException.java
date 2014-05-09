package com.messaggi.external.message.exception;

public abstract class SendMessageException extends Exception
{
    private static final long serialVersionUID = -4421740111232326948L;

    public SendMessageException()
    {
        super();
    }

    public SendMessageException(Throwable cause)
    {
        super(cause);
    }
}

