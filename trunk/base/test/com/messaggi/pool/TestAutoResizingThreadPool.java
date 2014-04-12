package com.messaggi.pool;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAutoResizingThreadPool extends ThreadPoolTestCase<AutoResizingThreadPool>
{
    private AutoResizingThreadPool pool;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        pool = new AutoResizingThreadPool();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        if (!pool.isShutdown()) {
            pool.shutdown();
        }
        if (!pool.isTerminated()) {
            pool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        }
        super.tearDown();
    }

    @Test
    public void test()
    {
        fail("Not yet implemented");
    }
}