package app;

import scheduleModel.ISchedule;

/**
 * Make algorithm polymorpic in case we want to
 * switch out algorithms and try different things.
 */
public interface IAlgorithm {
    ISchedule run();
    ISchedule getBestSchedule();
}
