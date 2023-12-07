/* Implement this class. */

import java.util.LinkedList;
import java.util.Queue;

public class MyHost extends Host {
    // Create queue for tasks
    private final Queue<Task> queue = new LinkedList<>();

    @Override
    public void run() {
        // Make the host busy for the duration of the task
        while (!queue.isEmpty()) {
            Task task = queue.peek();
            if (task.getLeft() > 0) {
                task.setLeft(task.getLeft() - 1);
            } else {
                task.finish();
                queue.remove();
            }
        }
    }

    @Override
    public void addTask(Task task) {
        queue.add(task);
    }

    @Override
    public int getQueueSize() {
        return queue.size();
    }

    @Override
    public long getWorkLeft() {
        return 0;
    }

    @Override
    public void shutdown() {
    }
}
