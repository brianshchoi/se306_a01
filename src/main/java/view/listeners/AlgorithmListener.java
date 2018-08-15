package view.listeners;

import scheduleModel.ISchedule;

public interface AlgorithmListener {
    void bestScheduleUpdated(ISchedule schedule);
    void algorithmFinished();
}
