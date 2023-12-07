/* Implement this class. */
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    // Create blocking queue
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    double finishTime = 0;
    Task task = null;
    Boolean stop = false;

    @Override
    public void run() {
        // Make the host busy for the duration of the task
        while (!stop) {
            if (!queue.isEmpty()) {
                try {
                    finishTime = Timer.getTimeDouble();
                    task = queue.take();

                    // Print the task
                    System.out.println("Running task " + task.getId() + " on host " + this.getId());

                    sleep(task.getLeft());

                    // Finish the task
                    task.finish();
                    task.setLeft(0);
                    task = null;
                } catch (InterruptedException e) {
                    // Compute the time elapsed since the task was started
                    long elapsedTime = Math.round(Timer.getTimeDouble() - finishTime) * 1000;
                    task.setLeft(task.getDuration() - elapsedTime);
                    queue.offer(task);
                }
            }
        }
    }

    @Override
    public void addTask(Task task) {
        // Print what task is added
        queue.offer(task);
    }

    @Override
    public int getQueueSize() {
       synchronized (queue) {
           if (task != null) {
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
