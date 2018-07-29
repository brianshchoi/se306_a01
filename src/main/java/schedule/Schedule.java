package schedule;

import model.Task;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

    private Map<Integer, Processor> processors = new HashMap<>();
    public int getMakeSpan() {
        int max = 0;
        for (Processor processor: processors.values()) {
            int current = processor.getMakespan();
            max = current > max ? current : max;
        }

        return max;
    }

    public void scheduleOnProcessor(int processor, Task task, int scheduleTime) {
        Processor proc = processors.get(processor);
        proc.addAtTime(scheduleTime, task);
    }
}
