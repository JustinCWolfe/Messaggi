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

import com.messaggi.pool.AutoResizingThreadPool;
import com.messaggi.pool.ThreadPoolTestCase;
import com.messaggi.pool.ThreadPoolTestHelper;
import com.messaggi.pool.ThreadPoolTestHelper.PoolInspector;
import com.messaggi.pool.ThreadPoolTestHelper.WaitingTask;
import com.messaggi.pool.task.InspectPoolQueueSizeTask.PoolSizeOpinion;
import com.messaggi.pool.task.Task.State;

public class TestInspectPoolQueueSizeTask extends ThreadPoolTestCase<AutoResizingThreadPool>
{
    private InspectPoolQueueSizeTask task;

    private Mean meanCalculator;

    private StandardDeviation standardDeviationCalculator;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        pool = new AutoResizingThreadPool(1, 2);
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
    protected void validateTaskInitialState(Task<?>... tasks) throws Exception
    {
        super.validateTaskInitialState(tasks);
        for (Task<?> task : tasks) {
            if (task instanceof InspectPoolQueueSizeTask) {
                InspectPoolQueueSizeTask testTask = (InspectPoolQueueSizeTask) task;
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(0.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        validateWaitingTaskResults(t1);
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.NONE));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(0.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        validateWaitingTaskResults(t1, t2);
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(1.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        validateWaitingTaskResults(t1, t2, t3);
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(2.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        validateWaitingTaskResults(t1, t2, t3, t4);
    }

    @Test
    public void testRun_SixTasksDifferentReadingEachSample() throws Exception
    {
        validatePoolRunningState();
        WaitingTask t1 = new WaitingTask(task.getMillisecondsBetweenSamples());
        WaitingTask t2 = new WaitingTask(task.getMillisecondsBetweenSamples());
        WaitingTask t3 = new WaitingTask(task.getMillisecondsBetweenSamples() / (3 / 2));
        WaitingTask t4 = new WaitingTask(task.getMillisecondsBetweenSamples() / (3 / 2));
        WaitingTask t5 = new WaitingTask(task.getMillisecondsBetweenSamples());
        WaitingTask t6 = new WaitingTask(task.getMillisecondsBetweenSamples());
        validatePoolRunningState();
        validateTaskInitialState(t1, t2, t3, t4, t5, t6, task);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        pool.addTask(t4);
        pool.addTask(t5);
        pool.addTask(t6);
        // Give the waiting task time to start before inspecting the pool.
        Thread.sleep(task.getMillisecondsBetweenSamples() / 2);
        task.run();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        // Should give us an opinion of SHOULD_SHRINK because mean is less than 
        // CHECK_MEAN_VERSUS_STANDARD_DEVIATION_MEAN_LIMIT value.
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(2.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(2.0));
        validatePoolRunningState();
        validateWaitingTaskResults(t1, t2, t3, t4, t5, t6);
    }

    @Test
    public void testRun_ShortTasksToGetUndecided() throws Exception
    {
        pool = new AutoResizingThreadPool(1, 10);
        task = new InspectPoolQueueSizeTask(pool);
        meanCalculator = getTaskMeanCalculator(task);
        standardDeviationCalculator = getTaskStandardDeviationCalculator(task);
        List<WaitingTask> tasks = new ArrayList<>();
        long longTaskRunTime = task.getMillisecondsBetweenSamples();
        PoolInspector poolInspector = new PoolInspector(task);
        List<Thread> testThreads = ThreadPoolTestHelper.getTestThreads(poolInspector);
        Thread poolInspectorThread = testThreads.get(0);
        // First inspection should find 0 tasks.
        poolInspectorThread.start();
        Thread.sleep(longTaskRunTime / 2);
        // Add 2 long tasks after first but before second inspection.
        // These tasks will complete between second and third inspections.
        // When they complete, the short tasks will all run before the 
        // third inspection.
        WaitingTask t1 = new WaitingTask(longTaskRunTime);
        tasks.add(t1);
        pool.addTask(t1);
        WaitingTask t2 = new WaitingTask(longTaskRunTime);
        tasks.add(t2);
        pool.addTask(t2);
        // Add many short tasks.
        long shortTaskRunTime = task.getMillisecondsBetweenSamples() / 100;
        for (int i = 0; i < 50; i++) {
            WaitingTask t = new WaitingTask(shortTaskRunTime);
            tasks.add(t);
            pool.addTask(t);
        }
        poolInspectorThread.join();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.UNDECIDED));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), closeTo(16.66, .1));
        assertThat(standardDeviationCalculator.getResult(), closeTo(28.87, .1));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks.toArray(new WaitingTask[tasks.size()]));
    }

    @Test
    public void testRun_ShouldShrink() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 5;
        List<WaitingTask> tasks = new ArrayList<>();
        // First tasks will be longer so that we will be left with 4 in the queues.
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(4.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us at the should shrink limit.
        WaitingTask t1 = new WaitingTask(waitTime);
        tasks.add(t1);
        pool.addTask(t1);
        task.run();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_SHRINK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(5.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us above the should shrink limit.
        WaitingTask t2 = new WaitingTask(waitTime);
        tasks.add(t2);
        pool.addTask(t2);
        task.run();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.OK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(6.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks.toArray(new WaitingTask[tasks.size()]));
    }

    @Test
    public void testRun_ShouldGrow() throws Exception
    {
        validatePoolRunningState();
        long waitTime = 5;
        List<WaitingTask> tasks = new ArrayList<>();
        // First tasks will be longer so that we will be left with 99 in the queues.
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
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.OK));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(99.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us at the should grow limit.
        WaitingTask t1 = new WaitingTask(waitTime);
        tasks.add(t1);
        pool.addTask(t1);
        task.run();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(100.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        // Add another task to put us above the should grow limit.
        WaitingTask t2 = new WaitingTask(waitTime);
        tasks.add(t2);
        pool.addTask(t2);
        task.run();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(101.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(0.0));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks.toArray(new WaitingTask[tasks.size()]));
    }

    @Test
    public void testRun_Interrupted() throws Exception
    {
        PoolInspector poolInspector = new PoolInspector(task);
        List<Thread> testThreads = ThreadPoolTestHelper.getTestThreads(poolInspector);
        Thread poolInspectorThread = testThreads.get(0);
        validatePoolRunningState();
        validateTaskInitialState(task);
        poolInspectorThread.start();
        // Give the inspection task time to start before interrupting.
        Thread.sleep(10);
        poolInspectorThread.interrupt();
        poolInspectorThread.join();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(20, 20));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.INTERRUPTED));
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
        // First tasks will be longer so that we will be left with 300 in the queues.
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
        List<Thread> testThreads = ThreadPoolTestHelper.getTestThreads(poolInspector);
        Thread poolInspectorThread = testThreads.get(0);
        poolInspectorThread.start();
        Thread.sleep(5);
        // Add tasks while the inspection is occurring.
        List<WaitingTask> tasks2 = new ArrayList<>();
        tasks2.add(new WaitingTask(waitTime * 50));
        tasks2.add(new WaitingTask(waitTime * 50));
        for (int i = 0; i < 300; i++) {
            tasks2.add(new WaitingTask(waitTime));
        }
        for (WaitingTask t : tasks2) {
            pool.addTask(t);
        }
        Thread.sleep(task.getMillisecondsBetweenSamples());
        // Add more tasks.
        List<WaitingTask> tasks3 = new ArrayList<>();
        tasks3.add(new WaitingTask(waitTime * 50));
        tasks3.add(new WaitingTask(waitTime * 50));
        for (int i = 0; i < 300; i++) {
            tasks3.add(new WaitingTask(waitTime));
        }
        for (WaitingTask t : tasks3) {
            pool.addTask(t);
        }
        poolInspectorThread.join();
        assertThat((double) task.getTotalRunTime(TimeUnit.MILLISECONDS),
                closeTo(task.getMillisecondsBetweenSamples() * (InspectPoolQueueSizeTask.NUMBER_OF_SAMPLES - 1), 50));
        assertThat(task.getTaskResult(), equalTo(PoolSizeOpinion.SHOULD_GROW));
        assertThat(task.getState(), equalTo(State.COMPLETED));
        assertThat(meanCalculator.getResult(), equalTo(602.0));
        assertThat(standardDeviationCalculator.getResult(), equalTo(302.0));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks.toArray(new WaitingTask[tasks.size()]));
        validateWaitingTaskResults(tasks2.toArray(new WaitingTask[tasks2.size()]));
        validateWaitingTaskResults(tasks3.toArray(new WaitingTask[tasks3.size()]));
    }
}
