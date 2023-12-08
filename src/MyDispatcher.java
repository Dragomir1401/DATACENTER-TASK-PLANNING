/* Implement this class. */

import java.util.List;
import java.util.Objects;

public class MyDispatcher extends Dispatcher {
    // The minimum queue size of a host storage variable
    int minQueueSize = Integer.MAX_VALUE;

    // The minimum work left of a host storage variable
    long minWorkLeft = Integer.MAX_VALUE;

    // The last host id of a host storage variable
    int lastHostId = -1;


    /**
     * Creates a dispatcher.
     *
     * @param algorithm the scheduling algorithm to be used by the dispatcher
     * @param hosts     the list of hosts available for task execution
     */
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }


    /**
     * Add a task to a host queue using different scheduling algorithms.
     * @param task the task to be added
     */
    @Override
    public synchronized void addTask(Task task) {
        // Switch between the scheduling algorithms
        switch (algorithm) {
            case ROUND_ROBIN -> addTaskRR(task);
            case SHORTEST_QUEUE -> addTaskSQ(task);
            case LEAST_WORK_LEFT -> addTaskLWL(task);
            case SIZE_INTERVAL_TASK_ASSIGNMENT -> addTaskSITA(task);
            default -> System.out.println("Invalid scheduling algorithm.");
        }
    }


    /**
     * Add a task to a host queue using the Size Interval Task Assignment algorithm.
     * @param task the task to be added
     */
    public void addTaskSITA(Task task) {
        // Add the task to the host with the same type
        int taskType = task.getType().ordinal();
        hosts.get(taskType).addTask(task);
    }


    /**
     * Add a task to a host queue using the Least Work Left algorithm.
     * @param task the task to be added
     */
    public void addTaskLWL(Task task) {
        Host selectedHost = null;
        long minWorkLeft = Long.MAX_VALUE;

        // Store the last checked host id and the host counter
        int lastCheckedHostId = 0;
        int hostCounter = 0;

        // Find the host with the least work left
        for (Host h : hosts) {
            long workLeft = h.getWorkLeft();

            // If the work left of the current host is less than the minimum work left
            // or the work left of the current host is equal to the minimum work left
            // and the host counter is less than the last checked host id
            // then choose the host with smaller work left or same work left but smaller host id
            if (workLeft < minWorkLeft || (workLeft == minWorkLeft && hostCounter < lastCheckedHostId)) {
                lastCheckedHostId = hostCounter;
                minWorkLeft = workLeft;
                selectedHost = h;
            }

            hostCounter++;
        }

        // Assign task to the selected host if it exists
        if (selectedHost != null) {
            selectedHost.addTask(task);
        }
    }


    /**
     * Add a task to a host queue using the Round Robin algorithm.
     * @param task the task to be added
     */
    public void addTaskRR(Task task) {
        // Compute the next host id and add the task to the host with that id
        int nextHostId = (lastHostId + 1) % hosts.size();
        hosts.get(nextHostId).addTask(task);
        lastHostId = nextHostId;
    }


    /**
     * Add a task to a host queue using the Shortest Queue algorithm.
     * @param task the task to be added
     */
    public void addTaskSQ(Task task) {
        // Firstly find the host with the smallest queue size
        for (Host h : hosts) {
            if (h.getQueueSize() < minQueueSize) {
                minQueueSize = h.getQueueSize();
            }
        }

        // Assign task to the host with the smallest queue size
        for (Host host : hosts) {
            if (host.getQueueSize() == minQueueSize) {
                host.addTask(task);
                minQueueSize = Integer.MAX_VALUE;
                return;
            }
        }
    }
}
