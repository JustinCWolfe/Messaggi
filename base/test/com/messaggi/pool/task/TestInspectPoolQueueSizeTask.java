package com.messaggi.pool.task;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.pool.ThreadPoolTestCase;
import com.messaggi.pool.ThreadPoolTestCase.MockReceiveResultThreadPool;
import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.Task;
import com.messaggi.pool.task.InspectPoolQueueSizeTask.PoolSizeOpinion;
import com.messaggi.pool.task.Task.State;

public class TestInspectPoolQueueSizeTask extends ThreadPoolTestCase<MockReceiveResultThreadPool>
{
    private InspectPoolQueueSizeTask task;

    private Mean meanCalculator;

    private StandardDeviation standardDeviationCalculator;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        pool = new MockReceiveResultThreadPool();
        task = new InspectPoolQueueSizeTask(pool);
        meanCalculator = getTaskMeanCalculator(task);
        standardDeviationCalculator = getTaskStandardDeviationCalculator(task);
    }

    private static Mean getTaskMeanCalculator(InspectPoolQueueSizeTask task) throws Exception
    {
        Field fi = task.getClass().getDeclaredField("meanCalculator");
        fi.setAccessible(true);
        return (Mean) fi.get(task);
    }

    private static StandardDeviation getTaskStandardDeviationCalculator(InspectPoolQueueSizeTask task) throws Exception
    {
        Field fi = task.getClass().getDeclaredField("standardDeviationCalculator");
        fi.setAccessible(true);
        return (StandardDeviation) fi.get(task);
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        pool.shutdown();
        super.tearDown();
    }

    @Override
    protected void validateTaskInitialState(Task... tasks) throws Exception
    {
        super.validateTaskInitialState(tasks);
        for (Task task : tasks) {
            if (task instanceof InspectPoolQueueSizeTask) {
                InspectPoolQueueSizeTask testTask = (InspectPoolQueueSizeTask) task;
                assertThat(testTask.getOpinion(), equalTo(PoolSizeOpinion.NONE));
                assertThat(testTask.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
            }
        }
    }

    @Test
    public void testRun_NoTasks() throws Exception
    {
        validatePoolRunningState();
        validateTaskInitialState(task);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(0.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_OneTask() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        validatePoolRunningState();
        validateTaskInitialState(t1, task);
        pool.addTask(t1);
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(25);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(0.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_TwoTasks() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        validatePoolRunningState();
        validateTaskInitialState(t1, t2, task);
        pool.addTask(t1);
        pool.addTask(t2);
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(25);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(0.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_ThreeTasks() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 1);
        validatePoolRunningState();
        validateTaskInitialState(t1, t2, t3, task);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(25);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(1.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_FourTasks() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 1);
        WaitingTask t4 = new WaitingTask(waitTime * 1);
        validatePoolRunningState();
        validateTaskInitialState(t1, t2, t3, t4, task);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        pool.addTask(t4);
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(25);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(2.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_SixTasksDifferentReadingEachSample() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 75;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 1);
        WaitingTask t4 = new WaitingTask(waitTime * 1);
        WaitingTask t5 = new WaitingTask(waitTime * 1);
        WaitingTask t6 = new WaitingTask(waitTime * 1);
        validatePoolRunningState();
        validateTaskInitialState(t1, t2, t3, t4, t5, t6, task);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        pool.addTask(t4);
        pool.addTask(t5);
        pool.addTask(t6);
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(25);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(2.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(2.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_ShortTasksToGetUndecided() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 5;
        List<WaitingTask> tasks = new ArrayList<>();
        // First tasks will be longer so that we will be left with 20 in the queues.
        tasks.add(new WaitingTask(waitTime * 10));
        tasks.add(new WaitingTask(waitTime * 10));
        // Next tasks are short ones.
        for (int i = 0; i < 10; i++) {
            tasks.add(new WaitingTask(waitTime));
        }
        // Next tasks are longer ones.
        tasks.add(new WaitingTask(waitTime * 10));
        tasks.add(new WaitingTask(waitTime * 10));
        // Next tasks are short ones.
        for (int i = 0; i < 8; i++) {
            tasks.add(new WaitingTask(waitTime));
        }
        validatePoolRunningState();
        validateTaskInitialState(task);
        for (WaitingTask t : tasks) {
            validateTaskInitialState(t);
            pool.addTask(t);
        }
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(25);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), closeTo(9, 1));
        assertThat(standardDeviationCalculator.getResult(), closeTo(10.0, 1));
        validatePoolRunningState();
    }

    @Test
    public void testRun_ShouldShrink() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 5;
        List<WaitingTask> tasks = new ArrayList<>();
        // First tasks will be longer so that we will be left with 30 in the queues.
        tasks.add(new WaitingTask(waitTime * 400));
        tasks.add(new WaitingTask(waitTime * 400));
        // Next tasks are short ones.
        for (int i = 0; i < 4; i++) {
            tasks.add(new WaitingTask(waitTime));
        }
        validatePoolRunningState();
        validateTaskInitialState(task);
        for (WaitingTask t : tasks) {
            validateTaskInitialState(t);
            pool.addTask(t);
        }
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(10);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(4.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us at the should shrink limit.
        pool.addTask(new WaitingTask(waitTime));
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(5.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us above the should shrink limit.
        pool.addTask(new WaitingTask(waitTime));
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.OK));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.OK));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.OK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(6.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_ShouldGrow() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 5;
        List<WaitingTask> tasks = new ArrayList<>();
        // First tasks will be longer so that we will be left with 30 in the queues.
        tasks.add(new WaitingTask(waitTime * 400));
        tasks.add(new WaitingTask(waitTime * 400));
        // Next tasks are short ones.
        for (int i = 0; i < 99; i++) {
            tasks.add(new WaitingTask(waitTime));
        }
        validatePoolRunningState();
        validateTaskInitialState(task);
        for (WaitingTask t : tasks) {
            validateTaskInitialState(t);
            pool.addTask(t);
        }
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(10);
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.OK));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.OK));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.OK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(99.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us at the should grow limit.
        pool.addTask(new WaitingTask(waitTime));
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(100.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us above the should grow limit.
        pool.addTask(new WaitingTask(waitTime));
        task.run();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(101.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_Interrupted() throws Exception
    {
        PoolInspector poolInspector = new PoolInspector(task);
        List<Thread> testThreads = getTestThreads(poolInspector);
        Thread poolInspectorThread = testThreads.get(0);
        validatePoolRunningState();
        validateTaskInitialState(task);
        poolInspectorThread.start();
        // Give the inspection task time to start before interrupting.
        Thread.sleep(20);
        poolInspectorThread.interrupt();
        poolInspectorThread.join();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(20, 20));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.INTERRUPTED));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.INTERRUPTED));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.INTERRUPTED));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(0.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
    }

    @Test
    public void testRun_AddTasksDuringInspection() throws Exception
    {
        long waitTime = 1;
        List<WaitingTask> tasks = new ArrayList<>();
        // First tasks will be longer so that we will be left with 30 in the queues.
        tasks.add(new WaitingTask(waitTime * 50));
        tasks.add(new WaitingTask(waitTime * 50));
        // Next tasks are short ones.
        for (int i = 0; i < 300; i++) {
            tasks.add(new WaitingTask(waitTime));
        }
        validatePoolRunningState();
        validateTaskInitialState(task);
        for (WaitingTask t : tasks) {
            validateTaskInitialState(t);
            pool.addTask(t);
        }
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(10);
        PoolInspector poolInspector = new PoolInspector(task);
        List<Thread> testThreads = getTestThreads(poolInspector);
        Thread poolInspectorThread = testThreads.get(0);
        poolInspectorThread.start();
        Thread.sleep(10);
        // Add tasks while the inspection is occurring.
        for (WaitingTask t : tasks) {
            pool.addTask(t);
        }
        Thread.sleep(100);
        // Add more tasks.
        for (WaitingTask t : tasks) {
            pool.addTask(t);
        }
        poolInspectorThread.join();
        assertThat(
                (double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(InspectPoolQueueSizeTask.MILLISECONDS_BETWEEN_SAMPLES *
                        (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getOpinion(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(pool.receivedResult, equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), closeTo(550.0, 50));
        assertThat(standardDeviationCalculator.getResult(), closeTo(250.0, 50));
        validatePoolRunningState();
    }
}
