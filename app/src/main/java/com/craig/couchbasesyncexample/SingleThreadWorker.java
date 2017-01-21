package com.craig.couchbasesyncexample;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class SingleThreadWorker implements AsyncWorker {
    List<Runnable> workQueue = new ArrayList<>();
    boolean shouldRun = true;
    Thread worker = new Thread(new Runnable() {
        @Override
        public void run() {
            while (shouldRun) {
                poll();
            }
        }

        private void poll() {
            Runnable removedWork = lookForWork();
            if (removedWork!=null) doWork(removedWork);
        }

        @Nullable
        private Runnable lookForWork() {
            Runnable removedWork = null;
            synchronized (workQueue) {
                if(workQueue.size() > 0) {
                    removedWork = workQueue.remove(0);
                }
                else {
                    try {
                        workQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return removedWork;
        }
    }, getClass().getName() + ": " + UUID.randomUUID());

    private void doWork(Runnable removedWork) {
        removedWork.run();
    }

    public SingleThreadWorker() {
        worker.start();
    }

    @Override
    public void doAsync(Runnable work) {
        synchronized (workQueue) {
            workQueue.add(work);
            workQueue.notify();
        }
    }
}
