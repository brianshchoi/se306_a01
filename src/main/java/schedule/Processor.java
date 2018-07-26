package schedule;

import model.Task;

public interface Processor {

    int addAtTime(int time);

    int getMakespan();

    Task getLatestTask();
}
