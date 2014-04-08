package com.messaggi.pool;

import static org.junit.Assert.fail;

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
        pool = new AutoResizingThreadPool();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        pool.shutdown();
    }

    @Test
    public void test()
    {
        fail("Not yet implemented");
    }
}