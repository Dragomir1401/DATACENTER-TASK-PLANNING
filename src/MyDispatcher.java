/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    int minQueueSize = Integer.MAX_VALUE;
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
            default -> System.out.println("Invalid scheduling algorithm.");
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