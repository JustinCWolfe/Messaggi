package com.messaggi.util;

public interface Task extends Runnable
{
    String getName();

    long getTotalMilliseconds();
}
