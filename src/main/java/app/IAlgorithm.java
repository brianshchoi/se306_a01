package app;

import scheduleModel.ISchedule;

public interface IAlgorithm {

    ISchedule run();

    ISchedule getBestSchedule();
}
