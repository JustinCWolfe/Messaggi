package com.messaggi.util;

public interface RetryPolicy
{
    ShouldRetryResult shouldRetry(int retryCount);

    public static class RetryPolicyHelper
    {
        public static final ShouldRetryResult NO_RETRY_RESULT = new ShouldRetryResult(false);

        public static void pause(ShouldRetryResult result)
        {
            if (result.delayMilliseconds > 0) {
                try {
                    Thread.sleep(result.delayMilliseconds);
                } catch (InterruptedException e) {
                    // Reset interrupt flag.
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static class ShouldRetryResult
    {
        public final long delayMilliseconds;

        public final boolean shouldRetry;

        public ShouldRetryResult(boolean shouldRetry)
        {
            this(0, shouldRetry);
        }

        public ShouldRetryResult(long delayMilliseconds, boolean shouldRetry)
        {
            this.delayMilliseconds = delayMilliseconds;
            this.shouldRetry = shouldRetry;
        }
    }
}

