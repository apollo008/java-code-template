package com.freedom.commonutil.thread;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *@author dingbinthu@163.com
 *@create 2019-02-27, 22:44
 */
public class TaskDispatcher implements Runnable {
    public static SimpleDateFormat s_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public enum Status {
        Init(0), Running(1), Stopped(2);

        private Status(int value) {
            this.value = value;
        }

        private int value;

        int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            //default value
            return Init;
        }
    }
    protected final static int STAT_INIT = 0;
    protected final static int STAT_RUNNING = 1;
    protected final static int STAT_STOPPED = 2;


    protected Logger logger = LoggerFactory.getLogger(getClass());


    protected String uuid;

    private BlockingQueue<TaskHandler> handlersQueue = new LinkedBlockingQueue<TaskHandler>();
    protected List<TaskHandler> startHandlers;

    private List<TaskListener> taskListeners;

    protected CountableThreadPool threadPool;
    protected ExecutorService executorService;
    protected int threadNum = 1;

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    protected boolean exitWhenComplete = true;
    protected boolean destroyWhenExit = true;

    private ReentrantLock newHandlerLock = new ReentrantLock();
    private Condition newHandlerCondition = newHandlerLock.newCondition();

    private final AtomicLong handlerCount = new AtomicLong(0);

    private Date startTime;
    private int emptySleepTime = 1000;

    public static  TaskDispatcher create() {
        return new TaskDispatcher();
    }

    protected  TaskDispatcher() {
    }

    public TaskDispatcher setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }


    public TaskDispatcher scheduler(TaskHandler handler) {
        addHandlerPrivate(handler);
        return this;
    }

    public TaskDispatcher schedulerQueue(BlockingQueue<TaskHandler> handlersQueue) {
        return setSchedulerQueue(handlersQueue);
    }

    public TaskDispatcher setSchedulerQueue(BlockingQueue<TaskHandler> handlersQueue) {
        checkIfRunning();
        BlockingQueue<TaskHandler> oldHandlersQueue = this.handlersQueue;
        this.handlersQueue = handlersQueue;
        if (oldHandlersQueue != null) {
            TaskHandler handler;
            while ((handler = oldHandlersQueue.poll()) != null) {
                addHandlerPrivate(handler);
            }
        }
        return this;
    }

    protected void initComponent() {
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
        if (startHandlers != null) {
            for (TaskHandler handler: startHandlers) {
                addHandlerPrivate(handler);
            }
            startHandlers.clear();
        }
        startTime = new Date();
    }

    @Override
    public void run() {
        checkRunningStat();
        initComponent();
        logger.error("============Task dispatcher {} started by {} concurrent thread(s) at time [{}]!",getUUID(),threadNum,s_sdf.format(getStartTime()));

        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            final TaskHandler handler = handlersQueue.poll();
            if (handler == null) {
                logger.error("-----poll handler is null and threadpool.alive.number is:" + threadPool.getThreadAlive() + " ,and exitWhenComplete is:" + exitWhenComplete);
                if (threadPool.getThreadAlive() == 0 && handlersQueue.isEmpty() && exitWhenComplete ) {
                    break;
                }
                // wait until new taskHandler added
                logger.error("before waitNewHandler() and threadpool.alive.number is:" + threadPool.getThreadAlive() + " ,and exitWhenComplete is:" + exitWhenComplete);
                waitNewHandler();
                logger.error("after waitNewHandler() and threadpool.alive.number is:" + threadPool.getThreadAlive() + " ,and exitWhenComplete is:" + exitWhenComplete);
            } else {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handler.run();
                            onSuccess(handler);
                        } catch (Exception e) {
                            onError(handler);
                            logger.error("process handler " + handler + " error", e);
                        } finally {
                            handlerCount.incrementAndGet();
                            signalNewHandler();
                        }
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
        // release some resources
        if (destroyWhenExit) {
            close();
        }
        logger.error("==========Task dispatcher {} closed! {} task handler finished at time [{}] from time [{}].", getUUID(), handlerCount.get(),s_sdf.format(new Date()),s_sdf.format(getStartTime()));
    }

    protected void onError(TaskHandler handler) {
        if (CollectionUtils.isNotEmpty(taskListeners)) {
            for (TaskListener listener : taskListeners) {
                listener.onError(handler);
            }
        }
    }

    protected void onSuccess(TaskHandler handler) {
        if (CollectionUtils.isNotEmpty(taskListeners)) {
            for (TaskListener listener : taskListeners) {
                listener.onSuccess(handler);
            }
        }
    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("TaskDispatcher is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    public void close() {
        destroyEach(handlersQueue);
        threadPool.shutdown();
    }

    private void destroyEach(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted when sleep",e);
        }
    }

    private void addHandlerPrivate(TaskHandler handler){
        handlersQueue.add(handler);
    }

    public TaskDispatcher addTaskHandlers(TaskHandler...handlers) {
        for (TaskHandler handler:handlers) {
            addHandlerPrivate(handler);
        }
        signalNewHandler();
        return this;
    }

    public TaskDispatcher startHandlers(List<TaskHandler> startHandlers) {
        checkIfRunning();
        this.startHandlers  = new ArrayList<>(startHandlers);
        return this;
    }


    protected void checkIfRunning() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Task dispatcher is already running!");
        }
    }

    public void runAsync() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    private void waitNewHandler() {
        logger.warn("entering waitNewHandler()======");
        newHandlerLock.lock();
        logger.warn("after lock entering waitNewHandler()======");
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && handlersQueue.isEmpty() && exitWhenComplete) {
                logger.warn("return exit entering waitNewHandler()======");
                return;
            }
            logger.warn("before newHandlerCondition.await waitNewHandler()======");
            newHandlerCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
            logger.warn("after newHandlerCondition.await waitNewHandler()======");
        } catch (InterruptedException e) {
            logger.warn("waitNewHandler - interrupted, error {}", e);
        } finally {
            logger.warn("exit waitNewHandler()======");
            newHandlerLock.unlock();
        }
    }

    private void signalNewHandler() {
        try {
            newHandlerLock.lock();
            newHandlerCondition.signalAll();
        } finally {
            newHandlerLock.unlock();
        }
    }

    public void start() {
        runAsync();
    }

    public void stop() {
        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            logger.info("TaskDispatcher " + getUUID() + " stop success!");
        } else {
            logger.info("TaskDispatcher " + getUUID() + " stop fail!");
        }
    }

    public TaskDispatcher thread(int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    public TaskDispatcher thread(ExecutorService executorService, int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        this.executorService = executorService;
        return this;
    }

    public boolean isExitWhenComplete() {
        return exitWhenComplete;
    }

    public TaskDispatcher setExitWhenComplete(boolean exitWhenComplete) {
        this.exitWhenComplete = exitWhenComplete;
        return this;
    }

    public long getHandlerCount() {
        return handlerCount.get();
    }

    public Status getStatus() {
        return Status.fromValue(stat.get());
    }



    /**
     * Get thread count which is running
     * @return thread count which is running
     */
    public int getThreadAlive() {
        if (threadPool == null) {
            return 0;
        }
        return threadPool.getThreadAlive();
    }

    public String getUUID() {
        if (uuid != null) {
            return uuid;
        }
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public TaskDispatcher setExecutorService(ExecutorService executorService) {
        checkIfRunning();
        this.executorService = executorService;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEmptySleepTime(int emptySleepTime) {
        this.emptySleepTime = emptySleepTime;
    }
}
