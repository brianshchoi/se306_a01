package schedule;

import model.Task;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

    Map<Integer, Processor> processors = new HashMap<>();
    public int getMakeSpan() {


        return 0;
    }

    public void scheduleOnProcessor(int processor, Task task, int scheduleTime) {


        Processor proc = processors.get(processor);
        proc.addAtTime(scheduleTime, task);
    }
}
