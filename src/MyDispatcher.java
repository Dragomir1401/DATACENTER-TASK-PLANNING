/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    int minQueueSize = Integer.MAX_VALUE;
    long minWorkLeft = Integer.MAX_VALUE;
    int lastHostId = 0;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        // Switch between the scheduling algorithms
        switch (algorithm) {
            case ROUND_ROBIN -> addTaskRR(task);
            case SHORTEST_QUEUE -> addTaskSQ(task);
            case LEAST_WORK_LEFT -> addTaskLWL(task);
            case SIZE_INTERVAL_TASK_ASSIGNMENT -> addTaskSITA(task);
            default -> System.out.println("Invalid scheduling algorithm.");
        }
    }

    public void addTaskSITA(Task task) {
        // Add tasks of type X to the host X
        int taskType = task.getType().ordinal();

        // Add the task to the host
        hosts.get(taskType).addTask(task);
    }
    public void addTaskLWL(Task task) {
        // Firstly find the host with the least work left
        for (Host h : hosts) {
            // Print the queue size of each host
            if (h.getWorkLeft() < minWorkLeft) {
                minWorkLeft = h.getWorkLeft();
            }
        }

        for (Host host : hosts) {
            // Assign task to the host with the smallest queue size
            if (host.getWorkLeft() == minWorkLeft) {
                // Add the task to the host
                host.addTask(task);

                // Reset the minWorkLeft
                minWorkLeft = Integer.MAX_VALUE;

                return;
            }
        }
    }
    public void addTaskRR(Task task) {
        // Add the task to the host
        hosts.get((lastHostId + 1) % hosts.size()).addTask(task);
        lastHostId = (lastHostId + 1) % hosts.size();
    }

    public void addTaskSQ(Task task) {
        // Firstly find the host with the smallest queue size
        for (Host h : hosts) {
            // Print the queue size of each host
            if (h.getQueueSize() < minQueueSize) {
                minQueueSize = h.getQueueSize();
            }
        }

        for (Host host : hosts) {
            // Assign task to the host with the smallest queue size
            if (host.getQueueSize() == minQueueSize) {
                // Add the task to the host
                host.addTask(task);

                // Reset the minQueueSize
                minQueueSize = Integer.MAX_VALUE;

                return;
            }
        }
    }
}