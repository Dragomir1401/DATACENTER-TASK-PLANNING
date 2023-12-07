/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    int minQueueSize = Integer.MAX_VALUE;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        // Firstly find the host with the smallest queue size
        for (Host h : hosts) {
            // Print the queue size of each host
            System.out.println("Host " + h.getId() + " has queue size " + h.getQueueSize());
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