package com.messaggi.messaging.task;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import com.messaggi.util.DelegatingThreadPool;

public class SendMessageService implements Runnable
{
    private final ExecutorService pool;

    public SendMessageService() throws IOException
    {
        pool = new DelegatingThreadPool<SendMessageTask>();
    }

    @Override
    public void run()
    {
        // run the service
        try {
            for (;;) {
                pool.execute(new Handler(serverSocket.accept()));
            }
        } catch (IOException ex) {
            pool.shutdown();
        }
    }
}

class Handler implements Runnable
{
    private final Socket socket;

    Handler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        // read and service request on socket
    }
}

