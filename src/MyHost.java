/* Implement this class. */
import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    // Blocking queue for the tasks of the host
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(10, (t1, t2) -> {
        if (t1.getPriority() == t2.getPriority()) {
            if (t1.getStart() == t2.getStart()) {
                if (Objects.equals(t1.getDuration(), t2.getDuration())) {
                    return t1.getId() - t2.getId();
                } else {
                    return (int) (t1.getDuration() - t2.getDuration());
                }
            } else {
                return t1.getStart() - t2.getStart();
            }
        } else {
            return t2.getPriority() - t1.getPriority();
        }
    });

    // The time when the working task started executing
    volatile double currentTime = 0;

    // The working task
    volatile Task workingTask = null;

    // The stop flag
    volatile Boolean stop = false;


    /**
     * Runs the first task from the queue of the host.
     */
    @Override
    public void run() {
        // While the shutdown method was not called
        while (!stop) {
            // If the queue still has tasks
            if (!queue.isEmpty()) {
                try {
                    // Store the current time before starting the task
                    currentTime = Timer.getTimeDouble();

                    // Take the first task from the queue and simulate its execution
                    workingTask = queue.take();
                    sleep(workingTask.getLeft());

                    // Set left time to 0 and finish the task
                    workingTask.setLeft(0);
                    workingTask.finish();

                    // Set the working task to null to be it is cleared
                    workingTask = null;
                } catch (InterruptedException e) {
                    // If another task interrupts the working task
                    // Set the left time of the working task to the remaining time
                    workingTask.setLeft(workingTask.getDuration() - (Math.round(Timer.getTimeDouble() - currentTime) * 1000));

                    // Add the working task back to the queue to be executed later
                    queue.offer(workingTask);
                }
            }
        }
    }


    /**
     * Adds a task to the queue of the host.
     * @param task the task to be added
     */
    @Override
    public synchronized void addTask(Task task) {
        // Add the task to the queue anyway
        queue.offer(task);

        // If the working task exists and is preemptive
        if (workingTask != null && workingTask.isPreemptible()) {
            // If the entering task has a higher priority than the working task
            if (task.getPriority() > workingTask.getPriority()) {
                // Interrupt the working task to execute the entering task
                this.interrupt();
            }
        }
    }


    /**
     * Returns the size of the queue of the host.
     * @return the size of the queue of the host
     */
    @Override
    public synchronized int getQueueSize() {
        synchronized (queue) {
            if (workingTask != null) {
                // If the working task exists, return the size of the queue + 1
                return queue.size() + 1;
            } else {
                // Else return the size of the queue
                return queue.size();
            }
        }
    }


    /**
     * Returns the total work left of the host, meaning the sum
     * of the work left of the working task and the work left
     * of all the tasks in the queue.
     * @return the total work left of the host
     */
    @Override
    public synchronized long getWorkLeft() {
        long workLeft = 0;
        synchronized (queue) {
            // Compute the work left of the working task and add it to the total work left
            if (workingTask != null) {
                workLeft += workingTask.getDuration() - (Math.round(Timer.getTimeDouble() - currentTime) * 1000);
            }

            // Add all the tasks that are in the queue to the total work left
            for (Task task : queue) {
                workLeft += task.getLeft();
            }
        }

        return workLeft;
    }


    /**
     * Stops the host.
     */
    @Override
    public void shutdown() {
        // Set the stop flag to true
        stop = true;
    }
}