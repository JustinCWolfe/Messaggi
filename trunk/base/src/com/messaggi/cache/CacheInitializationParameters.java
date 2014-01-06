package com.messaggi.cache;

public class CacheInitializationParameters
{
    private static final int DEFAULT_MAX_SIZE = 1000;

    private static final boolean DEFAULT_RECORD_STATS = false;

    public static final CacheInitializationParameters DEFAULT_INIT_PARAMS = new CacheInitializationParameters();

    private int maxSize;

    private boolean recordStats;

    public int getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public boolean isRecordStats()
    {
        return recordStats;
    }

    public void setRecordStats(boolean recordStats)
    {
        this.recordStats = recordStats;
    }

    public CacheInitializationParameters()
    {
        this(DEFAULT_MAX_SIZE, DEFAULT_RECORD_STATS);
    }

    public CacheInitializationParameters(int maxSize, boolean recordStats)
    {
        this.maxSize = maxSize;
        this.recordStats = recordStats;
    }
}

