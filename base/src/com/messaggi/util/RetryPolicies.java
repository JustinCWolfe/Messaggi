package com.messaggi.util;

public class RetryPolicies
{
    public static final RetryPolicy NoRetry = new RetryPolicy()
    {
        @Override
        public ShouldRetryResult shouldRetry(int currentRetryCount)
        {
            return RetryPolicyHelper.NO_RETRY_RESULT;
        }
    };

    /**
     * Linear retry.
     */
    public static final RetryPolicy Retry = new RetryPolicy()
    {
        private static final long MILLISECONDS_BETWEEN_RETRIES = 1000;

        private static final int RETRY_COUNT = 10;

        @Override
        public ShouldRetryResult shouldRetry(int currentRetryCount)
        {
            return new ShouldRetryResult(MILLISECONDS_BETWEEN_RETRIES, currentRetryCount < RETRY_COUNT);
        }
    };
}

