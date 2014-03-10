package com.messaggi.messaging.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.naming.InitialContext;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.junit.MessaggiTestCase;

public class TestSendMessageThreadPool extends MessaggiTestCase
{
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        messaggiSuiteSetUp();

        InitialContext ic = new InitialContext();
        ic.bind("messaggi:/pool/SendMessageThreadPool", new SendMessageThreadPoolImpl());
    }

    @AfterClass
    public static void tearDownAfterClassClass() throws Exception
    {
        messaggiSuiteTearDown();
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void test() throws Exception
    {
        TestThread tt = new TestThread();
        tt.start();
        tt.addTask(new SendMessageTask("1"));
        tt.addTask(new SendMessageTask("2"));
        tt.addTask(new SendMessageTask("3"));
        tt.addTask(new SendMessageTask("4"));
        tt.interrupt();
        tt.join();
    }

    class SendMessageTask implements Callable<Boolean>
    {
        private final String something;

        @Override
        public Boolean call()
        {
            System.out.println("start running task..." + something);
            System.out.println("finished running task..." + something);
            return true;
        }

        public SendMessageTask(String something)
        {
            this.something = something;
        }
    }

    class TestThread extends Thread
    {
        private final ConcurrentLinkedQueue<SendMessageTask> synchronizedSendMessageTaskQueue = new ConcurrentLinkedQueue<>();

        public void addTask(SendMessageTask newTask)
        {
            synchronizedSendMessageTaskQueue.add(newTask);
        }

        @Override
        public void run()
        {
            System.out.println("starting...");
            try {
                while (true) {
                    if (isInterrupted()) {
                        break;
                    }
                    SendMessageTask sendMessageTaskToExecute = synchronizedSendMessageTaskQueue.peek();
                    if (sendMessageTaskToExecute != null) {
                        System.out.println("running task...");
                        boolean result = sendMessageTaskToExecute.call();
                        if (result) {
                            synchronizedSendMessageTaskQueue.remove(sendMessageTaskToExecute);
                        } else {
                            // Should have retry perhaps?
                        }
                    } else {
                        System.out.println("no task to run...");
                        sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("interupted...");
            }
            System.out.println("exiting...");
        }
    }
}

