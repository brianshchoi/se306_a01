package schedule;

import model.Task;

public interface Processor {

    int addAtTime(int time, Task task);

    int getMakespan();

    Task getLatestTask();
}
