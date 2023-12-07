/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        // Firstly use the Round Robin algorithm to decide the host
        int minQueueSize = Integer.MAX_VALUE;
        for (Host h : hosts) {
            if (h.getQueueSize() < minQueueSize) {
                minQueueSize = h.getQueueSize();
            }
        }

        for (Host host : hosts) {
            // Assign task to the host with the smallest queue size
            if (host.getQueueSize() == minQueueSize) {
                host.addTask(task);
                return;
            }
        }
    }
}
