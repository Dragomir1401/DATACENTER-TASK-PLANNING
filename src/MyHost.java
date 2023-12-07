/* Implement this class. */
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    // Create blocking queue
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(11, (t1, t2) -> Integer.compare(t2.getPriority(), t1.getPriority()));

    double finishTime = 0;
    Task workingTask = null;
    Boolean stop = false;

    @Override
    public void run() {
        // Print all the tasks in the queue
        for (Task task : queue) {
            System.out.println(task);
        }
        
        // Make the host busy for the duration of the task
        while (!stop) {
            if (!queue.isEmpty()) {
                try {
                    workingTask = queue.take();
                    finishTime = Timer.getTimeDouble();

                    // Print the task
                    sleep(workingTask.getLeft());
                    workingTask.setLeft(0);

                    // Finish the task
                    workingTask.finish();
                    workingTask = null;
                } catch (InterruptedException e) {
                    workingTask.setLeft(workingTask.getDuration() - (Math.round(Timer.getTimeDouble() - finishTime) * 1000));
                    queue.offer(workingTask);
                }
            }
        }
    }

    @Override
    public void addTask(Task task) {
        // Add the task to the queue
        queue.offer(task);

        // Check if the working task is preemptive
        if (workingTask != null && workingTask.isPreemptible()) {
            // Check if the entering task has higher priority
            if (task.getPriority() > workingTask.getPriority()) {
                // Interrupt the working task
                this.interrupt();
            }
        }
    }

    @Override
    public int getQueueSize() {
        synchronized (queue) {
            if (workingTask != null) {
                return queue.size() + 1;
            } else {
                return queue.size();
            }
        }
    }

    @Override
    public long getWorkLeft() {
        synchronized (queue) {
            long workLeft = 0;
            for (Task task : queue) {
                workLeft += task.getDuration();
            }
            return workLeft;
        }
    }

    @Override
    public void shutdown() {
        // Stop the host
        stop = true;
    }
}